package net.muxi.huashiapp.ui.score.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muxistudio.appcommon.data.Score;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;

//包括展示成绩和计算学分绩
public class ScoreCreditAdapter extends RecyclerView.Adapter<ScoreCreditAdapter.ViewHolder>{


    List<Score> mScores = new ArrayList<>();
    Context mContext;

    public ScoreCreditAdapter(List<Score> scores) {
        super();
        this.mScores = scores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_score_credit,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String totalScore = String.format("总成绩是:%s",mScores.get(position).grade);
        String usualScore = String.format("平时成绩是:%s",mScores.get(position).usual);
        String examScore = String.format("总成绩是:%s",mScores.get(position).ending);

        holder.mTvTotalScore.setText(totalScore);
        holder.mTvUsualScore.setText(usualScore);
        holder.mTvExamScore.setText(examScore);

        holder.mTvCourseType.setText(mScores.get(position).kcxzmc);
        holder.mTvCourseCredit.setText(mScores.get(position).credit);
    }

    @Override
    public int getItemCount() {
        return mScores.size();
    }





    static class ViewHolder extends RecyclerView.ViewHolder {

        //注意：需要给textView添加分类，在xml文件中是硬编码的 需要格式化 比如 "总成绩是:%s"

        private TextView mTvCourseType;
        private TextView mTvCourseCredit;

        private TextView mTvTotalScore;
        private TextView mTvUsualScore;
        private TextView mTvExamScore;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvCourseType = itemView.findViewById(R.id.tv_course_type);
            mTvCourseCredit = itemView.findViewById(R.id.tv_course_credit);

            mTvTotalScore = itemView.findViewById(R.id.tv_score_total_value);
            mTvUsualScore = itemView.findViewById(R.id.tv_score_usual_value);
            mTvExamScore  = itemView.findViewById(R.id.tv_score_exam_value);
        }

    }


}
