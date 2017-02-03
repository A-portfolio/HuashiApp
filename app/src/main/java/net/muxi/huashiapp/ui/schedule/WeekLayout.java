package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.muxi.huashiapp.Constants;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.DateUtil;

import java.util.List;

/**
 * Created by ybao on 17/1/26.
 */

public class WeekLayout extends ScheduleTimeLayout {

    private Context mContext;

    private View[] views;

    public WeekLayout(Context context) {
        this(context, null);
    }

    public WeekLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        views = new View[7];
        initView();
    }

    private void initView() {
        List<String> dateList = DateUtil.getTheWeekDate(0);
        for (int i = 0; i < 7; i++) {
            views[i] = LayoutInflater.from(mContext).inflate(R.layout.view_weekday, this, false);
            ((TextView)views[i].findViewById(R.id.tv_weekday)).setText(Constants.WEEKDAYS[i]);
            ((TextView)views[i].findViewById(R.id.tv_date)).setText(dateList.get(i));
            addView(views[i]);
        }
    }

    public void setWeekDate(int distance) {
        List<String> dateList = DateUtil.getTheWeekDate(distance);
        for (int i = 0;i < 7;i ++){
            ((TextView) views[i].findViewById(R.id.tv_date)).setText(dateList.get(i));
        }
    }

}
