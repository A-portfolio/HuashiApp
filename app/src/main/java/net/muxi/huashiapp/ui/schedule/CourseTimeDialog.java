package net.muxi.huashiapp.ui.schedule;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;


import net.muxi.huashiapp.R;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/28.
 */
public class CourseTimeDialog extends Dialog implements NumberPicker.OnValueChangeListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.num_picker_weekday)
    NumberPicker mNumPickerWeekday;
    @BindView(R.id.num_picker_start)
    NumberPicker mNumPickerStart;
    @BindView(R.id.num_picker_end)
    NumberPicker mNumPickerEnd;
    @BindView(R.id.btn_negative)
    Button mBtnNegative;
    @BindView(R.id.btn_positive)
    Button mBtnPositive;

    //星期
    private int mWeekday;
    //开始的节次
    private int mStartTime;
    //结束的节次
    private int mEndTime;

    private Context mContext;

    private String[] weeks = new String[]{
            "星期一",
            "星期二",
            "星期三",
            "星期四",
            "星期五",
            "星期六",
            "星期日"
    };
    private String[] startTimes = new String[14];
    private String[] endTimes = new String[14];

    public static final String WEEKDAY = "weekday";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";

    private NoticeDialogListener mNoticeDialogListener;


    public interface NoticeDialogListener {
        void onDialogPositiveClick(String weekday, int startTime, int endTime);

        void onDialogNegativeClick();
    }

    public CourseTimeDialog(Context context, int weekday, int startTime, int endTime) {
        super(context,R.style.DialogStyle);
        mContext = context;
        mWeekday = weekday;
        mStartTime = startTime;
        mEndTime = endTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_course);
        ButterKnife.bind(this);
        setupNumberPicker();
        mBtnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoticeDialogListener.onDialogNegativeClick();
                CourseTimeDialog.this.dismiss();
            }
        });
        mBtnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoticeDialogListener.onDialogPositiveClick(getWeekday(mNumPickerWeekday.getValue()),
                        mNumPickerStart.getValue(),
                        mNumPickerEnd.getValue());
                CourseTimeDialog.this.dismiss();
            }
        });
        setDividerColor(mNumPickerWeekday, mContext.getResources().getColor(R.color.colorPrimary));
        setDividerColor(mNumPickerStart,mContext.getResources().getColor(R.color.colorPrimary));
        setDividerColor(mNumPickerEnd, mContext.getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        int pickerId = picker.getId();
        switch (pickerId) {
            case R.id.num_picker_start:
                if (newVal > mNumPickerEnd.getValue()) {
                    mNumPickerEnd.setValue(newVal);
                }
                break;
            case R.id.num_picker_end:
                if (newVal < mNumPickerStart.getValue()) {
                    mNumPickerEnd.setValue(oldVal);
                }
                break;
        }
    }


    public void setNoticeDialogListener(NoticeDialogListener listener){
        mNoticeDialogListener = listener;
    }

    //初始化numberpicker
    private void setupNumberPicker() {
        mNumPickerWeekday.setMinValue(0);
        mNumPickerWeekday.setMaxValue(6);
        mNumPickerWeekday.setDisplayedValues(weeks);
        mNumPickerWeekday.setWrapSelectorWheel(false);
        mNumPickerWeekday.setValue(mWeekday);

        for (int i = 0; i < 14; i++) {
            startTimes[i] = "第" + (i + 1) + "节";
            endTimes[i] = "第" + (i + 1) + "节";
        }
        mNumPickerStart.setMinValue(0);
        mNumPickerStart.setMaxValue(13);
        mNumPickerStart.setDisplayedValues(startTimes);
        mNumPickerStart.setWrapSelectorWheel(false);
        mNumPickerStart.setValue(mStartTime);

        mNumPickerEnd.setMinValue(0);
        mNumPickerEnd.setMaxValue(13);
        mNumPickerEnd.setDisplayedValues(endTimes);
        mNumPickerEnd.setWrapSelectorWheel(false);
        mNumPickerEnd.setValue(mEndTime);

        mNumPickerStart.setOnValueChangedListener(this);
        mNumPickerEnd.setOnValueChangedListener(this);
    }

    //获取星期的String形式
    private String getWeekday(int weekdayValue) {
        return weeks[weekdayValue];
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


}
