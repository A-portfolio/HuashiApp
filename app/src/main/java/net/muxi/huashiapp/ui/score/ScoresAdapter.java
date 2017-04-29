package net.muxi.huashiapp.ui.score;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.DetailScores;
import net.muxi.huashiapp.common.data.Scores;
import net.muxi.huashiapp.ui.credit.ScoreUtil;
import net.muxi.huashiapp.util.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/6/20.
 */
public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {

    private List<Scores> mScoresList;
    private List<DetailScores> mDetailScores;
    private Context mContext;
    //存放已展开的卡片

    public ScoresAdapter(List<Scores> scoresList,List<DetailScores> detailScores) {
        super();
        mScoresList = scoresList;
        mDetailScores = detailScores;
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
        holder.mTvCate1.setText(ScoreUtil.toCate1(mScoresList.get(position).kcxzmc));
        holder.mTvCate2.setText(ScoreUtil.toCate2(mScoresList.get(position).kcxzmc));
        holder.mTvScore.setText("总成绩：" + mScoresList.get(position).grade);
        holder.mTvCredit.setText("学分：" + mScoresList.get(position).credit);

        if (Float.parseFloat(mScoresList.get(position).grade) < 60){
            holder.mTvProperty.setBackgroundResource(R.drawable.shape_red);
            holder.mTvScore.setTextColor(App.sContext.getResources().getColor(R.color.red));
        }

        if (position < mDetailScores.size()){
            holder.mTvUsual.setText("平时："+ mDetailScores.get(position).usual);
            holder.mTvEnding.setText("期末："+ mDetailScores.get(position).ending);
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_property)
        TextView mTvProperty;
        @BindView(R.id.tv_course)
        TextView mTvCourse;
        @BindView(R.id.tv_cate1)
        TextView mTvCate1;
        @BindView(R.id.tv_cate2)
        TextView mTvCate2;
        @BindView(R.id.tv_score)
        TextView mTvScore;
        @BindView(R.id.tv_credit)
        TextView mTvCredit;
        @BindView(R.id.tv_usual)
        TextView mTvUsual;
        @BindView(R.id.tv_ending)
        TextView mTvEnding;
        @BindView(R.id.layout_item)
        RelativeLayout mLayoutItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
