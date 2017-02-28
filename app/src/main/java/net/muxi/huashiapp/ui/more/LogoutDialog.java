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
 * Created by december on 17/2/26.
 */

public class LogoutDialog extends CenterDialogFragment {

    @BindView(R.id.btn_id_logout)
    Button mBtnIdLogout;
    @BindView(R.id.btn_library_logout)
    Button mBtnLibraryLogout;
    @BindView(R.id.btn_all_logout)
    Button mBtnAllLogout;


    private OnIdClickListener mIdClickListener;
    private OnLibraryClickListener mLibraryClickListener;
    private OnAllClickListener mAllClickListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_log_out, null);
        ButterKnife.bind(this, view);

        Dialog dialog = createCenterDialog(view);

        initEvent();
        return dialog;
    }

    private void initEvent() {
        mBtnIdLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIdClickListener != null) {
                    mIdClickListener.OnIdClick();
                }
            }
        });

        mBtnLibraryLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLibraryClickListener != null) {
                    mLibraryClickListener.OnLibraryClick();
                }
            }
        });

        mBtnAllLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAllClickListener != null) {
                    mAllClickListener.OnAllClick();
                }
            }
        });
    }


    public void setBtnIdLogout(OnIdClickListener onIdClickListener) {
        mIdClickListener = onIdClickListener;
    }

    public void setBtnLibraryLogout(OnLibraryClickListener onLibraryClickListener) {
        mLibraryClickListener = onLibraryClickListener;
    }

    public void setBtnAllLogout(OnAllClickListener onAllClickListener) {
        mAllClickListener = onAllClickListener;
    }

    public interface OnIdClickListener {
        public void OnIdClick();
    }

    public interface OnLibraryClickListener {
        public void OnLibraryClick();
    }

    public interface OnAllClickListener {
        public void OnAllClick();
    }
}

