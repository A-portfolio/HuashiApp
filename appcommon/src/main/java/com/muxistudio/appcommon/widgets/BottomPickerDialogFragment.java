package com.muxistudio.appcommon.widgets;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxistudio.appcommon.R;


/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/3/1
 */

public class BottomPickerDialogFragment extends BottomDialogFragment {

    TextView mTvTitle;
    PickerLayout mPickerLayout;
    Button mBtnCancel;
    Button mBtnEnter;

    private View.OnClickListener mOnPositiveButtonClickListener;
    private View.OnClickListener mOnNegativeButtonClickListener;

    public BottomPickerDialogFragment() {
        super();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_picker_dialog, null);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mPickerLayout = (PickerLayout) view.findViewById(R.id.picker_layout);
        mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mBtnEnter = (Button) view.findViewById(R.id.btn_enter);
        initView();
        Dialog dialog = createBottomDialog(view);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initView() {
        mBtnCancel.setOnClickListener(v -> {
            dismiss();
            if (mOnNegativeButtonClickListener != null) {
                mOnNegativeButtonClickListener.onClick(v);
            }
        });
        mBtnEnter.setOnClickListener(v -> {
            dismiss();
            if (mOnPositiveButtonClickListener != null) {
                mOnPositiveButtonClickListener.onClick(v);
            }
        });
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setOnPositionButtonClickListener(View.OnClickListener listener) {
        mOnPositiveButtonClickListener = listener;
    }

    public void setOnNegativeButtonClickListener(View.OnClickListener listener) {
        mOnNegativeButtonClickListener = listener;
    }

    public void setContentView(View view) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPickerLayout.removeAllViews();
        mPickerLayout.addView(view, params);
    }

    public void setContentView(View view, RelativeLayout.LayoutParams params) {
        mPickerLayout.removeAllViews();
        mPickerLayout.addView(view, params);
    }

}
