package net.muxi.huashiapp.ui.more;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.CenterDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 17/2/27.
 */

public class CheckUpdateDialog extends CenterDialogFragment {

    @BindView(R.id.update_title)
    TextView mUpdateTitle;
    @BindView(R.id.update_content)
    TextView mUpdateContent;
    @BindView(R.id.btn_update)
    Button mBtnUpdate;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;


    private String mTitle;
    private String mContent;

    private String mUpdate;
    private String mCancel;

    private OnPositiveClickListener mPositiveClickListener;
    private OnNegativeClickListener mNegativeClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_check_update, null);
        ButterKnife.bind(this, view);

        Dialog dialog = createCenterDialog(view);

        initData();
        initEvent();
        return dialog;

    }

    private void initEvent(){
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPositiveClickListener != null){
                    mPositiveClickListener.OnPositiveClick();
                }
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNegativeClickListener != null){
                    mNegativeClickListener.OnNegativeClick();
                }
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



    public interface OnPositiveClickListener {
        public void OnPositiveClick();
    }

    public interface OnNegativeClickListener {
        public void OnNegativeClick();
    }

}
