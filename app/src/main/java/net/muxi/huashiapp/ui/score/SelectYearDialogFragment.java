package net.muxi.huashiapp.ui.score;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.muxi.huashiapp.ui.schedule.LargeSizeNumberPicker;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.UserUtil;
import net.muxi.huashiapp.widget.BottomDialogFragment;
import net.muxi.huashiapp.widget.BottomPickerDialogFragment;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/3/1
 */

public class SelectYearDialogFragment extends BottomPickerDialogFragment{

    public int value;
    private String[] years = UserUtil.generateHyphenYears(4);

    public static SelectYearDialogFragment newInstance(int value) {
        Bundle args = new Bundle();
        args.putInt("picker_value",value);
        SelectYearDialogFragment fragment = new SelectYearDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        value = getArguments().getInt("picker_value");
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        setTitle(String.format("%s学年",years[value]));
        final LargeSizeNumberPicker numberpicker = new LargeSizeNumberPicker(getContext());
        numberpicker.setValue(value);
        numberpicker.setMinValue(0);
        numberpicker.setMaxValue(3);
        numberpicker.setDisplayedValues(years);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        setContentView(numberpicker,params);
        numberpicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            value = i1;
            setTitle(String.format("%s学年",years[value]));
        });
        return dialog;
    }

    public int getValue(){
        return value;
    }
}
