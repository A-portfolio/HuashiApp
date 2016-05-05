package net.muxi.huashiapp.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Scroller;
import android.widget.TableLayout;

import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.widget.TimeTable;

/**
 * Created by ybao on 16/4/26.
 */
public class TimeTableLayout extends TableLayout {

    public Scroller mScroller;

    public TimeTableLayout(Context context) {
        this(context, null);
    }

    public TimeTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }


    public void scrollBy(int x, int y,int flag) {
        x = checkPositionX(x);
        y = checkPositionY(y,flag);
        super.scrollBy(x, y);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    public int checkPositionX(int x) {
        if (getScrollX() + x < 0) {
            x = -getScrollX();
        } else if ((getScrollX() + x) >
                (TimeTable.WEEK_DAY_WIDTH * 7 - DimensUtil.getScreenWidth() + TimeTable.LITTLE_VIEW_WIDTH)) {
            x = TimeTable.WEEK_DAY_WIDTH * 7 - getScrollX() - DimensUtil.getScreenWidth() + TimeTable.LITTLE_VIEW_WIDTH;
        }
        return x;
    }

    public int checkPositionY(int y,int flag) {
        if (getScrollY() + y < 0) {
            y = -getScrollY();
        } else if ((getScrollY() + y) >
                (TimeTable.COURSE_TIME_HEIGHT * 7 - DimensUtil.getScreenHeight() + ScheduleActivity.SELECT_WEEK_LAYOUT_HEIGHT * flag
                        + DimensUtil.getActionbarHeight() + DimensUtil.getStatusBarHeight() + TimeTable.LITTLE_VIEW_HEIGHT)) {
            y = TimeTable.COURSE_TIME_HEIGHT * 7 - getScrollY() - DimensUtil.getScreenHeight() + ScheduleActivity.SELECT_WEEK_LAYOUT_HEIGHT * flag
                    + DimensUtil.getActionbarHeight() + DimensUtil.getStatusBarHeight() + TimeTable.LITTLE_VIEW_HEIGHT;
        }
        return y;
    }

    public void smoothScrollBy(int deltaX, int deltaY,int flag) {
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        deltaX = checkPositionX(deltaX);
        deltaY = checkPositionY(deltaY,flag);
        mScroller.startScroll(scrollX, scrollY, deltaX, deltaY, 1000);
    }


}
