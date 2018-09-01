package net.muxi.huashiapp.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.muxistudio.appcommon.Constants;
import java.util.Locale;
import net.muxi.huashiapp.utils.TimeTableUtil;
import com.muxistudio.common.util.DateUtil;
import com.muxistudio.common.util.Logger;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.service.WidgetService;
import net.muxi.huashiapp.ui.main.MainActivity;

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

            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = widgetManager.getAppWidgetIds(
                    new ComponentName(context, ScheduleWidgetProvider.class));

            if (appWidgetIds.length > 0) {
                Intent widgetServiceIntent = new Intent(context,WidgetService.class);
                rv = new RemoteViews(context.getPackageName(), R.layout.widget_schedule);
                int week = TimeTableUtil.getCurWeek();
                String weekday = Constants.WEEKDAYS_XQ[DateUtil.getDayInWeek(new Date()) - 1];
                rv.setTextViewText(R.id.tv_weekday, String.format(Locale.CHINESE,"第%d周%s", week, weekday));
                rv.setRemoteAdapter(R.id.lv,widgetServiceIntent);
                widgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv);
                rv.setEmptyView(R.id.lv,R.id.layout_empty);

                Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.putExtra("ui", "table");
                PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                rv.setPendingIntentTemplate(R.id.lv, mainPendingIntent);

                widgetManager.updateAppWidget(appWidgetIds, rv);
            }
        }
    }

}
