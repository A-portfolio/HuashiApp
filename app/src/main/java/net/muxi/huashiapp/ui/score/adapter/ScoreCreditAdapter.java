package net.muxi.huashiapp.ui.score.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.muxistudio.appcommon.data.Score;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//包括展示成绩和计算学分绩
public class ScoreCreditAdapter extends RecyclerView.Adapter<ScoreCreditAdapter.ViewHolder>{

    Map<Integer,Boolean> mCheckMap = new HashMap<>();
    List<Score> mScores = new ArrayList<>();
    Context mContext;

    public ScoreCreditAdapter(List<Score> scores) {
        super();
        this.mScores = scores;
    }

    //默认情况下全选checklist中所有的成绩 去计算学分绩
    private void initCheckList(){
        int size = getItemCount();
        for(int i=0;i<size;i++){
            mCheckMap.put(i,true);
        }
    }

    private boolean isDouble(String grade){
        return Character.isDigit(grade.charAt(0));
    }

    /**
     * @return 返回一个判断那些课程已经被选择了的map
     */
    public Map<Integer,Boolean> getCheckMap(){
        return mCheckMap;
    }

    /**
     * 设置checkmap都被选择 作为暴露给外部全选的接口
     */
    public void setAllChecked(){
        initCheckList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_score_credit,parent,false);

        initCheckList();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String totalScore = String.format("总成绩/%s",mScores.get(position).grade);
        String usualScore = String.format("平时成绩/%s",mScores.get(position).usual);
        String examScore = String.format("总成绩/%s",mScores.get(position).ending);

        holder.mTvTotalScore.setText(totalScore);
        holder.mTvUsualScore.setText(usualScore);
        holder.mTvExamScore.setText(examScore);

        holder.mTvCourseType.setText(mScores.get(position).kcxzmc);
        holder.mTvCourseCredit.setText(mScores.get(position).credit);

        holder.mTvCourseName.setText(mScores.get(position).course);
        Set<Integer> keySet=  mCheckMap.keySet();
        //判断是否是 浮点型数字
        if(!isDouble(mScores.get(position).grade)){
            holder.mCbCredit.setVisibility(View.INVISIBLE);
            mCheckMap.remove(position);
            mCheckMap.put(position,false);
        }

        holder.mCbCredit.setOnClickListener(view ->{
            if(keySet.contains(position)){
                mCheckMap.remove(position);
                holder.mCbCredit.setChecked(false);
            }else{
                mCheckMap.put(position,true);
                holder.mCbCredit.setChecked(true);
            }
        });
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
        private TextView mTvCourseName;

        private CheckBox mCbCredit;

        public ViewHolder(View itemView) {
            super(itemView);
            mCbCredit     = itemView.findViewById(R.id.cb_cal_credit);

            mTvCourseType = itemView.findViewById(R.id.tv_course_type);
            mTvCourseCredit = itemView.findViewById(R.id.tv_course_credit);

            mTvTotalScore = itemView.findViewById(R.id.tv_score_total_value);
            mTvUsualScore = itemView.findViewById(R.id.tv_score_usual_value);
            mTvExamScore  = itemView.findViewById(R.id.tv_score_exam_value);

            mTvCourseName = itemView.findViewById(R.id.tv_course_name);
        }

    }


}
