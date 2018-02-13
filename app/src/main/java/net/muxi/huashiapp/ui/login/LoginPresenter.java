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
    //在完成登陆之后无论是否成功都清除了 cookieStore
    public Observable login(User user){
        return Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            subscriber.onStart();
            String crawlerResult = "-1";
            try {
                crawlerResult = CcnuCrawler2.performLogin(user.sid, user.password);
                CcnuCrawler2.clearCookieStore();
                CcnuCrawler2.loginCode = "-1";
            } catch (Exception e) {
                e.printStackTrace();
            }
            subscriber.onNext(crawlerResult);
            subscriber.onCompleted();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
