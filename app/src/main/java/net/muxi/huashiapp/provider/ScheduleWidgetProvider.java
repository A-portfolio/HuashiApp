package net.muxi.huashiapp.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.schedule.ScheduleActivity;

/**
 * Created by ybao on 16/11/2.
 */

public class ScheduleWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        final int N = appWidgetIds.length;
        for (int i = 0;i < N;i ++){
            int appWidgetId = appWidgetIds[i];
            Intent intent = new Intent(context, ScheduleActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_schedule);
            rv.setOnClickPendingIntent(R.id.widget_layout,pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId,rv);
        }
    }


}
