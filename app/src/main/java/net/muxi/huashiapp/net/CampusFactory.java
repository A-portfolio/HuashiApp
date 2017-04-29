package net.muxi.huashiapp.net;

/**
 * Created by ybao on 16/4/28.
 * 网络的工厂类
 */
public class CampusFactory {

    public volatile static RetrofitService sRetrofitService = null;

    public static RetrofitService getRetrofitService(){
        RetrofitService retrofitService = sRetrofitService;
        if (retrofitService == null){
            synchronized (CampusFactory.class){
                retrofitService = sRetrofitService;
                if (retrofitService == null) {
                    sRetrofitService = new CampusRetrofit().getRetrofitService();
                    retrofitService = sRetrofitService;
                }
            }
        }
        return retrofitService;
    }


}
