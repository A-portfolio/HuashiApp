package net.muxi.huashiapp.ui.login;

import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.net.ccnu.CcnuCrawler2;

import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kolibreath on 17-12-21.
 */

public class LoginPresenter {

    public void loginRetry(User user)  {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        CcnuCrawler2.performLogin(user.sid,user.password);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

    }
    public Observable<Boolean> login(User user){
        return Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
            subscriber.onStart();
            boolean crawlerResult = false;
            try {
                crawlerResult = CcnuCrawler2.performLogin(user.sid, user.password);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if (crawlerResult)
                subscriber.onNext(crawlerResult);
                    subscriber.onCompleted();
//                return;
//            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
