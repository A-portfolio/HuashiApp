package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by ybao on 16/5/22.
 */
public class ContentLayout extends RelativeLayout{

    private Scroller contentScroller;

    public ContentLayout(Context context) {
        this(context,null);
    }

    public ContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        contentScroller = new Scroller(context);
    }


    @Override
    public void computeScroll() {
        if (contentScroller.computeScrollOffset()){
            this.scrollTo(contentScroller.getCurrX(),contentScroller.getCurrY());
            postInvalidate();
        }
    }


    public void smoothScrollTo(int y){
        int curY = getScrollY();
        int deltaY = y - curY;
        contentScroller.startScroll(0,curY,0,y,250);
        invalidate();
    }


}
