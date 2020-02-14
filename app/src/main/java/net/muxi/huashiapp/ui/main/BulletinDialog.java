package net.muxi.huashiapp.ui.main;

import android.app.Dialog;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.CenterDialogFragment;


/**
 * Created by kolibreath on 18-2-13.
 */

public class BulletinDialog extends CenterDialogFragment {

    private android.widget.RelativeLayout mRlVerify;
    private TextView mTvTitleBulletin;
    private TextView mTvMsgBulletin;
    private TextView mBtnConfirmBulletin;

    void submit() {
        dismiss();
    }

    public static BulletinDialog newInstance() {
        return new BulletinDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_main_bulletin, null);
        initView(view);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(R.drawable.bg_center_dialog);
        return dialog;
    }

    private void initView(View view) {
        mRlVerify = view.findViewById(R.id.rl_verify);
        mTvTitleBulletin = view.findViewById(R.id.tv_title_bulletin);
        mTvMsgBulletin = view.findViewById(R.id.tv_msg_bulletin);
        mBtnConfirmBulletin = view.findViewById(R.id.btn_confirm_bulletin);
        mBtnConfirmBulletin.setOnClickListener(v -> submit());
    }
}