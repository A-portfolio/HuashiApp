package net.muxi.huashiapp.util;

import com.zhuge.analysis.stat.ZhugeSDK;

import net.muxi.huashiapp.App;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ybao on 16/8/15.
 */
public class ZhugeUtils {

    public static void sendEvent (String title,String detail){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(title,detail);
            ZhugeSDK.getInstance().track(App.sContext,title,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
