package net.muxi.huashiapp.ui.score;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.muxistudio.appcommon.widgets.BottomDialogFragment;

import net.muxi.huashiapp.R;

public class SelectTermDialog extends BottomDialogFragment implements View.OnClickListener {


    private android.widget.TextView tvTermAll;
    private android.widget.CheckBox rbAll;
    private android.widget.TextView tvFirstTerm;
    private android.widget.CheckBox cbFirstTerm;
    private android.widget.TextView tvSecondTerm;
    private android.widget.CheckBox cbSecondTerm;
    private android.widget.TextView tvThirdTerm;
    private android.widget.CheckBox cbThirdTerm;
    private android.widget.TextView btnConfirm;
    private android.widget.TextView btnCancel;

    private OnPositiveClickListener mListener;

    private boolean mSelectAll = true;

    //实例化的时候自动初始化为true 使用init函数
    private boolean terms[] = new boolean[3];

    public static SelectTermDialog newInstance(){
        return new SelectTermDialog();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.rb_all) {
            if (!mSelectAll) {
                for (int i = 0; i < terms.length; i++) {
                    terms[i] = true;
                }

                cbFirstTerm.setChecked(true);
                cbSecondTerm.setChecked(true);
                cbThirdTerm.setChecked(true);

                rbAll.setChecked(false);
                mSelectAll = true;
            }else{
                for (int i = 0; i < terms.length; i++) {
                    terms[i] = false;
                }

                cbFirstTerm.setChecked(false);
                cbSecondTerm.setChecked(false);
                cbThirdTerm.setChecked(false);

                rbAll.setChecked(false);
                mSelectAll = false;
            }
        }

        if(id == R.id.cb_first_term)
            terms[0] = cbFirstTerm.isChecked();

        if(id == R.id.cb_second_term)
            terms[1] = cbSecondTerm.isChecked();

        if(id == R.id.cb_third_term)
            terms[2] = cbThirdTerm.isChecked();;

        if(id == R.id.btn_cancel)
            dismiss();

        if(id == R.id.btn_confirm) {
            dismiss();
            if(mListener != null)
                  mListener.onclick(terms);
        }

    }

    public void setOnPositiveButtonClickListener(OnPositiveClickListener listener){
        if(mListener == null)
            this.mListener = listener;
    }

    private void initView(View view) {

        for(int i=0;i<terms.length;i++){
            terms[i] = false;
        }
        tvTermAll = (TextView) view.findViewById(R.id.tv_term_all);
        rbAll = (CheckBox) view.findViewById(R.id.rb_all);
        tvFirstTerm = (TextView) view.findViewById(R.id.tv_first_term);
        cbFirstTerm = (CheckBox) view.findViewById(R.id.cb_first_term);
        tvSecondTerm = (TextView) view.findViewById(R.id.tv_second_term);
        cbSecondTerm = (CheckBox) view.findViewById(R.id.cb_second_term);
        tvThirdTerm = (TextView) view.findViewById(R.id.tv_third_term);
        cbThirdTerm = (CheckBox) view.findViewById(R.id.cb_third_term);
        btnConfirm = (TextView) view.findViewById(R.id.btn_confirm);
        btnCancel = (TextView) view.findViewById(R.id.btn_cancel);

        tvTermAll.setOnClickListener(this);
        rbAll.setOnClickListener(this);
        tvFirstTerm.setOnClickListener(this);
        cbFirstTerm.setOnClickListener(this);
        cbSecondTerm.setOnClickListener(this);
        cbThirdTerm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        rbAll.setChecked(true);
        cbFirstTerm.setChecked(true);
        cbSecondTerm.setChecked(true);
        cbThirdTerm.setChecked(true);
    }

    private void initSelectTerm(){
        for(int i=0;i<terms.length;i++){
            terms[i] = true;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_select_term,null);
        initView(view);
        initSelectTerm();
        Dialog dialog = createBottomDialog(view);
        return dialog;
    }

    public interface OnPositiveClickListener{
        void onclick(boolean[] terms);
    }

}
