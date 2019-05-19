package net.muxi.huashiapp.login;

import net.muxi.huashiapp.App;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingleRetrofit {

    private volatile static CcnuService3 client=null;
    private static OkHttpClient okHttpClient;
    public static CcnuService3 getClient(){
        if (client ==null){
            synchronized (SingleRetrofit.class) {
                if (client == null) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY);
                     okHttpClient = new OkHttpClient.Builder()
                            .cookieJar(new MyCookieJar(App.getContext()))
                            .addInterceptor(new AddHeadInterceptor())
                            .addInterceptor(interceptor)
                            .build();

                    client=new Retrofit.Builder()
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

    public static void saveCookieToLocal(){
        if (okHttpClient==null)
            return;
        ((MyCookieJar)okHttpClient.cookieJar()).saveCookieToLocal();
    }
}
