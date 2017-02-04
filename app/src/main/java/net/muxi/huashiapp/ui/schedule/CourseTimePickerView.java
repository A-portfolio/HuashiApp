package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.muxistudio.viewpractice.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/3.
 */

public class CourseTimePickerView extends LinearLayout {

    @BindView(R.id.np_weekday)
    NumberPicker mNpWeekday;
    @BindView(R.id.np_start)
    NumberPicker mNpStart;
    @BindView(R.id.np_end)
    NumberPicker mNpEnd;

    public static final int START_LINE_WIDTH = 8 * 3;
    public static final String[] WEEKDAYS = {"周一","周二","周三","周四","周五","周六","周日"};
    public static final String[] COURSETIME;

    public CourseTimePickerView(Context context) {
        this(context, null);
    }

    public CourseTimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_time_picker, this, true);
        ButterKnife.bind(this,this);
        setWillNotDraw(false);
        initView();
    }

    private void initView() {
        setDividerColor(mNpWeekday,Color.TRANSPARENT);
        setDividerColor(mNpStart,Color.TRANSPARENT);
        setDividerColor(mNpEnd,Color.TRANSPARENT);
        mNpWeekday.setMinValue(0);
        mNpWeekday.setMaxValue(6);
        mNpStart.setMinValue(0);
        mNpStart.setMaxValue(13);
        mNpEnd.setMinValue(0);
        mNpEnd.setMaxValue(13);
        mNpWeekday.setDisplayedValues(WEEKDAYS);

    }

    public int getWeekDay(){
        return mNpWeekday.getValue();
    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("course","context3");

        int width = getWidth();
        int height = getHeight();
        Path path = new Path();

        path.moveTo(0,0);
        path.lineTo(getWidth(),0);
        path.lineTo(getWidth(),getHeight());
        path.lineTo(0,getHeight());

        Paint p = new Paint();
        p.setAntiAlias(true);
//        p.setColor(getResources().getColor(R.color.divider));
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path,p);

        Path path1 = new Path();

        path1.moveTo(START_LINE_WIDTH / 2,0);
        path1.lineTo(START_LINE_WIDTH / 2,height / 3);
        path1.moveTo(START_LINE_WIDTH / 2,height * 2 / 3);
        path1.lineTo(START_LINE_WIDTH / 2,height);
        p.setStrokeWidth(START_LINE_WIDTH);
        canvas.drawPath(path1,p);

        Path path2 = new Path();
        path2.moveTo(START_LINE_WIDTH / 2,height /3);
        path2.lineTo(START_LINE_WIDTH / 2,height * 2 /3);
        p.setColor(getResources().getColor(R.color.colorAccent));
        canvas.drawPath(path2,p);
//
        Path path3 = new Path();
        path3.moveTo(START_LINE_WIDTH,height /3);
        path3.lineTo(width,height/3);
        path3.lineTo(width,height*2/3);
        path3.lineTo(START_LINE_WIDTH,height*2/3);
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        canvas.drawPath(path3,p);

    }
}
