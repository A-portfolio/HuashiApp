package net.muxi.huashiapp.ui.studyroom;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.BottomDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 17/2/9.
 */

public class StudyTimePickerDialogFragment extends BottomDialogFragment {


    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.study_time_picker_view)
    StudyTimePickerView mStudyTimePickerView;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;

    private OnPositiveButtonClickListener mOnPositiveButtonClickListener;
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
        ButterKnife.bind(this,view);
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

    public interface OnPositiveButtonClickListener{
        void onPositiveButtonClick(int week,int day);
    }
}
