package com.muxistudio.appcommon.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
//import android.support.v4.app.NotificationCompat;

import androidx.core.app.NotificationCompat;

import com.muxistudio.appcommon.R;
import com.muxistudio.common.base.Global;


/**
 * Created by ybao on 16/5/16.
 */
public class NotifyUtil {

    public static void show(Context context,Class<?> targetActivity,String title, String content,String uiName){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent intent = new Intent(context,targetActivity);
        if (!uiName.equals("")){
            intent.putExtra("ui",uiName);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context,123,new Intent(context,targetActivity),PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap bitmap = BitmapFactory.decodeResource(Global.getApplication().getResources(), R.mipmap.ic_launcher);
        builder.setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);

        notificationManager.notify(2,builder.build());
    }

    public static void show(Context context,Class<?> targetActvity,String title,String content){
        show(context,targetActvity,title,content,"");
    }
}
