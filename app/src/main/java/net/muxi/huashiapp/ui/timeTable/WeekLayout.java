package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.common.util.DateUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.utils.TimeTableUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by ybao on 17/1/26.
 */

//横向的周数的布局

public class WeekLayout extends ScheduleTimeLayout {
    private Context mContext;
    private Scroller mScroller;
    private View[] views;
    public WeekLayout(Context context) {
        this(context, null);
    }
    public WeekLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScroller = new Scroller(context);
        views = new View[7];
        initView();
        setWillNotDraw(false);
    }
    private void initView() {
        List<String> dateList = DateUtil.getTheWeekDate(0);
        for (int i = 0; i < 7; i++) {
            views[i] = LayoutInflater.from(mContext).inflate(R.layout.view_weekday, this, false);
            ((TextView)views[i].findViewById(R.id.tv_weekday)).setText(Constants.WEEKDAYS[i]);
            ((TextView)views[i].findViewById(R.id.tv_date)).setText(dateList.get(i));
            if(i+1 == DateUtil.getDayInWeek(new Date())){
                views[i].setBackground(getResources().getDrawable(TimeTableUtil.getCourseBgAccordDay(i)));
                ((TextView)views[i].findViewById(R.id.tv_weekday)).setTextColor(Color.WHITE);
            }
            addView(views[i]);
        }
    }
    public void setWeekDate(int distance) {
        List<String> dateList = DateUtil.getTheWeekDate(distance);
        for (int i = 0;i < 7;i ++){
            ((TextView) views[i].findViewById(R.id.tv_date)).setText(dateList.get(i));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        canvas.drawRect(0,0,TimeTable.WEEK_DAY_WIDTH * 7,TimeTable.LITTLE_VIEW_HEIGHT,p);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void smoothScrollTo(int x,int y){
        mScroller.startScroll(mScroller.getCurrX(),mScroller.getCurrY(),x,y);
    }
}
