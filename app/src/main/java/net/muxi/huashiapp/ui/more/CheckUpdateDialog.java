package net.muxi.huashiapp.ui.more;

import android.app.Dialog;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.CenterDialogFragment;


/**
 * Created by december on 17/2/27.
 */

public class CheckUpdateDialog extends CenterDialogFragment {

    private String mTitle;
    private String mContent;

    private String mUpdate;
    private String mCancel;

    private OnPositiveClickListener mPositiveClickListener;
    private OnNegativeClickListener mNegativeClickListener;
    private TextView mUpdateTitle;
    private TextView mUpdateContent;
    private Button mBtnUpdate;
    private Button mBtnCancel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_check_update, null);
        initView(view);

        Dialog dialog = createCenterDialog(view);

        initData();
        initEvent();
        return dialog;

    }

    private void initEvent(){
        mBtnUpdate.setOnClickListener(v -> {
            if (mPositiveClickListener != null){
                mPositiveClickListener.OnPositiveClick();
            }
        });

        mBtnCancel.setOnClickListener(v -> {
            if (mNegativeClickListener != null){
                mNegativeClickListener.OnNegativeClick();
            }
        });
    }

    private void initData() {
        if (mTitle != null) {
            mUpdateTitle.setText(mTitle);
        }
        if (mContent != null) {
            mUpdateContent.setText(mContent);
        }
        if (mUpdate != null) {
            mBtnUpdate.setText(mUpdate);
        }
        if (mCancel != null) {
            mBtnCancel.setText(mCancel);
        }
    }

    public void setOnPositiveButton(String str,OnPositiveClickListener onPositiveClickListener){
        if (str != null){
            mUpdate = str;
        }
        mPositiveClickListener = onPositiveClickListener;
    }

    public void setOnNegativeButton(String str,OnNegativeClickListener onNegativeClickListener){
        if (str != null){
            mCancel = str;
        }
        mNegativeClickListener = onNegativeClickListener;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setContent(String content) {
        mContent = content;
    }

    private void initView(View view) {
        mUpdateTitle = view.findViewById(R.id.update_title);
        mUpdateContent = view.findViewById(R.id.update_content);
        mBtnUpdate = view.findViewById(R.id.btn_update);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
    }


    public interface OnPositiveClickListener {
        void OnPositiveClick();
    }

    public interface OnNegativeClickListener {
        void OnNegativeClick();
    }

}
