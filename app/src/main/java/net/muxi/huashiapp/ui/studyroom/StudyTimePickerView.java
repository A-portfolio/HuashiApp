package net.muxi.huashiapp.ui.studyroom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.NumberPickerHelper;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 17/2/9.
 */

public class StudyTimePickerView extends LinearLayout {


    public static final String[] DAYS = {"周一", "周二", "周三", "周四", "周五"};
    public String[] STUDY_TIME = new String[20];

    @BindView(R.id.np_study_week)
    NumberPicker mNpStudyWeek;
    @BindView(R.id.np_study_day)
    NumberPicker mNpStudyDay;

    private OnValueChangeListener mValueChangeListener;

    public StudyTimePickerView(Context context) {
        this(context, null);
    }

    public StudyTimePickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflate(context,R.layout.view_study_time_picker, this);
        ButterKnife.bind(this);

        setWillNotDraw(false);
        initView();

    }


    private void initView() {
        setDividerColor(mNpStudyWeek, Color.TRANSPARENT);
        setDividerColor(mNpStudyDay, Color.TRANSPARENT);
        mNpStudyWeek.setMinValue(0);
        mNpStudyWeek.setMaxValue(19);
        mNpStudyDay.setMinValue(0);
        mNpStudyDay.setMaxValue(4);
        mNpStudyDay.setDisplayedValues(DAYS);
        for (int i = 1; i < 21; i++) {
            STUDY_TIME[i - 1] = String.valueOf(i);
        }
        mNpStudyWeek.setDisplayedValues(STUDY_TIME);

        mNpStudyWeek.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mValueChangeListener.onValueChange(newVal, mNpStudyDay.getValue());
            }
        });
        mNpStudyDay.setOnValueChangedListener(((picker, oldVal, newVal) -> {
            mValueChangeListener.onValueChange(mNpStudyWeek.getValue(), newVal);
        }));

    }

    public int getWeek() {
        return mNpStudyWeek.getValue();
    }

    public int getDay() {
        return mNpStudyDay.getValue();
    }

    public void setWeek(int weeks) {
        mNpStudyWeek.setValue(weeks);
    }

    public void setDay(int days) {
        mNpStudyDay.setValue(days);
    }


    private void setDividerColor(NumberPicker picker, int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        NumberPickerHelper.drawVerticalPickerBg(canvas, getWidth(), getHeight());
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        mValueChangeListener = onValueChangeListener;
    }

    public interface OnValueChangeListener {
        void onValueChange(int week, int day);
    }
}
