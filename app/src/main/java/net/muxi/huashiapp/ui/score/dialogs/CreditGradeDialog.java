package net.muxi.huashiapp.ui.score.dialogs;

import android.app.Dialog;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.muxistudio.common.util.Logger;
import java.util.Locale;
import net.muxi.huashiapp.R;

public class CreditGradeDialog extends DialogFragment implements View.OnClickListener{

  private TextView mTvCreditGrade;
  private TextView mBtnOk;

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
    initView(view);
    float result = getArguments().getFloat("result",0);
    mTvCreditGrade.setText(String.format(Locale.CHINESE,"%.4f",result));
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
    int id = view.getId();
    if (id == R.id.btn_ok){
      Logger.d("dialog dismiss");
      CreditGradeDialog.this.dismiss();
    }
  }

  private void initView(View view) {
    mTvCreditGrade = view.findViewById(R.id.tv_credit_grade);
    mBtnOk = view.findViewById(R.id.btn_ok);
    mBtnOk.setOnClickListener(v -> onClick(view));
  }
}