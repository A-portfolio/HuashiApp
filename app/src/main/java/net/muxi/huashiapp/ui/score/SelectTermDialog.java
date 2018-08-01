package net.muxi.huashiapp.ui.score;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.muxistudio.appcommon.widgets.BottomPickerDialogFragment;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.timeTable.LargeSizeNumberPicker;

public class SelectTermDialog extends BottomPickerDialogFragment implements View.OnClickListener{

    private RadioButton mRb;

    private CheckBox mZyzgk;
    private CheckBox mZyxxk;
    private CheckBox mTsbxk;
    private CheckBox mTsxxk;

    private Button mBtnConfirm;
    private Button mBtnCancel;

    //选择了课程中的哪几种
    private boolean[] terms = new boolean[4];
    public static SelectTermDialog newInstance(boolean terms[]){
        Bundle args = new Bundle();
        args.putBooleanArray("terms_value",terms);

        SelectTermDialog dialog = new SelectTermDialog();
        dialog.setArguments(args);
        return dialog;
    }

    public boolean[] getTerms(){
        return terms;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.cb_zyzgk){
            terms[0] = true;
        }

        if(id == R.id.cb_zyxxk){
            terms[1] = true;
        }

        if(id == R.id.cb_tsbxk){
            terms[2] = true;
        }

        if(id == R.id.cb_tsxxk){
            terms[3] = true;
        }

        if(id == R.id.rb_all){
            for(int i= 0;i<terms.length;i++){
                terms[i] = true;
            }

            mZyzgk.setChecked(true);
            mZyxxk.setChecked(true);
            mTsbxk.setChecked(true);
            mTsxxk.setChecked(true);
        }
    }

    private void initView(View view){

        for(int i= 0;i<terms.length;i++){
            terms[i] = false;
        }

        mZyzgk = view.findViewById(R.id.cb_zyzgk);
        mZyxxk = view.findViewById(R.id.cb_zyxxk);
        mTsbxk = view.findViewById(R.id.cb_tsbxk);
        mTsxxk = view.findViewById(R.id.cb_tsxxk);

        mBtnConfirm = view.findViewById(R.id.btn_confirm);
        mBtnCancel  = view.findViewById(R.id.btn_cancel);

        mRb    = view.findViewById(R.id.rb_all);

        mRb.setOnClickListener(this);
        mZyzgk.setOnClickListener(this);
        mZyxxk.setOnClickListener(this);
        mTsbxk.setOnClickListener(this);
        mTsxxk.setOnClickListener(this);

        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        terms = getArguments().getBooleanArray("terms_value");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_select_term,null);
        Dialog dialog = createBottomDialog(view);
        initView(view);
        setTitle("");
        return super.onCreateDialog(savedInstanceState);
    }
}
