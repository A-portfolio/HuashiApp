package com.muxistudio.appcommon.net;


import com.muxistudio.common.util.NetUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kolibreath on 17-12-12
 */

public class NetStatusInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetUtil.isConnected()) {
         //   RxBus.getDefault().send(new NetErrorEvent());
        }
        return chain.proceed(request);
    }
}
