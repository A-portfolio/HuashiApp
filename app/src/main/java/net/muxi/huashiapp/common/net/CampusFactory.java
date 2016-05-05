package net.muxi.huashiapp.common.net;

/**
 * Created by ybao on 16/4/28.
 * 网络的工厂类
 */
public class CampusFactory {

    public static final Object monitor = new Object();
    public static RetrofitService sRetrofitService = null;

    public static RetrofitService getRetrofitService(){
        synchronized (monitor){
            if (sRetrofitService == null){
                sRetrofitService = new CampusRetrofit().getRetrofitService();
            }
            return sRetrofitService;
        }
    }
}
