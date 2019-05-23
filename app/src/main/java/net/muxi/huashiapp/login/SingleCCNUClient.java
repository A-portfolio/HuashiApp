package net.muxi.huashiapp.login;

import android.support.annotation.Nullable;

import net.muxi.huashiapp.App;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleCCNUClient {

    private volatile static CcnuService3 client = null;
    private static OkHttpClient okHttpClient;

    public static CcnuService3 getClient() {
        if (client == null) {
            synchronized (SingleCCNUClient.class) {
                if (client == null) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY);
                    okHttpClient = new OkHttpClient.Builder()
                            .cookieJar(new MyCookieJar(App.getContext()))
                            .addInterceptor(new AddHeadInterceptor())
                            .addInterceptor(interceptor)
                            .addNetworkInterceptor(new RedirectInterceptor())
                            .build();

                    client = new Retrofit.Builder()
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .baseUrl("http://xk.ccnu.edu.cn/")
                            .build()
                            .create(CcnuService3.class);
                }
            }

        }

        return client;
    }


    public void saveCookieToLocal() {
        if (okHttpClient == null)
            return;
        ((MyCookieJar) okHttpClient.cookieJar()).saveCookieToLocal();
    }

    public void clearAllCookie() {
        if (okHttpClient == null)
            return;
        ((MyCookieJar) okHttpClient.cookieJar()).clearCookie();
    }

    public CookieJar getCookieJar() {
        if (okHttpClient == null)
            return null;
        return okHttpClient.cookieJar();
    }
}
