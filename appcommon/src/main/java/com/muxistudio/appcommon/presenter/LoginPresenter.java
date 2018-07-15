package com.muxistudio.appcommon.presenter;

import android.content.Intent;

import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.data.User;
import com.muxistudio.appcommon.event.LibLoginEvent;
import com.muxistudio.appcommon.event.LoginSuccessEvent;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;

import rx.Observable;

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
    //在完成登陆之后无论是否成功需要清除了 cookieStore
    public Observable<Boolean> login(User user){
        return Observable.create(subscriber -> {
            subscriber.onStart();
            boolean crawlerResult = false;
            try {
                crawlerResult = CcnuCrawler2.performLogin(user.sid, user.password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            subscriber.onNext(crawlerResult);
            subscriber.onCompleted();
        });
    }

    public void saveLoginState(Intent intent,User user,String type){
        UserAccountManager.getInstance().saveInfoUser(user);
        MobclickAgent.onProfileSignIn(user.getSid());
        String target = intent.hasExtra("target") ?
                intent.getStringExtra("target") : null;
        if (type.equals("info")) {
            RxBus.getDefault().send(new LoginSuccessEvent(target));
        } else {
            RxBus.getDefault().send(new LibLoginEvent());
        }
        CcnuCrawler2.saveCookies();
    }
}
