package net.muxi.huashiapp.score;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Scores;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/6/20.
 */
public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {



    private List<Scores> mScoresList;

    public ScoresAdapter(List<Scores> scoresList) {
        super();
        mScoresList = scoresList;
    }

    @Override
    public int getItemCount() {
        return mScoresList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTvCourseName.setText(mScoresList.get(position).getCourse());
        holder.mTvCourseCategory.setText(mScoresList.get(position).getCategory());
        holder.mTvCourseProperty.setText(mScoresList.get(position).getType());
        holder.mTvScoreUsual.setText(mScoresList.get(position).getGrade().getUsual());
        holder.mTvScoreEnding.setText(mScoresList.get(position).getGrade().getEnding());
        holder.mTvScoreTotal.setText(mScoresList.get(position).getGrade().getTotal());
        holder.mTvCredit.setText("学分:" + mScoresList.get(position).getCredit());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_course_name)
        TextView mTvCourseName;
        @BindView(R.id.tv_course_category)
        TextView mTvCourseCategory;
        @BindView(R.id.tv_course_property)
        TextView mTvCourseProperty;
        @BindView(R.id.tv_credit)
        TextView mTvCredit;
        @BindView(R.id.tv_score_usual)
        TextView mTvScoreUsual;
        @BindView(R.id.tv_score_ending)
        TextView mTvScoreEnding;
        @BindView(R.id.tv_score_total)
        TextView mTvScoreTotal;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

}
