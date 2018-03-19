package net.muxi.huashiapp.ui.studyroom;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.widgets.BottomDialogFragment;

import net.muxi.huashiapp.R;


/**
 * Created by december on 17/2/9.
 */

public class StudyTimePickerDialogFragment extends BottomDialogFragment {

    private OnPositiveButtonClickListener mOnPositiveButtonClickListener;
    private TextView mTvTitle;
    private StudyTimePickerView mStudyTimePickerView;
    private Button mBtnCancel;
    private Button mBtnEnter;

    //例如第十周周三　传入的就是１０／３则需要减去一作为索引值
    public static StudyTimePickerDialogFragment newInstance(int week,int day){
        Bundle args = new Bundle();
        args.putInt("week",week-1);
        args.putInt("day",day-1);
        StudyTimePickerDialogFragment fragment = new StudyTimePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_study_time, null);
        initView(view);
        int week,day;
        week = getArguments().getInt("week",0);
        day = getArguments().getInt("day",0);
        mStudyTimePickerView.setWeek(week);
        mStudyTimePickerView.setDay(day);
        mTvTitle.setText(
                String.format("第%d周周%s",week + 1, Constants.WEEKDAYS[day]));
        mStudyTimePickerView.setOnValueChangeListener((mweek, mday) -> {
            mTvTitle.setText(
                    String.format("第%d周周%s",mweek + 1, Constants.WEEKDAYS[mday]));
        });

        Dialog dialog = createBottomDialog(view);
        mBtnEnter.setOnClickListener(v -> {
            dialog.dismiss();
            if (mOnPositiveButtonClickListener != null){
                mOnPositiveButtonClickListener.onPositiveButtonClick(
                        mStudyTimePickerView.getWeek(),
                        mStudyTimePickerView.getDay()
                );
            }
        });

        mBtnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        return dialog;

    }

    public void setOnPositiveButtonClickListener(OnPositiveButtonClickListener onPositiveButtonClickListener){
        mOnPositiveButtonClickListener = onPositiveButtonClickListener;

    }

    private void initView(View view) {
        mTvTitle = view.findViewById(R.id.tv_title);
        mStudyTimePickerView = view.findViewById(R.id.study_time_picker_view);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        mBtnEnter = view.findViewById(R.id.btn_enter);
    }

    public interface OnPositiveButtonClickListener{
        void onPositiveButtonClick(int week,int day);
    }
}
