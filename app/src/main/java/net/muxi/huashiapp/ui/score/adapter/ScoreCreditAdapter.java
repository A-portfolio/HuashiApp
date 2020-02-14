package net.muxi.huashiapp.ui.score.adapter;


//import android.support.annotation.NonNull;
//import android.support.v4.app.FragmentActivity;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.muxistudio.appcommon.data.Score;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.score.fragments.ScoreDetailDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//包括展示成绩和计算学分绩
public class ScoreCreditAdapter extends RecyclerView.Adapter<ScoreCreditAdapter.ViewHolder>{

    Map<Integer,Boolean> mCheckMap = new HashMap<>();
    ArrayList<Score> mScores;
    FragmentActivity mContext;

    public ScoreCreditAdapter(ArrayList<Score> scores) {
        super();
        this.mScores = scores;
        initCheckList(true);
    }

    //默认情况下全选checklist中所有的成绩 去计算学分绩
    private void initCheckList(boolean mAllChecked){
        int size = mScores.size();
        for(int i=0;i<size;i++){
            mCheckMap.put(i,mAllChecked);
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
    public void setAllChecked(boolean allChecked){
        initCheckList(allChecked);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = (FragmentActivity) parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_score_credit,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String totalScore = String.format("总成绩:%s",mScores.get(position).grade);


        //由于recycleview的复用，多页的checkbox会出现混乱，点击一个可能好几个跟着变，所以要用这个map来记录
        if(mCheckMap.keySet().contains(position)){
          if(mCheckMap.get(position))
            holder.mCbCredit.setChecked(true);
          else
            holder.mCbCredit.setChecked(false);
        }



        holder.mTvTotalScore.setText(totalScore);

        String credit = String.format("学分:%s",mScores.get(position).credit);
        holder.mTvCourseCredit.setText(credit);

        holder.mTvCourseName.setText(mScores.get(position).course);

        holder.mItemView.setOnClickListener(v ->{
            ScoreDetailDialog dialog = ScoreDetailDialog.newInstance(mScores,position);
            dialog.show(mContext.getSupportFragmentManager(),"score_detail");
        });

        //判断是否是 浮点型数字
        if(!isDouble(mScores.get(position).grade)){
            holder.mCbCredit.setVisibility(View.INVISIBLE);
            mCheckMap.remove(position);
            mCheckMap.put(position,false);
        }

        //先变化ui 变化之后的状态
        holder.mCbCredit.setOnClickListener(v -> {
            Log.i("bug", "onBindViewHolder: "+position);
            if(!holder.mCbCredit.isChecked()){
                mCheckMap.put(position,false);
            }else{
                mCheckMap.put(position,true);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mScores.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        //注意：需要给textView添加分类，在xml文件中是硬编码的 需要格式化 比如 "总成绩是:%s"

        private TextView mTvCourseCredit;

        private TextView mTvTotalScore;
        private TextView mTvCourseName;

        //默认情况下学分绩全选
        private CheckBox mCbCredit;
        private View mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCbCredit     = itemView.findViewById(R.id.cb_cal_credit);
            mTvCourseCredit = itemView.findViewById(R.id.course_credit);
            mTvTotalScore = itemView.findViewById(R.id.tv_score_total_value);
            mTvCourseName = itemView.findViewById(R.id.tv_course_name);
            mItemView = itemView;
            mCbCredit.setChecked(true);

          }

    }


}
