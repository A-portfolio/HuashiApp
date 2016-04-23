package net.muxi.huashiapp;

import android.app.Application;
import android.content.Context;

import com.github.mmin18.layoutcast.BuildConfig;
import com.github.mmin18.layoutcast.LayoutCast;



/**
 * Created by ybao on 16/4/18.
 */
public class App extends Application{

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        if (BuildConfig.DEBUG) {
            LayoutCast.init(this);
        }
    }

    public static Context getContext(){
        return sContext;
    }
}
