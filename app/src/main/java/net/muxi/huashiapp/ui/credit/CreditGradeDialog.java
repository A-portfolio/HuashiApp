package net.muxi.huashiapp.ui.credit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/11.
 */

public class CreditGradeDialog extends DialogFragment implements View.OnClickListener{

    @BindView(R.id.tv_credit_grade)
    TextView mTvCreditGrade;
    @BindView(R.id.btn_ok)
    TextView mBtnOk;

    public static CreditGradeDialog newInstance(float result) {
        Bundle args = new Bundle();
        args.putFloat("result", result);
        CreditGradeDialog fragment = new CreditGradeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_grade_result, null);
        ButterKnife.bind(this,view);
        float result = getArguments().getFloat("result",0);
        mTvCreditGrade.setText(String.format("%.2f",result + 0.005));
        mBtnOk.setOnClickListener(this);

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wmlp);
        window.setBackgroundDrawableResource(R.drawable.bg_center_dialog);
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ok:{
                Logger.d("dialog dismiss");
                CreditGradeDialog.this.dismiss();
            }
        }
    }
}
