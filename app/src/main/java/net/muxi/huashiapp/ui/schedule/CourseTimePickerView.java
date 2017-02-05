package net.muxi.huashiapp.ui.schedule;

import android.app.Dialog;
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


import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.NumberPickerBgHelper;

import java.util.Arrays;

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

    private OnValueChangeListener mOnValueChangeListener;

    public static final int START_LINE_WIDTH = 8 * 3;
    public static final String[] WEEKDAYS = {"周一","周二","周三","周四","周五","周六","周日"};
    public String[] COURSE_TIME;

    public CourseTimePickerView(Context context) {
        this(context, null);
    }

    public CourseTimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_time_picker, this, true);
        ButterKnife.bind(this,this);
        //invoke onDraw
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
        for (int i = 1;i < 15;i ++) {
            COURSE_TIME[i - 1] = String.valueOf(i);
        }
        mNpStart.setDisplayedValues(COURSE_TIME);
        mNpEnd.setDisplayedValues(COURSE_TIME);

        mNpWeekday.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                mOnValueChangeListener.onValueChange(i1,mNpStart.getValue(),mNpEnd.getValue());
            }
        });
        mNpStart.setOnValueChangedListener((numberPicker, i, i1) -> {
            mOnValueChangeListener.onValueChange(mNpWeekday.getValue(),i1,mNpEnd.getValue());
        });
        mNpEnd.setOnValueChangedListener((numberPicker, i, i1) -> {
            mOnValueChangeListener.onValueChange(mNpWeekday.getValue(),mNpStart.getValue(),i1);
        });
    }

    public int getWeekday(){
        return mNpWeekday.getValue();
    }

    public int getStartTime(){
        return mNpStart.getValue() + 1;
    }

    public int getEndTime(){
        return mNpEnd.getValue() + 1;
    }

    public void setWeekday(int weekday){
        mNpWeekday.setValue(weekday);
    }

    public void setStartTime(int startTime){
        mNpStart.setValue(startTime);
    }

    public void setEndTime(int endTime){
        mNpEnd.setValue(endTime);
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
        NumberPickerBgHelper.drawVerticalPickerBg(canvas,getWidth(),getHeight());
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener){
        mOnValueChangeListener = onValueChangeListener;
    }

    public interface OnValueChangeListener{
        void onValueChange(int weekday,int start,int end);
    }

}
