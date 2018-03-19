package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import com.muxistudio.appcommon.utils.NumberPickerHelper;

import net.muxi.huashiapp.R;


/**
 * Created by ybao on 17/2/3.
 */


//用于添加课程中选择的的课程表
public class CourseTimePickerView extends RelativeLayout {

    private OnValueChangeListener mOnValueChangeListener;

    public static final int START_LINE_WIDTH = 8 * 3;
    public static final String[] WEEKDAYS = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    public String[] COURSE_TIME = new String[14];
    private LargeSizeNumberPicker mNpWeekday;
    private LargeSizeNumberPicker mNpStart;
    private LargeSizeNumberPicker mNpEnd;

    public CourseTimePickerView(Context context) {
        this(context, null);
    }

    public CourseTimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_time_picker, this);
        //invoke onDraw!!!
        setWillNotDraw(false);
        initView();
    }

    private void initView() {
        mNpWeekday = findViewById(R.id.np_weekday);
        mNpStart = findViewById(R.id.np_start);
        mNpEnd = findViewById(R.id.np_end);
        NumberPickerHelper.setDividerColor(mNpWeekday, Color.TRANSPARENT);
        NumberPickerHelper.setDividerColor(mNpStart, Color.TRANSPARENT);
        NumberPickerHelper.setDividerColor(mNpEnd, Color.TRANSPARENT);
        mNpWeekday.setMinValue(0);
        mNpWeekday.setMaxValue(6);
        mNpStart.setMinValue(0);
        mNpStart.setMaxValue(13);
        mNpEnd.setMinValue(0);
        mNpEnd.setMaxValue(13);
        mNpWeekday.setDisplayedValues(WEEKDAYS);
        for (int i = 1; i < 15; i++) {
            COURSE_TIME[i - 1] = String.valueOf(i);
        }
        mNpStart.setDisplayedValues(COURSE_TIME);
        mNpEnd.setDisplayedValues(COURSE_TIME);

        mNpWeekday.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                mOnValueChangeListener.onValueChange(i1, mNpStart.getValue(), mNpEnd.getValue());
            }
        });
        mNpStart.setOnValueChangedListener((numberPicker, i, i1) -> {
            if (i1 > mNpEnd.getValue()) {
                mNpEnd.setValue(i1);
            }
            mOnValueChangeListener.onValueChange(mNpWeekday.getValue(), i1, mNpEnd.getValue());
        });
        mNpEnd.setOnValueChangedListener((numberPicker, i, i1) -> {
            if (i1 < mNpStart.getValue()) {
                i1 = mNpStart.getValue();
                mNpEnd.setValue(i1);
            }
            mOnValueChangeListener.onValueChange(mNpWeekday.getValue(), mNpStart.getValue(), i1);
        });

    }

    public int getWeekday() {
        return mNpWeekday.getValue();
    }

    public int getStartTime() {
        return mNpStart.getValue() + 1;
    }

    public int getEndTime() {
        return mNpEnd.getValue() + 1;
    }

    public void setWeekday(int weekday) {
        mNpWeekday.setValue(weekday);
    }

    public void setStartTime(int startTime) {
        mNpStart.setValue(startTime);
    }

    public void setEndTime(int endTime) {
        mNpEnd.setValue(endTime);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        NumberPickerHelper.drawVerticalPickerBg(canvas, getWidth(), getHeight());
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        mOnValueChangeListener = onValueChangeListener;
    }

    public interface OnValueChangeListener {
        void onValueChange(int weekday, int start, int end);
    }


}
