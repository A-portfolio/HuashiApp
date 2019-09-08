package net.muxi.huashiapp.login;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.data.User;
import com.muxistudio.appcommon.event.LibLoginEvent;
import com.muxistudio.appcommon.event.LoginSuccessEvent;
import com.muxistudio.appcommon.net.ccnu.CcnuCrawler2;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.common.util.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.sina.weibo.sdk.statistic.WBAgent.TAG;

/**
 * Created by messi-wpy
 * <p>
 * Date: 2019-5-19
 */

public class CcnuCrawler3 {

    private Subscription loginSubscription;
    private Subscription libSubscription;
    private SingleCCNUClient client;
    public CcnuService3 clientWithRetrofit;
    private Date date;


    public SingleCCNUClient getClient() {
        return client;
    }

    public CcnuCrawler3() {
        date = new Date();
        client = new SingleCCNUClient();
        clientWithRetrofit = SingleCCNUClient.getClient();
    }

    public void performLogin(Subscriber<ResponseBody> subscriber, final User user) {

        //其实这里可以把统一验证的登录  与 教务处登录, 图书馆登录分开，写三个observable（这样其实更符合浏览器的逻辑）
        // 这里只将图书馆分开了，影响不大
        loginSubscription = clientWithRetrofit.firstLogin()
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<Response<ResponseBody>, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> call(Response<ResponseBody> response) {
                        if (response.code() != 200)
                            return Observable.error(new HttpException(response));

                        String html = " ";
                        try {
                            html = response.body().string();
                        } catch (Exception e) {
                            return Observable.error(e);
                        }

                        //判断是否已经登录过了
                        if (isLogined(html)) {
                            Log.i(TAG, "call: has logined");

                            return clientWithRetrofit.performSystemLogin()
                                    .flatMap(new Func1<ResponseBody, Observable<ResponseBody>>() {
                                        @Override
                                        public Observable<ResponseBody> call(ResponseBody responseBody) {


                                            // FIXME: 19-9-5 
                                            return Observable.empty();
                                        }
                                    });


                        }


                        //这步获取cookie是因为它是下一次请求的url参数
                        String valueOfcookie;
                        try {
                            List<String> cookies = response.headers().values("Set-Cookie");
                            if (cookies == null || cookies.size() == 0)
                                return Observable.error(new NullPointerException("cookie ==null"));
                            int index = cookies.get(0).indexOf('=');
                            valueOfcookie = cookies.get(0).substring(index + 1);
                            Log.i(TAG, "first call in flatmap: cookie  " + cookies.get(0));
                        } catch (Exception e) {
                            return Observable.error(new NullPointerException("first reponse cookie wrong"));
                        }

                        String[] params = null;
                        params = getWordFromHtml(html);
                        Log.i(TAG, "call: regex get param from html:" + params[0] + "  " + params[1]);
                        if (params == null)
                            return Observable.error(new NullPointerException("first html get words wrong"));

                        return clientWithRetrofit.performCampusLogin(valueOfcookie, user.sid, user.password, params[0], params[1], "submit", "登录");
                    }
                }).flatMap(new Func1<ResponseBody, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> call(ResponseBody responseBody) {
                        String html = null;
                        try {
                            html = responseBody.string();
                        } catch (IOException e) {
                            return Observable.error(e);

                        }
                        if (loginFailed(html)) {
                            Log.i(TAG, "call: 密码错误");
                            return Observable.error(new Throwable("密码错误"));
                        }

                        Log.i(TAG, "call: first 学校系统登录完成，下一步进行教务处登录验证");
                        return clientWithRetrofit.performSystemLogin();
                    }
                })
                .retry(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


        //图书馆登录
        libSubscription = Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (loginSubscription == null) return;
                int time = 0;
                while (!loginSubscription.isUnsubscribed() && time < 10) {
                    Log.i(TAG, "call: wait");
                    try {
                        Thread.sleep(500);
                        time++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (time == 10)
                    subscriber.onError(new Throwable("图书馆登录失败"));
                else
                    subscriber.onNext("start");
            }
        }).subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> call(String s) {
                        return clientWithRetrofit.perLibLogin();
                    }
                }).subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: lib login finish");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: lib login fail");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        Log.i(TAG, "onCompleted: lib login finish");
                        getClient().saveCookieToLocal();

                        //RxBus.getDefault().send(new LibLoginEvent());
                    }
                });


    }

    /**
     * 匹配：
     * <input type="hidden" name="lt" value="LT-31315-O4Nt1gZeHUSnmzr4DALQwyn3xNyir6-account.ccnu.edu.cn" />
     * <input type="hidden" name="execution" value="e1s1" />
     *
     * @param html
     * @return string[] length=2,string[0]=lt,string[1]=execution
     */
    private String[] getWordFromHtml(String html) throws NullPointerException {
        if (TextUtils.isEmpty(html))
            throw new NullPointerException("first login html==null");
        String[] res = new String[2];
        Pattern ltPattern = Pattern.compile("name=\"lt\" value=\"(.+?)\" />");
        Pattern executionP = Pattern.compile("name=\"execution\" value=\"(.+?)\"");
        Matcher m1 = ltPattern.matcher(html);
        Matcher m2 = executionP.matcher(html);
        if (m1.find())
            res[0] = m1.group(1);
        else res[0] = null;
        //todo 观察 if execution的值确实不变的话，可以删除，直接填 e1s1
        if (m2.find())
            res[1] = m2.group(1);
        else res[1] = null;


        return res;
    }

    /**
     * md 单点登录异常
     * <p>
     *    		尊敬的用户：<br />
     *    		系统单点登录出现异常，请及时与系统管理员联系。<br />
     *
     *  		身份验证超时！请检查网络通信是否正常。
     *
     * </p>
     * @return
     */
    private boolean isSingleSignOn(String html){
        if (html.contains("系统单点登录出现异常")){
            return true;
        }else
            return false;


    }

    private boolean isLogined(String html) {
        Pattern p = Pattern.compile("<div id=\"msg\" class=\"success\">.+?</div>", Pattern.DOTALL);
        Matcher m = p.matcher(html);
        if (m.find()) {
            Log.i(TAG, "isLogined: ");
            return true;
        } else {
            Log.i(TAG, "has not Logined or out of data ");
            return false;
        }
    }

    //判断登录失败(密码错误,依然返回200...),
    private boolean loginFailed(String html) {
        Pattern p = Pattern.compile("<div id=\"msg\" class=\"errors\">.+?</div>", Pattern.DOTALL);
        Matcher m = p.matcher(html);
        if (m.find()) {
            Log.i(TAG, "faild 密码错误 ");
            return true;
        } else {
            return false;
        }

    }

    private String getCookieValueFromHeader(String header) {
        int index = header.indexOf('=');
        String valueOfcookie = header.substring(index + 1);
        return valueOfcookie;
    }

    public void unsubscription() {
        if (loginSubscription != null && loginSubscription.isUnsubscribed())
            loginSubscription.unsubscribe();
        if (libSubscription != null && libSubscription.isUnsubscribed())
            libSubscription.unsubscribe();
    }

    public void saveLoginState(Intent intent, User user, String type) {
        UserAccountManager.getInstance().saveInfoUser(user);
        MobclickAgent.onProfileSignIn(user.getSid());
        String target = intent.hasExtra("target") ?
                intent.getStringExtra("target") : null;
            RxBus.getDefault().send(new LoginSuccessEvent(target));

    }


}
