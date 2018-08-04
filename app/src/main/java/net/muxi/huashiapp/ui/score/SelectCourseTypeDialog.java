package net.muxi.huashiapp.ui.score;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.muxistudio.appcommon.widgets.BottomPickerDialogFragment;

import net.muxi.huashiapp.R;

public class SelectCourseTypeDialog extends BottomPickerDialogFragment implements View.OnClickListener{

    private RadioButton mRb;

    private CheckBox mZyzgk;
    private CheckBox mZyxxk;
    private CheckBox mTsbxk;
    private CheckBox mTsxxk;

    private TextView mBtnConfirm;
    private TextView mBtnCancel;

    private OnPositiveClickListener mListener;

    //选择了课程中的哪几种
    private boolean[] courses = new boolean[4];

    public static SelectCourseTypeDialog newInstance(){
        SelectCourseTypeDialog dialog = new SelectCourseTypeDialog();
        return dialog;
    }

    public boolean[] getCourses(){
        return courses;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.cb_zyzgk){
            courses[0] = true;
        }

        if(id == R.id.cb_zyxxk){
            courses[1] = true;
        }

        if(id == R.id.cb_tsbxk){
            courses[2] = true;
        }

        if(id == R.id.cb_tsxxk){
            courses[3] = true;
        }

        if(id == R.id.rb_all){
            for(int i = 0; i< courses.length; i++){
                courses[i] = true;
            }

            mZyzgk.setChecked(true);
            mZyxxk.setChecked(true);
            mTsbxk.setChecked(true);
            mTsxxk.setChecked(true);
        }

        if(id == R.id.btn_cancel)
            dismiss();

        if(id == R.id.btn_confirm){
            dismiss();
            mListener.onclick(courses);
        }
    }


    private void initView(View view){

        for(int i = 0; i< courses.length; i++){
            courses[i] = false;
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

    public void setOnPositiveButtonClickListener(OnPositiveClickListener listener){
        if(listener!=null){
            this.mListener = listener;
        }
    }

    public interface OnPositiveClickListener{
        void onclick(boolean courses[]);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_select_course_type,null);
        Dialog dialog = createBottomDialog(view);
        initView(view);
        return dialog  ;
    }
}
