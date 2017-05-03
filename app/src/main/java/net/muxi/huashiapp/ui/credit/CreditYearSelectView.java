package net.muxi.huashiapp.ui.credit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.schedule.LargeSizeNumberPicker;
import net.muxi.huashiapp.util.NumberPickerHelper;
import net.muxi.huashiapp.util.UserUtil;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/9.
 */

public class CreditYearSelectView extends RelativeLayout {

    @BindView(R.id.start_year)
    LargeSizeNumberPicker mNpStartYear;
    @BindView(R.id.end_year)
    LargeSizeNumberPicker mNpEndYear;

    private String[] years = UserUtil.generateYears(6);

    private OnValueChangeListener mOnValueChangeListener;

    public CreditYearSelectView(Context context) {
        super(context);
    }

    public CreditYearSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_credit_year_select, this);
        ButterKnife.bind(this);
        setWillNotDraw(false);
        mNpStartYear.setMinValue(0);
        mNpStartYear.setMaxValue(5);
        mNpEndYear.setMinValue(0);
        mNpEndYear.setMaxValue(5);
        mNpStartYear.setDisplayedValues(UserUtil.generateYears(6));
        mNpEndYear.setDisplayedValues(UserUtil.generateYears(6));
        NumberPickerHelper.setDividerColor(mNpStartYear, Color.TRANSPARENT);
        NumberPickerHelper.setDividerColor(mNpEndYear, Color.TRANSPARENT);
        mNpStartYear.setOnValueChangedListener((numberPicker, i, i1) -> {
            if (i1 > mNpEndYear.getValue() - 1){
                mNpEndYear.setValue(i1 + 1);
            }
            if (mOnValueChangeListener != null){
                mOnValueChangeListener.onValueChange(UserUtil.generateYears(6)[i1],UserUtil.generateYears(6)[mNpEndYear.getValue()]);
            }
        });
        mNpEndYear.setOnValueChangedListener((numberPicker, i, i1) -> {
            if (i1 <= mNpStartYear.getValue()){
                mNpEndYear.setValue(mNpStartYear.getValue() + 1);
            }
            if (mOnValueChangeListener != null){
                mOnValueChangeListener.onValueChange(UserUtil.generateYears(6)[mNpStartYear.getValue()],UserUtil.generateYears(6)[i1]);
            }
        });
    }

    public void setStartYear(String start){
        mNpStartYear.setValue(Arrays.binarySearch(years,start));
    }

    public void setEndYear(String end){
        mNpEndYear.setValue(Arrays.binarySearch(years,end));
    }

    public String getStartYear(){
        return years[mNpStartYear.getValue()];
    }

    public String getEndYear(){
        return years[mNpEndYear.getValue()];
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
        void onValueChange(String start, String end);
    }
}
