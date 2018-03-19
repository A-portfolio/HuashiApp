package net.muxi.huashiapp.ui.credit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.muxistudio.appcommon.utils.NumberPickerHelper;
import com.muxistudio.appcommon.utils.UserUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.timeTable.LargeSizeNumberPicker;

import java.util.Arrays;


/**
 * Created by ybao on 17/2/9.
 */

public class CreditYearSelectView extends RelativeLayout {

    private String[] years = UserUtil.generateYears(6);

    private OnValueChangeListener mOnValueChangeListener;
    private LargeSizeNumberPicker mNpStartYear;
    private LargeSizeNumberPicker mNpEndYear;

    public CreditYearSelectView(Context context) {
        super(context);
    }

    public CreditYearSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_credit_year_select, this);
        initView();
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
            if (i1 > mNpEndYear.getValue() - 1) {
                mNpEndYear.setValue(i1 + 1);
            }
            if (mOnValueChangeListener != null) {
                mOnValueChangeListener.onValueChange(UserUtil.generateYears(6)[i1], UserUtil.generateYears(6)[mNpEndYear.getValue()]);
            }
        });
        mNpEndYear.setOnValueChangedListener((numberPicker, i, i1) -> {
            if (i1 <= mNpStartYear.getValue()) {
                mNpEndYear.setValue(mNpStartYear.getValue() + 1);
            }
            if (mOnValueChangeListener != null) {
                mOnValueChangeListener.onValueChange(UserUtil.generateYears(6)[mNpStartYear.getValue()], UserUtil.generateYears(6)[i1]);
            }
        });
    }

    public void setNpStartYear(String start) {
        mNpStartYear.setValue(Arrays.binarySearch(years, start));
    }

    public void setNpEndYear(String end) {
        mNpEndYear.setValue(Arrays.binarySearch(years, end));
    }

    public String getNpStartYear() {
        return years[mNpStartYear.getValue()];
    }

    public String getNpEndYear() {
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

    private void initView() {
        mNpStartYear = findViewById(R.id.start_year);
        mNpEndYear = findViewById(R.id.end_year);
    }

    public interface OnValueChangeListener {
        void onValueChange(String start, String end);
    }
}
