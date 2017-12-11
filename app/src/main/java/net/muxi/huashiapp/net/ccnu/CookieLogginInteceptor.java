package net.muxi.huashiapp.net.ccnu;

import android.util.Log;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kolibreath on 17-12-11.
 */

public class CookieLogginInteceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request  = chain.request();
        Headers headers = request.headers();
        Set<String> names = headers.names();
        String logging = "";
        Iterator<String> it  =  names.iterator();
        while (it.hasNext()){
            logging += it.next() + "= "+ headers.values(it.next())+"\n";
        }
        Log.d("loggings", "intercept: "+logging);
        return chain.proceed(request);
    }
}
