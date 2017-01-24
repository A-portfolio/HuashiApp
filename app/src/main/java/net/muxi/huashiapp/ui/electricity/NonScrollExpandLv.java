package net.muxi.huashiapp.ui.electricity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * Created by ybao on 16/8/3.
 * 不能滑动的expandablelistview,防止滑动冲突
 */
public class NonScrollExpandLv extends ExpandableListView {

    public NonScrollExpandLv(Context context) {
        super(context);
    }

    public NonScrollExpandLv(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonScrollExpandLv(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }

}
