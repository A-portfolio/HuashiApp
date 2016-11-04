package net.muxi.huashiapp.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.service.WidgetService;
import net.muxi.huashiapp.common.util.DateUtil;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.TimeTableUtil;
import net.muxi.huashiapp.schedule.ScheduleActivity;

import java.util.Date;

/**
 * Created by ybao on 16/11/2.
 */

public class ScheduleWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Logger.d(intent.getAction());
        if (intent.getAction().equals("android.intent.action.WidgetProvider") ||
                intent.getAction().equals("android.intent.action.ACTION_DATE_CHANGED")) {
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(context,ScheduleWidgetProvider.class));
            updateWidget(context,widgetManager,appWidgetIds);

        }

    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateWidget(context, appWidgetManager, appWidgetIds);
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            Intent intent = new Intent(context, WidgetService.class);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_schedule);

            Intent activityIntent = new Intent(context, ScheduleActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);
            rv.setRemoteAdapter(appWidgetId, R.id.lv, intent);
            rv.setTextViewText(R.id.tv_week, AppConstants.WEEKS[TimeTableUtil.getCurWeek() - 1]);
            rv.setTextViewText(R.id.tv_weekday, AppConstants.WEEKDAYS[DateUtil.getDayInWeek(new Date()) - 1]);
            rv.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, rv);
            Logger.d("appwidget update");
        }
    }

}
