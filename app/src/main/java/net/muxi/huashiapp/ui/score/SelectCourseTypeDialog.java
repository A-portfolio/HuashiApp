package net.muxi.huashiapp.ui.score;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.widgets.BottomPickerDialogFragment;

import net.muxi.huashiapp.R;

import java.util.HashMap;

public class SelectCourseTypeDialog extends BottomPickerDialogFragment implements View.OnClickListener{

    private RadioButton mRb;

    private CheckBox mZyzgk;
    private CheckBox mGxfzk;
    private CheckBox mTshxk;
    private CheckBox mTsbxk;
    private CheckBox mTsxxk;

    private OnPositiveClickListener mListener;

    //选择了课程中的哪几种
    private HashMap<String,Boolean> mSelectedTypes = new HashMap<>();

    public static SelectCourseTypeDialog newInstance(){
        SelectCourseTypeDialog dialog = new SelectCourseTypeDialog();
        return dialog;
    }

    /**
     * 设置selecedTypes 注意： 其他课程字段也设置为选择
     */
    private void setState(){
        if(mRb.isChecked()) {

            for(String keys: Constants.CLASS_TYPE){
                mSelectedTypes.put(keys,true);
            }

            mZyzgk.setChecked(true);
            mGxfzk.setChecked(true);
            mTshxk.setChecked(true);
            mTsbxk.setChecked(true);
            mTsxxk.setChecked(true);
        }else{

            for(String keys: Constants.CLASS_TYPE){
                mSelectedTypes.put(keys,false);
            }
            mTshxk.setChecked(false);
            mZyzgk.setChecked(false);
            mGxfzk.setChecked(false);
            mTsbxk.setChecked(false);
            mTsxxk.setChecked(false);
        }
    }

    //todo to refractor
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.cb_zyzgk){
            if(mZyzgk.isChecked())
                mSelectedTypes.put(Constants.CLASS_TYPE[0],true);
            else
                mSelectedTypes.put(Constants.CLASS_TYPE[0],false);
        }

        if(id == R.id.cb_gxfzk){
            mSelectedTypes.put(Constants.CLASS_TYPE[1],true);
        }

        if(id == R.id.cb_tshxk){
            mSelectedTypes.put(Constants.CLASS_TYPE[2],true);
        }

        if(id == R.id.cb_tsbxk){
            mSelectedTypes.put(Constants.CLASS_TYPE[3],true);
        }

        if(id == R.id.cb_tsxxk){
           mSelectedTypes.put(Constants.CLASS_TYPE[4],true);
        }


        if(id == R.id.rb_all){
            setState();
        }

        if(id == R.id.btn_cancel)
            dismiss();

        if(id == R.id.btn_confirm){
            dismiss();
            mListener.onclick(mSelectedTypes);
        }
    }




    private void initView(View view){


        mZyzgk = view.findViewById(R.id.cb_zyzgk);
        mGxfzk = view.findViewById(R.id.cb_gxfzk);
        mTshxk = view.findViewById(R.id.cb_tshxk);
        mTsbxk = view.findViewById(R.id.cb_tsbxk);
        mTsxxk = view.findViewById(R.id.cb_tsxxk);

        TextView mBtnConfirm = view.findViewById(R.id.btn_confirm);
        TextView mBtnCancel = view.findViewById(R.id.btn_cancel);

        mRb    = view.findViewById(R.id.rb_all);

        mRb.setOnClickListener(this);
        mZyzgk.setOnClickListener(this);
        mGxfzk.setOnClickListener(this);
        mTshxk.setOnClickListener(this);
        mTsbxk.setOnClickListener(this);
        mTsxxk.setOnClickListener(this);

        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    public void setOnPositiveButtonClickListener(OnPositiveClickListener listener){
        if(listener == null){
            this.mListener = listener;
        }
    }

    public interface OnPositiveClickListener{
        void onclick(HashMap<String,Boolean> selectedCourse);
    }

    private void initSelectTypes(){
        for(String key:Constants.CLASS_TYPE){
            mSelectedTypes.put(key,true);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_select_course_type,null);
        Dialog dialog = createBottomDialog(view);
        initView(view);
        initSelectTypes();
        return dialog  ;
    }
}
