package net.muxi.huashiapp.ui.score.fragments;

import android.app.Dialog;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.muxistudio.appcommon.data.Score;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.login.SingleCCNUClient;
import net.muxi.huashiapp.widget.CenterDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ScoreDetailDialog  extends CenterDialogFragment{


    private Score mScore;
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

        TextView tvCourseType= view.findViewById(R.id.tv_course_type_value);
        TextView tvCourseCredit = view.findViewById(R.id.tv_course_credit);
        TextView tvCourseGrade = view.findViewById(R.id.tv_grade_value);
        TextView tvCourseUsualGrade = view.findViewById(R.id.tv_usual_grade_value);
        TextView tvCourseExamGrade  = view.findViewById(R.id.tv_exam_grade_value);

        TextView tvCourseExam  = view.findViewById(R.id.tv_exam_grade);
        TextView tvCourseUsual = view.findViewById(R.id.tv_usual_grade);

        TextView tvCourseName = view.findViewById(R.id.tv_course_name);

        List<Score> mScores = (List<Score>) getArguments().get("scoreArray");

        int position = (int) getArguments().get("position");
        mScore=mScores.get(position);
        tvCourseType.setText(mScores.get(position).kcxzmc);
        tvCourseName.setText(mScores.get(position).course);
        tvCourseCredit.setText(mScores.get(position).credit);
        tvCourseGrade.setText(mScores.get(position).grade);

        boolean usualGradeEmpty = false;
        boolean examGradeEmpty  = false;
        //现在教务系统课程的平时成绩和期末考试成绩可能为空

        tvCourseUsualGrade.setText(mScores.get(position).usual+"获取中...");


        tvCourseExamGrade.setText(mScores.get(position).ending+"获取中...");

        Dialog dialog = createCenterDialog(view);

        TextView btnConfirm = view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener( view1 ->{
            dialog.dismiss();
        });

        SingleCCNUClient.getClient().getUAE(mScore.jxb_id,mScore.xnm,mScore.xqm,mScore.course)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        tvCourseUsualGrade.setText("获取失败");


                        tvCourseExamGrade.setText("获取失败");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String[]uae=getScoreFromHtml(responseBody.string());
                            tvCourseUsualGrade.setText(uae[0]);
                            tvCourseExamGrade.setText(uae[1]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });


//        if(examGradeEmpty && usualGradeEmpty){
//          RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//              ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//          params.addRule(RelativeLayout.BELOW,R.id.tv_grade);
//          params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//          params.setMargins(0, DimensUtil.dp2px(30),0,0);
//          btnConfirm.setLayoutParams(params);
//        }
        return dialog;
    }

    private String[] getScoreFromHtml(String html){
        String[]strings=new String[2];
        Pattern p = Pattern.compile("<td valign=\"middle\">【 平时 】</td>.+?<td valign=\"middle\">(.+?)%&nbsp;</td>.+?<td valign=\"middle\">(.+?)&nbsp;</td>", Pattern.DOTALL);
        Pattern p2 = Pattern.compile("<td valign=\"middle\">【 期末 】</td>.+?<td valign=\"middle\">(.+?)%&nbsp;</td>.+?<td valign=\"middle\">(.+?)&nbsp;</td>", Pattern.DOTALL);
        Matcher m = p.matcher(html);
        Matcher m2=p2.matcher(html);
        if (m.find()){
            strings[0]= String.format("%s (%s%%)",m.group(2),m.group(1));
        }else {
            strings[0]= "获取失败";
        }
        if (m2.find()){
            strings[1]= String.format("%s (%s%%)",m2.group(2),m2.group(1));
        }else {
            strings[1]= "获取失败";
        }
        return strings;
    }


}
