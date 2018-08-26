package com.muxistudio.appcommon.net;

public class MapCampusFactory {
    private volatile static RetrofitService sRetrofitService = null;

    public static RetrofitService getRetrofitService(){
        RetrofitService retrofitService = sRetrofitService;
        if (retrofitService == null){
            synchronized (MapCampusFactory.class){
                retrofitService = sRetrofitService;
                if (retrofitService == null) {
                    sRetrofitService = new MapCampusRetrofit().getRetrofitService();
                    retrofitService = sRetrofitService;
                }
            }
        }
        return retrofitService;
    }

}
