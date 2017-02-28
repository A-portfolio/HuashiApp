package net.muxi.huashiapp.ui.more;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.CenterDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 17/2/23.
 */

public class FeedbackDialog extends CenterDialogFragment {


    @BindView(R.id.btn_exit)
    Button mBtnExit;

    private OnClickListener mOnClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_feedback, null);
        ButterKnife.bind(this, view);

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


    public interface OnClickListener {
        public void OnClick();
    }


}
