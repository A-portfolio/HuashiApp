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

    protected Scroller mScroller;

    public TimeTableLayout(Context context) {
        this(context, null);
    }

    public TimeTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }


    @Override
    public void scrollBy(int x, int y) {
        x = checkPositionX(x);
        y = checkPositionY(y);
        super.scrollBy(x, y);
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

    public int checkPositionY(int y) {
        if (getScrollY() + y < 0) {
            y = -getScrollY();
        } else if ((getScrollY() + y) >
                (TimeTable.COURSE_TIME_HEIGHT * 7 - DimensUtil.getScreenHeight() + DimensUtil.getToolbarHeight() + DimensUtil.getNavigationBarHeight() + TimeTable.LITTLE_VIEW_HEIGHT)) {
            y = TimeTable.COURSE_TIME_HEIGHT * 7 - getScrollY() - DimensUtil.getScreenHeight() + DimensUtil.getToolbarHeight() + DimensUtil.getNavigationBarHeight() + TimeTable.LITTLE_VIEW_HEIGHT;
        }
        return y;
    }

    public void smoothScrollBy(int deltaX, int deltaY) {
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        deltaX = checkPositionX(deltaX);
        deltaY = checkPositionY(deltaY);
        mScroller.startScroll(scrollX, scrollY, deltaX, deltaY, 1000);
    }


}
