package net.muxi.huashiapp.provider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.service.WidgetService;
import net.muxi.huashiapp.util.DateUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.TimeTableUtil;

import java.util.Date;

/**
 * Created by ybao on 16/11/2.
 */

public class ScheduleWidgetProvider extends AppWidgetProvider {

    private RemoteViews rv;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Logger.d(intent.getAction());
        if (intent.getAction().equals("android.intent.action.TIME_SET") ||
                intent.getAction().equals("android.intent.action.WidgetProvider") ||
                intent.getAction().equals("android.appwidget.action.APPWIDGET_UPDATE")) {

            Logger.d("begin update list");
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = widgetManager.getAppWidgetIds(
                    new ComponentName(context, ScheduleWidgetProvider.class));

            if (appWidgetIds.length > 0) {
                Intent widgetServiceIntent = new Intent(context,WidgetService.class);
                rv = new RemoteViews(context.getPackageName(), R.layout.widget_schedule);
                int week = TimeTableUtil.getCurWeek();
                String weekday = Constants.WEEKDAYS_XQ[DateUtil.getDayInWeek(new Date()) - 1];
                rv.setTextViewText(R.id.tv_weekday, String.format("第%d周%s", week, weekday));
                rv.setRemoteAdapter(R.id.lv,widgetServiceIntent);
                widgetManager.updateAppWidget(appWidgetIds, rv);
                widgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv);
            }
        }
    }

//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        updateWidget(context, appWidgetManager, appWidgetIds);
//        super.onUpdate(context, appWidgetManager, appWidgetIds);
//
//    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        int appWidgetId = appWidgetIds[0];
        Logger.d(appWidgetId + "");
        Intent intent = new Intent(context, WidgetService.class);
        rv = new RemoteViews(context.getPackageName(), R.layout.widget_schedule);
        int week = TimeTableUtil.getCurWeek();
        String weekday = Constants.WEEKDAYS_XQ[DateUtil.getDayInWeek(new Date()) - 1];
        rv.setTextViewText(R.id.tv_weekday, String.format("第%d周%s", week, weekday));
        rv.setRemoteAdapter(R.id.lv,intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds,R.id.lv);
        appWidgetManager.updateAppWidget(appWidgetId,rv);
    }


}
