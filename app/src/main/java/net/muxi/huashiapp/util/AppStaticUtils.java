package net.muxi.huashiapp.util;

import android.content.Context;
import com.umeng.analytics.MobclickAgent;
import java.util.HashMap;

/**
 * Created by kolibreath on 18-3-25.
 */

public class AppStaticUtils {

  public static void onPause(Context context){
    MobclickAgent.onPause(context);
  }

  public static void onResume(Context context){
    MobclickAgent.onResume(context);
  }

  //计数事件
  public static void onEvent(Context context,String id){
    MobclickAgent.onEvent(context,id);
  }

  public static void onEvent(Context context,String id,HashMap<String,String>map ){
    MobclickAgent.onEvent(context,id,map);
  }
}
