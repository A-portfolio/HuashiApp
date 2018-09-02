package net.muxi.huashiapp.ui.score.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.muxistudio.appcommon.data.Score;

import com.muxistudio.common.util.DimensUtil;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.CenterDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ScoreDetailDialog  extends CenterDialogFragment{


    public static ScoreDetailDialog newInstance(ArrayList<Score> scores, int position){
        Bundle args = new Bundle();
        args.putParcelableArrayList("scoreArray",scores);
        args.putInt("position",position);

        ScoreDetailDialog fragment = new ScoreDetailDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view  = LayoutInflater.from(getContext()).inflate(R.layout.view_score_detail,null,false);

        TextView tvCourseType= view.findViewById(R.id.tv_course_type);
        TextView tvCourseCredit = view.findViewById(R.id.tv_course_credit);
        TextView tvCourseGrade = view.findViewById(R.id.tv_grade_value);
        TextView tvCourseUsualGrade = view.findViewById(R.id.tv_usual_grade_value);
        TextView tvCourseExamGrade  = view.findViewById(R.id.tv_exam_grade_value);

        TextView tvCourseExam  = view.findViewById(R.id.tv_exam_grade);
        TextView tvCourseUsual = view.findViewById(R.id.tv_usual_grade);

        TextView tvCourseName = view.findViewById(R.id.tv_course_name);

        List<Score> mScores = (List<Score>) getArguments().get("scoreArray");

        int position = (int) getArguments().get("position");

        tvCourseName.setText(mScores.get(position).course);
        tvCourseType.setText(mScores.get(position).kcxzmc);
        tvCourseCredit.setText(mScores.get(position).credit);
        tvCourseGrade.setText(mScores.get(position).grade);

        boolean usualGradeEmpty = false;
        boolean examGradeEmpty  = false;
        //现在教务系统课程的平时成绩和期末考试成绩可能为空
        tvCourseUsualGrade.setText(mScores.get(position).usual);
        if(TextUtils.isEmpty(mScores.get(position).usual)){
            tvCourseUsualGrade.setVisibility(View.INVISIBLE);
            tvCourseUsual.setVisibility(View.INVISIBLE);
            usualGradeEmpty = true;
        }

        tvCourseExamGrade.setText(mScores.get(position).ending);
        if(TextUtils.isEmpty(mScores.get(position).ending)) {
            tvCourseExamGrade.setVisibility(View.INVISIBLE);
            tvCourseExam.setVisibility(View.INVISIBLE);
            examGradeEmpty = true;
        }

        Dialog dialog = createCenterDialog(view);

        TextView btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener( view1 ->{
            dialog.dismiss();
        });

        if(examGradeEmpty && usualGradeEmpty){
          RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
              ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
          params.addRule(RelativeLayout.BELOW,R.id.tv_grade);
          params.addRule(RelativeLayout.CENTER_HORIZONTAL);
          params.setMargins(0, DimensUtil.dp2px(30),0,0);
          btnConfirm.setLayoutParams(params);
        }
        return dialog;
    }
}
