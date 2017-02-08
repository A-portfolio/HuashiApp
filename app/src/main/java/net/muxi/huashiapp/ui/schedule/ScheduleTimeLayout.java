package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by ybao on 16/4/27.
 * 星期，课程时间布局
 */
public class ScheduleTimeLayout extends LinearLayout {

    public Scroller mScroller;

    public ScheduleTimeLayout(Context context) {
        this(context,null);
    }

    public ScheduleTimeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

}
