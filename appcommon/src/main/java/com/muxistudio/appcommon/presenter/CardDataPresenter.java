package com.muxistudio.appcommon.presenter;

import com.muxistudio.appcommon.appbase.ICardView;
import com.muxistudio.appcommon.data.CardDailyUse;
import com.muxistudio.appcommon.data.CardDataEtp;
import com.muxistudio.appcommon.net.ccnu.CcnuService2;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.common.util.DateUtil;
import com.muxistudio.common.util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class CardDataPresenter {


    private HashMap<String ,String > mCookieMap = new HashMap<>();
    private String mConcatCookie = "";
    private CardDataEtp mCardDataEtp;

    private ICardView iCardView;
    public CardDataPresenter(ICardView view){
        this.iCardView = view;
    }

    private static final String SESSION_ID_URL = "http://weixin.ccnu.edu.cn/Analysis/Rdt?uuu=http%3a%2f%2fweixin.ccnu.edu.cn%2fqyh%2fphone%2fSudoku%3faid%3d22_wx7219d601c7/";
    private static final String CARD_DATA_URL = "http://weixin.ccnu.edu.cn/App/weixin/CardInfoAjax/";
    //新的模拟登录华中师范大学微信企业号获取用户本月消费和刷卡次数信息
    //具体文档:
    //https://www.zybuluo.com/Humbert/note/1149745
    private String WXQUERYID     = "wxqyuserid";
    private String mSessionID = "";
    private static final String ASP_NET_SESSION =   "ASP.NET_SessionId";
    private List<Cookie> mCookieStore = new ArrayList<>();

    public Observable getCardObservable(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CardCookieJar())
                .followRedirects(false)
                .addInterceptor(interceptor)
                .build();
        Request request = new Request.Builder()
                .url(SESSION_ID_URL)
                .addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E216 MicroMessenger/6.6.6 NetType/WIFI Language/zh_CN")
                .addHeader("wxqyuserid", UserAccountManager.getInstance().getInfoUser().sid)
                .build();

        OkHttpClient client1 = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client1)
                .baseUrl(CARD_DATA_URL)
                .build();

        CcnuService2 service = retrofit.create(CcnuService2.class);

        // 获取sessionId
        Observable<Response> sessionIdObservable = Observable.defer(() -> {
            try {
                Response response = client.newCall(request).execute();
                return Observable.just(response);
            } catch (IOException e) {
                e.printStackTrace();
                return Observable.error(e);
            }
        });

        return sessionIdObservable
                .subscribeOn(Schedulers.io())
                .flatMap((Func1<Response, Observable<CardDataEtp>>) response ->
                {
                    //cookie 分为两个部分: 一个部分是 User-Agent 另外的一个部分是Cookie 和 wxqyuserid 组合起来的一个字段
                    //有点奇怪但是如果不是这样的形式没法请求成功
                    mCookieMap = new HashMap<>();
                    mCookieMap.put("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit");
                    mConcatCookie = ASP_NET_SESSION + "=" + mSessionID + ";"
                            +WXQUERYID + "=" + UserAccountManager.getInstance().getInfoUser().sid;
                    mCookieMap.put("Cookie",mConcatCookie);
                    Observable<CardDataEtp> cardDataObservable = service.getCardData(mCookieMap);
                    return cardDataObservable;
                })
                .flatMap((Func1<Object, Observable<CardDailyUse>>) o -> {
                    CardDataEtp use = (CardDataEtp) o;
                    mCardDataEtp = use;


                    //这里的header可以和上面完全相同
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR));
                    Date today = calendar.getTime();
                    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) -7 );
                    Date seven = calendar.getTime();
                    String todayDays = DateUtil.toDateInYear(today);
                    String sevenDays = DateUtil.toDateInYear(seven);

                    Observable<CardDailyUse> cardDailyUseObservable = service.getCardDailyData(mCookieMap
                            ,1,100,sevenDays,todayDays);


                    return cardDailyUseObservable;
                });
    }

    public void setCardView() {
        getCardObservable()
                 .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() { }
                    @Override
                    public void onError(Throwable e) {e.printStackTrace();
                        Logger.d("card");}
                    @Override
                    public void onNext(Object o) {
                        CardDailyUse use = (CardDailyUse) o;
                        if(iCardView !=  null)
                            iCardView.initView(use,mCardDataEtp);
                    }
                });
    }

    public CardDataEtp getCardDataEtp(){
        return  mCardDataEtp;
    }

    private class CardCookieJar implements CookieJar {

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            mCookieStore.addAll(cookies);
            for(Cookie cookie: mCookieStore){
                if(cookie.name().equals(ASP_NET_SESSION)){
                  mSessionID = cookie.value();
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            return mCookieStore;
        }
    }



}
