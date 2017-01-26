package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.widget.TimeTable;

/**
 * Created by ybao on 16/4/27.
 * 星期布局
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

    public boolean isXScrollOut(int scrollX) {
        if (scrollX < 0 || scrollX > TimeTable.WEEK_DAY_WIDTH * 7) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void scrollBy(int x, int y ) {
        x = checkPositionX(x);
        y = checkPositionY(y);
        super.scrollBy(x, y);
    }

    public int checkPositionX(int x) {
        if (getScrollX() + x < 0) {
            x = -getScrollX();
        } else if ((getScrollX() + x) >
                (TimeTable.WEEK_DAY_WIDTH * 7 - DimensUtil.getScreenWidth()
                        + TimeTable.LITTLE_VIEW_WIDTH)) {
            x = TimeTable.WEEK_DAY_WIDTH * 7 - getScrollX() - DimensUtil.getScreenWidth()
                    + TimeTable.LITTLE_VIEW_WIDTH;
        }
        return x;
    }

    public int checkPositionY(int y) {
        if (getScrollY() + y < 0) {
            y = -getScrollY();
        } else if ((getScrollY() + y) >
                (TimeTable.COURSE_TIME_HEIGHT * 7 - DimensUtil.getScreenHeight()
                        + ScheduleActivity.WEEK_LAYOUT_HEIGHT
                        + DimensUtil.getActionbarHeight() + DimensUtil.getStatusBarHeight()
                        + TimeTable.LITTLE_VIEW_HEIGHT)) {
            y = TimeTable.COURSE_TIME_HEIGHT * 7 - getScrollY() - DimensUtil.getScreenHeight()
                    + ScheduleActivity.WEEK_LAYOUT_HEIGHT
                    + DimensUtil.getActionbarHeight() + DimensUtil.getStatusBarHeight()
                    + TimeTable.LITTLE_VIEW_HEIGHT;
        }
        return y;
    }

}
