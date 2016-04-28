package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TableLayout;
import android.widget.TableRow;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.DimensUtil;

/**
 * Created by ybao on 16/4/19.
 */
public class TimeTable extends FrameLayout {

    public static final int FIXED_WIDTH = DimensUtil.getScreenWidth();


    private Scroller mScroller;
    private View view;
    private Context mUpScrollerView;

    public TimeTable(Context context) {
        this(context,null);
    }

    public TimeTable(Context context, AttributeSet attrs) {
        super(context,attrs);

        mScroller = new Scroller(context);
        initLayout(context);
    }

    public void initLayout(Context context) {
        setupScrollerView(context);
        setupWeekDayLayout(context);
        setupCourseTimeLayout(context);

        setLittleView(context);

    }

    public void setLittleView(Context context) {
        view  = new View(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(48,32);
        params.height = DimensUtil.getScreenWidth() / 10;
        params.width = DimensUtil.getScreenHeight() / 10;
        view.invalidate();
    }

    public void setupCourseTimeLayout(Context context) {

    }


    public void setupWeekDayLayout(Context context) {

    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setupScrollerView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_time_table,null);
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.schedule_scroll_view);
        TableLayout tableLayout = (TableLayout)view.findViewById(R.id.view_schedule_table_layout);
        ViewGroup.LayoutParams tableLayoutParams = tableLayout.getLayoutParams();
        ViewGroup.LayoutParams tableRowParams = new ViewGroup.LayoutParams(tableLayoutParams.width,tableLayoutParams.height / 7);
        ViewGroup.LayoutParams tvParams = new ViewGroup.LayoutParams(tableLayoutParams.width / 7,tableLayoutParams.height / 7);
        TableRow[] tableRows = new TableRow[7];
        for (int i = 0; i < 7; i ++){
            tableRows[i].setLayoutParams(tableLayoutParams);
        }

    }
}
