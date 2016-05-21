package net.muxi.huashiapp.common.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import net.muxi.huashiapp.R;

/**
 * Created by ybao on 16/5/16.
 */
public class NotifyUtil {

    public static void show(Context context,Class<?> targetActivity,String title, String content){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,123,new Intent(context,targetActivity),PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_action_navigation_arrow_back)
                .setAutoCancel(true);
        Log.d("notify","it's time");

        notificationManager.notify(2,builder.build());
    }
}
