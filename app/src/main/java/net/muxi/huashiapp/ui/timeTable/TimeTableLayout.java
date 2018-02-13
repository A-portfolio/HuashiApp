package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/27
 */

public class TimeTableLayout extends RelativeLayout{

    private Scroller mScroller;

    public TimeTableLayout(Context context) {
        this(context,null);
    }

    public TimeTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            scrollBy(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void smoothScrollTo(int x,int y){
        mScroller.startScroll(mScroller.getCurrX(),mScroller.getCurrY(),x,y);
    }
}
