package net.muxi.huashiapp.ui.score;

import com.muxistudio.appcommon.data.Score;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.user.UserAccountManager;


import java.util.List;

import retrofit2.HttpException;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RequestRetry implements
        Func1<Observable<? extends Throwable>, Observable<?>> {

        private int maxRetries;
        private int retryCount = 0;
        //传递的是一个Obsevable[]的引用
        private Observable<List<Score>> listObservable[] ;

        private RetryInfoListener mListener;

        //todo to refactor
    // using following method :https://www.linkedin.com/pulse/switch-case-if-else-blocks-rx-java-streams-ahmed-adel
        @Override
        public Observable<?> call(Observable<? extends Throwable> attempts) {
            return attempts
                    .flatMap((Func1<Throwable, Observable<?>>) throwable -> {
                        throwable.printStackTrace();
                        CcnuCrawler2.clearCookieStore();

                        if(++retryCount > maxRetries){
                            //然后对这个throwbale的code进行判断再处理
                            return Observable.error(throwable);
                        }

                        if(throwable instanceof HttpException){
                            int code = ((HttpException) throwable).code();
                            switch (code){
                                //这两种情况下都会导致要重新写cookie
                                case 500:
                                case 403:

                                    return new LoginPresenter()
                                            .login(UserAccountManager.getInstance()
                                                    .getInfoUser())
                                            //上面的登录操作运行在io线程上
                                            .observeOn(Schedulers.io())
                                                //让这一段修改的线程运行在UI上
                                            .subscribeOn(AndroidSchedulers.mainThread())
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
                                                            scoreList ->
                                                            Observable.from(scoreList))
                                                    .toList());

                                case 404:
                                    //todo to implements
                                    break;
                            }
                        }
                        return Observable.error(throwable);
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
}
