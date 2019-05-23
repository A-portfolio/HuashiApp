package net.muxi.huashiapp.login;

import android.text.TextUtils;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class GetScorsePresenter {
    private final static String TAG="GetScores";
    private static boolean logined =false;
    private Subscription loginSubscription;
    private Subscription scoreSubscription;
    private Date date;
    private static AtomicInteger time;


    public void setLgoined(boolean l){
        logined=l;
    }
    public boolean isLogined(){
        return logined;

    }
    public CcnuService3 clientWithRetrofit;
    public GetScorsePresenter(){
            clientWithRetrofit= SingleCCNUClient.getClient();
            date=new Date();

    }
    public CcnuService3 getClientWithRetrofit(){
        return clientWithRetrofit;
    }

    //登录教务处获取cookie
    public void LoginJWC(Subscriber<ResponseBody>subscriber){
        if (isLogined())return ;
       loginSubscription= clientWithRetrofit.firstLogin()
                          .subscribeOn(Schedulers.io())
                          .flatMap(new Func1<Response<ResponseBody>, Observable<ResponseBody>>() {
                              @Override
                              public Observable<ResponseBody> call(Response<ResponseBody> response) {
                                  if (response.code()!=200)
                                      return Observable.error(new HttpException(response));
                                  List<String> cookies=response.headers().values("Set-Cookie");
                                  if (cookies==null||cookies.size()==0)
                                      return Observable.error(new NullPointerException("cookie ==null"));
                                  int index=cookies.get(0).indexOf('=');
                                  String valueOfcookie=cookies.get(0).substring(index+1);
                                  Log.i(TAG, "first call in flatmap: cookie  "+cookies.get(0));
                                  String[]params=null;
                                  try {
                                    params= getWordFromHtml(response.body().string());
                                      Log.i(TAG, "call: regex get param from html:" + params[0]+"  "+params[1]);
                                  } catch (Exception e) {
                                      e.printStackTrace();
                                      return Observable.error(e);
                                  }
                                    if (params==null)
                                        return Observable.error(new NullPointerException("first html get words wrong"));

                                  return clientWithRetrofit.performCampusLogin(valueOfcookie,"2017212163","13569158099",params[0],params[1],"submit","登录");
                              }
                          }).flatMap(new Func1<ResponseBody, Observable<ResponseBody>>() {
            @Override
            public Observable<ResponseBody> call(ResponseBody responseBody) {
                //todo 完善错误处理
                Log.i(TAG, "call: first 学校系统登录完成，下一步进行教务处登录验证");
                return clientWithRetrofit.performSystemLogin();
            }
        }).subscribe(subscriber);
        Log.i(TAG, "LoginJWC: subscription :"+loginSubscription.isUnsubscribed());
    }


    public void getScores(Subscriber<ResponseBody>subscriber){
        if (loginSubscription==null&&!isLogined()){
            Log.e(TAG, "getScores: wrong"+"未登录或登录失败,使用前请确认调用loginJWC()方法" );
            return ;
        }
       scoreSubscription= Observable.unsafeCreate(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (loginSubscription==null)return;
                while (!loginSubscription.isUnsubscribed()){
                    Log.i(TAG, "call: wait");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                subscriber.onNext("start");
            }
        }).subscribeOn(Schedulers.newThread())
                .flatMap(new Func1<String, Observable<ResponseBody>>() {
                    @Override
                    public Observable<ResponseBody> call(String s) {
                        return clientWithRetrofit.getScores("2018","3",false, String.valueOf(date.getTime()),15,1,"","asc", time.get());
                    }
                }).subscribe(subscriber);


    }


    /**
     * 匹配：
     * <input type="hidden" name="lt" value="LT-31315-O4Nt1gZeHUSnmzr4DALQwyn3xNyir6-account.ccnu.edu.cn" />
     * <input type="hidden" name="execution" value="e1s1" />
     * @param html
     * @return string[] length=2,string[0]=lt,string[1]=execution
     */
    private String[] getWordFromHtml(String html) throws NullPointerException {
        if (TextUtils.isEmpty(html))
            throw new NullPointerException("first login html==null");
        String[] res=new String[2];
        Pattern ltPattern= Pattern.compile("name=\"lt\" value=\"(.+?)\" />");
        Pattern executionP= Pattern.compile("name=\"execution\" value=\"(.+?)\"");
        Matcher m1=ltPattern.matcher(html);
        Matcher m2=executionP.matcher(html);
        if (m1.find())
            res[0]=m1.group(1);
        else res[0]=null;
        //todo 观察 if execution的值确实不变的话，可以删除，直接填 e1s1
        if (m2.find())
            res[1]=m2.group(1);
        else res[1]=null;
        return res;
    }

    private String getCookieValueFromHeader(String header){
        int index=header.indexOf('=');
        String valueOfcookie=header.substring(index+1);
        return valueOfcookie;

    }
    public void unsubscription(){
        if (scoreSubscription!=null&&!scoreSubscription.isUnsubscribed())
            scoreSubscription.unsubscribe();
        if (loginSubscription!=null&&loginSubscription.isUnsubscribed())
            loginSubscription.unsubscribe();
    }


}
