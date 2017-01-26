package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.muxi.huashiapp.R;

/**
 * Created by ybao on 17/1/26.
 */

public class CourseTimeLayout extends ScheduleTimeLayout{

    private Context mContext;

    private View[] views;

    public CourseTimeLayout(Context context) {
        this(context,null);
    }

    public CourseTimeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        views = new View[14];
        for (int i = 0;i < 14;i ++){
            views[i] = LayoutInflater.from(mContext).inflate(R.layout.view_course_time,this,false);
            ((TextView)views[i].findViewById(R.id.tv_order)).setText(i + "");
            if (i % 2 == 0){
                ((TextView)views[i].findViewById(R.id.tv_time)).setText((8 + i) + ":00");
            }
        }
    }

}
