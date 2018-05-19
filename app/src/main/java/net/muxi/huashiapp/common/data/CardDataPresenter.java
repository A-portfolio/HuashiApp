package net.muxi.huashiapp.common.data;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.net.ccnu.CcnuService2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class CardDataPresenter {

    //新的模拟登录华中师范大学微信企业号获取用户本月消费和刷卡次数信息
    //具体文档:
    //https://www.zybuluo.com/Humbert/note/1149745
    private String WXQUERYID     = "wxqyuserid";
    private String mSessionID = "";
    private static final String ASP_NET_SESSION =   "ASP.NET_SessionId";
    private List<Cookie> mCookieStore = new ArrayList<>();
    public Observable getData() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CardCookieJar())
                .followRedirects(false)
                .addInterceptor(interceptor)
                .build();
        Request request = new Request.Builder()
                .addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E216 MicroMessenger/6.6.6 NetType/WIFI Language/zh_CN")
                .addHeader("wxqyuserid", App.sUser.sid)
                .build();

        OkHttpClient client1 = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client1)
                .baseUrl("http://weixin.ccnu.edu.cn/App/weixin/CardInfoAjax")
                .build();

        CcnuService2 service = retrofit.create(CcnuService2.class);

        Observable sessionIdObservable = Observable.defer(() -> {
            try {
                okhttp3.Response response = client.newCall(request).execute();
                return Observable.just(response);
            } catch (IOException e) {
                e.printStackTrace();
                return Observable.error(e);
            }
        });

        //cookie 分为两个部分: 一个部分是 User-Agent 另外的一个部分是Cookie 和 wxqyuserid 组合起来的一个字段
        //有点奇怪但是如果不是这样的形式没法请求成功
        HashMap<String ,String > map = new HashMap<>();
        map.put("User-Agent","Mozilla/5.0 (iPhone; CPU iPhone OS 11_3 like Mac OS X) AppleWebKit");
        String concatCookie = ASP_NET_SESSION + "=" + mSessionID + ";"
                +WXQUERYID + "=" + App.sUser.sid;
        map.put("Cookie",concatCookie);
        Observable<CardDataEtp> cardDataObservable = service.getCardData(map);

        return sessionIdObservable.concatMap(o -> cardDataObservable);
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
            return null;
        }
    }

}
