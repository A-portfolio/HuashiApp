package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import net.muxi.huashiapp.R;


/**
 * Created by ybao on 17/1/26.
 */

public class CourseTimeLayout extends ScheduleTimeLayout{
    private Context mContext;
    private Scroller mScroller;
    //侧边栏的表格
    //竖着的表格
    private View[] views;

    public CourseTimeLayout(Context context) {
        this(context,null);
    }

    public CourseTimeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScroller = new Scroller(context);
        this.setOrientation(VERTICAL);
        initView();
        setWillNotDraw(false);
    }

    //给竖着的每一个日期的方格设置序号和逢双数设置时间
    private void initView() {
        views = new View[14];
        for (int i = 0;i < 14;i ++){
            views[i] = LayoutInflater.from(mContext).inflate(R.layout.view_course_time,this,false);
            ((TextView)views[i].findViewById(R.id.tv_order)).setText((i + 1) + "");
            if (i % 2 == 0){
                ((TextView)views[i].findViewById(R.id.tv_time)).setText((8 + i) + ":00");
            }
            addView(views[i]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        canvas.drawRect(0,0,getWidth(),TimeTable.COURSE_TIME_HEIGHT * 14,p);
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
