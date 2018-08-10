package net.muxi.huashiapp.ui.score;

import com.muxistudio.appcommon.data.Score;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.user.UserAccountManager;


import java.util.List;

import retrofit2.HttpException;
import rx.Observable;
import rx.functions.Func1;

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
                    .flatMap((Func1<Throwable, Observable<?>>) throwable -> {
                        throwable.printStackTrace();
                        CcnuCrawler2.clearCookieStore();

                        if(++retryCount > maxRetries){
                            //然后对这个throwbale的code进行判断再处理
                            return Observable.error(throwable);
                        }

                        //score credit通用的处理逻辑 如果一旦有着两种异常 需要整个重试
                        Observable<List<Score>> scoreObservable =
                                Observable.merge(listObservable, 5)
                                        .flatMap((Func1<List<Score>, Observable<Score>>) scoreList ->
                                                Observable.from(scoreList))
                                        .toList();

                        if(throwable instanceof HttpException){
                            int code = ((HttpException) throwable).code();
                            switch (code){
                                //这两种情况下都会导致要重新写cookie
                                case 500:
                                case 403:
                                    if(mListener!=null)
                                        mListener.onRetry();

                                    return new LoginPresenter().
                                            login(UserAccountManager.getInstance()
                                                    .getInfoUser())
                                            .flatMap(aubBoolean -> scoreObservable);
                                case 404:
                                    //todo to implements
                                    break;
                            }
                        }


                        return Observable.error(throwable);
                    });
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
