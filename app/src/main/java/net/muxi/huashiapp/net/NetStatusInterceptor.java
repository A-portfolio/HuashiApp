package net.muxi.huashiapp.net;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.NetStatus;
import net.muxi.huashiapp.util.ToastUtil;

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

        if (!NetStatus.isConnected()) {
            App.sActivity.runOnUiThread(() -> ToastUtil.showShort(R.string.tip_net_error));

        }
        Response response = chain.proceed(request);
        return response;
    }
}
