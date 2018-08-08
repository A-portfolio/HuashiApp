package net.muxi.huashiapp.ui.score;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxistudio.appcommon.data.Score;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.credit.ScoreUtil;

import java.util.List;


/**
 * Created by ybao on 16/6/20.
 */
//todo courseName view background add flexibility
public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {

    private List<Score> mScoresList;
    private Context mContext;

    //存放已展开的卡片

    public ScoresAdapter(List<Score> scoresList) {
        super();
        mScoresList = scoresList;
    }

    @Override
    public int getItemCount() {
        return mScoresList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTvCourse.setText(mScoresList.get(position).course);
        holder.mTvProperty.setText(mScoresList.get(position).type);
        String cate1 = ScoreUtil.toCate1(mScoresList.get(position).kcxzmc);
        String cate2 = ScoreUtil.toCate2(mScoresList.get(position).kcxzmc);
        holder.mTvCate1.setText(cate1);
        holder.mTvCate2.setText(cate2);

        if(TextUtils.isEmpty(cate1))
            holder.mTvCate1.setVisibility(View.INVISIBLE);
        if (TextUtils.isEmpty(cate2))
            holder.mTvCate2.setVisibility(View.INVISIBLE);

        holder.mTvScore.setText("总成绩:" + mScoresList.get(position).grade);
        holder.mTvCredit.setText("学分:" + mScoresList.get(position).credit);
        holder.mTvUsual.setText("平时:" + (mScoresList.get(position).usual.equals("") ? "无" : mScoresList.get(position).usual));
        holder.mTvEnding.setText("期末:" + (mScoresList.get(position).ending.equals("") ? "无" : mScoresList.get(position).ending));

        if(Character.isDigit(mScoresList.get(position).grade.charAt(0))) {
            if (Float.parseFloat(mScoresList.get(position).grade) < 60) {
                holder.mTvProperty.setBackgroundResource(R.drawable.shape_red);
                holder.mTvScore.setTextColor(App.sContext.getResources().getColor(R.color.red));
            } else {
                holder.mTvProperty.setBackgroundResource(R.drawable.shape_green);
                holder.mTvScore.setTextColor(App.sContext.getResources().getColor(android.R.color.primary_text_light));
            }
        }else{
            holder.mTvProperty.setBackgroundResource(R.drawable.shape_red);
            holder.mTvScore.setTextColor(App.sContext.getResources().getColor(R.color.red));
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mLayoutItem;
        private TextView mTvProperty;
        private TextView mTvCourse;
        private TextView mTvCate1;
        private TextView mTvCate2;
        private TextView mTvScore;
        private TextView mTvCredit;
        private TextView mTvUsual;
        private TextView mTvEnding;
        private CheckBox mCalCredit;

        public ViewHolder(View itemView) {
            super(itemView);
            mLayoutItem = itemView.findViewById(R.id.layout_item);
            mTvProperty = itemView.findViewById(R.id.tv_property);
            mTvCourse = itemView.findViewById(R.id.tv_course);
            mTvCate1 = itemView.findViewById(R.id.tv_cate1);
            mTvCate2 = itemView.findViewById(R.id.tv_cate2);
            mTvScore = itemView.findViewById(R.id.tv_score);
            mTvCredit = itemView.findViewById(R.id.tv_credit);
            mTvUsual = itemView.findViewById(R.id.tv_usual);
            mTvEnding = itemView.findViewById(R.id.tv_ending);
        }

    }

}
