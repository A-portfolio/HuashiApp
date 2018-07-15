package com.muxistudio.appcommon.presenter;

import com.muxistudio.appcommon.data.FlowUserInfo;
import com.muxistudio.appcommon.net.ccnu.CcnuService2;
import com.muxistudio.appcommon.view.IFlowView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FlowPresenter {

    private static final String LOGIN ="http://self.ccnu.edu.cn:8080/Self/login/";
    private static final String LOGIN_VERIFY="http://self.ccnu.edu.cn:8080/Self/login/verify";

    private String mUsedMin;
    //unit M
    private String mUseFlow;
    private String mBalance;

    private IFlowView mFlowView;

    private String mJsessionId;
    private String mCheckcode;

    private List<Cookie> mCookieStore = new ArrayList<>();
    public FlowPresenter(IFlowView view){
        this.mFlowView = view;
    }

    private void getFlowInfo(){
        FlowCookieJar jar = new FlowCookieJar();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client1 = new OkHttpClient.Builder()
                .cookieJar(jar)
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client1)
                .baseUrl(LOGIN)
                .build();

        CcnuService2 service2 = retrofit.create(CcnuService2.class);

        Observable<Object> loginObservable = service2.getUserFlowCookie();
        Observable<Response> loginQueryObservable = service2.getUserFlowResponse(new FlowUserInfo(mCheckcode));

        loginObservable
                .subscribeOn(Schedulers.io())
                .flatMap
                        (new Func1<Object, Observable<Response>>() {
                            @Override
                            public Observable<Response> call(Object o) {
                                
                                return null;
                            }
                        })
                .subscribe(new Subscriber<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response response) {
                        String webPage = response.body().toString();
                    }
                });


    }

    private class FlowCookieJar implements CookieJar {

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            mCookieStore.addAll(cookies);
            for(Cookie cookie: mCookieStore){
                if(cookie.name().equals("JSESSIONID")){
                    mJsessionId = cookie.value();
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            return mCookieStore;
        }
    }


}
