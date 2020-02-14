package net.muxi.huashiapp.ui.more;

import android.app.Dialog;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.CenterDialogFragment;


/**
 * Created by december on 17/2/23.
 */

public class FeedbackDialog extends CenterDialogFragment {

    private OnClickListener mOnClickListener;
    private android.widget.TextView mTitleSugg;
    private View mDivider;
    private android.widget.TextView mContentSugg;
    private Button mBtnExit;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_feedback, null);
        initView(view);

        Dialog dialog = createCenterDialog(view);
        mBtnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.OnClick();
                }
            }
        });
        return dialog;

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    private void initView(View view) {
        mTitleSugg = view.findViewById(R.id.title_sugg);
        mDivider = view.findViewById(R.id.divider);
        mContentSugg = view.findViewById(R.id.content_sugg);
        mBtnExit = view.findViewById(R.id.btn_exit);
    }


    public interface OnClickListener {
        void OnClick();
    }


}
