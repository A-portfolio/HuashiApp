package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.muxistudio.common.util.DimensUtil;
import com.muxistudio.common.util.PreferenceUtil;

import net.muxi.huashiapp.R;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/24
 * 2019-8-23 废弃
 */

//点击右上角的加号，添加新课程
public class AddNewCourseView extends RelativeLayout {

    //    public
    public static final int MAX_CLICK_DISTANCE = DimensUtil.dp2px(15);
    public static final int MAX_CLICK_DURATION = 500;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private long lastTime;

    public AddNewCourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        if (PreferenceUtil.getBoolean(PreferenceUtil.IS_FIRST_ENTER_TABLE, true)) {
            this.setVisibility(VISIBLE);
            //会自动添加到布局之中去
            LayoutInflater.from(context).inflate(R.layout.view_table_guide, this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                lastTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();
                if (System.currentTimeMillis() - lastTime < MAX_CLICK_DURATION &&
                        (endX - startX) * (endX - startX) + (endY - startY) * (endY - startY) < MAX_CLICK_DISTANCE * MAX_CLICK_DURATION){
                    CurweekSetActivity.start(getContext());
                }
                break;
        }
        return true;
    }
}
