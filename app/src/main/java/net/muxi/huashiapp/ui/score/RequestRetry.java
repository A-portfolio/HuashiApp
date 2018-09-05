package net.muxi.huashiapp.ui.score;

import com.muxistudio.appcommon.data.Score;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.user.UserAccountManager;


import java.util.List;

import retrofit2.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 服务端会偶尔返回500,或者cookie过期的时候就会触发重试，如果重试超过指定次数的话就会返回Observable.empty(),
 * 如果是500 403 404 之外的问题就会发射onError()信号中断流
 *
 * 注意：如果使用还未有成绩的年份或者学期，同样也会执行重试逻辑，最后会返回Observable.empty()，避免后面Observable流被中断
 */

public class RequestRetry implements
        Func1<Observable<? extends Throwable>, Observable<?>> {

        private int maxRetries;
        private int retryCount = 0;
        //传递的是一个Obsevable[]的引用
        private Observable<List<Score>> listObservable[] ;

        private RetryInfoListener mListener;

        @Override
        public Observable<?> call(Observable<? extends Throwable> attempts) {
            return attempts
                    .flatMap((Func1<Throwable, Observable<?>>) (Throwable throwable) -> {
                        throwable.printStackTrace();
                        CcnuCrawler2.clear();


                        if(++retryCount > maxRetries){
                            return Observable.error
                                    (new RetryException(RetryException.CONFIRM_QUERY));
                        }

                        if(throwable instanceof HttpException){
                            int code = ((HttpException) throwable).code();
                            switch (code){
                                //这两种情况下都会导致要重新写cookie
                                case 500:
                                    return  Observable.merge(listObservable, 5)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .flatMap((Func1<List<Score>, Observable<Score>>)
                                                    Observable::from)
                                            .toList();
                                case 403:

                                    return new LoginPresenter()
                                            .login(UserAccountManager.getInstance()
                                                    .getInfoUser())
                                            //上面的登录操作运行在io线程上
                                            .subscribeOn(Schedulers.io())
                                                //让这一段修改的线程运行在UI上
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .flatMap(
                                                    (Func1<Boolean, Observable<Boolean>>) aBoolean -> {
                                                if(mListener!=null)
                                                    mListener.onRetry();

                                                return Observable.just(aBoolean);
                                            })
                                            .subscribeOn(Schedulers.io())
                                            .flatMap(aubBoolean ->
                                                    Observable.merge(listObservable, 5)
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribeOn(Schedulers.io())
                                                    .flatMap((Func1<List<Score>, Observable<Score>>)
                                                            Observable::from)
                                                    .toList());

                                case 404:
                                    //todo to implements
                                    break;
                            }
                        }
                        return Observable.error(new RetryException((RetryException.NET_ERROR)));
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

        public void setRetryInfo(RetryInfoListener listener){
            this.mListener = listener;
        }

        public interface RetryInfoListener{
            void onRetry();
        }

        public  static class Builder{

            public RequestRetry mRequestRetry = new RequestRetry();

            public RequestRetry.Builder setMaxretries(int max){
                mRequestRetry.maxRetries = max;
                return this;
            }

            public RequestRetry.Builder setObservable(Observable<List<Score>> listObservable[]){
                mRequestRetry.listObservable = listObservable;
                return this;
            }

            public RequestRetry.Builder setRetryInfo(RetryInfoListener listener){
                mRequestRetry.setRetryInfo(listener);
                return this;
            }
            public RequestRetry build(){
                return mRequestRetry;
            }
        }


        public static class RetryException extends  Exception{
            public int code = 0;
            public static  final int  CONFIRM_QUERY = 100;
            public static final int NET_ERROR = 101;
            public RetryException(int code){
                this.code = code;
            }
        }
}
