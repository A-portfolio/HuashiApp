package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.NumberPickerHelper;

/**
 * Created by ybao on 17/2/9.
 * 18字号的numberpicker
 */

public class LargeSizeNumberPicker extends NumberPicker{

    public LargeSizeNumberPicker(Context context) {
        this(context,null);
    }

    public LargeSizeNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        setWrapSelectorWheel(false);
        NumberPickerHelper.setDividerColor(this,Color.TRANSPARENT);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    private void updateView(View view) {
        if(view instanceof EditText){
            ((EditText) view).setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            ((EditText) view).setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }
}
