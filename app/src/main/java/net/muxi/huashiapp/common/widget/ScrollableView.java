package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

import net.muxi.huashiapp.common.util.DimensUtil;

/**
 * Created by ybao on 16/8/15.
 */
public class ScrollableView extends LinearLayout{

    private Scroller mScroller;

    public ScrollableView(Context context) {
        this(context,null);
    }

    public ScrollableView(Context context, AttributeSet attrs) {
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


    public void scrollBy(int y,int imgHeight){
        y = checkY(y,imgHeight);
        scrollBy(0,y);
    }

    private int checkY(int y, int imgHeight) {
        if (y + getScrollY() < 0 ){
            y = - getScrollY();
        }else if (y + getScrollY() > imgHeight - DimensUtil.getScreenHeight() + DimensUtil.getActionbarHeight() +
                DimensUtil.getStatusBarHeight()){
            y = imgHeight - DimensUtil.getScreenHeight() + DimensUtil.getActionbarHeight() + DimensUtil.getStatusBarHeight();
        }
        return y;
    }

}
