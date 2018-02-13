package net.muxi.huashiapp.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.CenterDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kolibreath on 18-2-13.
 */

public class BulletinDialog extends CenterDialogFragment {

    @BindView(R.id.tv_title_bulletin)
    TextView mTvTitleBulletin;
    @BindView(R.id.tv_msg_bulletin)
    TextView mTvMsgBulletin;
    @BindView(R.id.btn_confirm_bulletin)
    TextView mBtnConfirmBulletin;
    @OnClick(R.id.btn_confirm_bulletin)
    void submit(){
      dismiss();
    }

    public static BulletinDialog newInstance(){
        return new BulletinDialog();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_main_bulletin, null);
        ButterKnife.bind(this,view);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height  = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(R.drawable.bg_center_dialog);
        return dialog;
    }
}