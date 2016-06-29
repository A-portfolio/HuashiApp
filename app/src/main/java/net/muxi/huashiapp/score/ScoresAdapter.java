package net.muxi.huashiapp.score;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Scores;

import java.util.List;

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
        holder.mTvScore.setText(mScoresList.get(position).getGrade());
        holder.mTvCredit.setText("学分:" + mScoresList.get(position).getCredit());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score,parent,false);
        return new ViewHolder(view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvCourseName;
        private TextView mTvScore;
        private TextView mTvCategory;
        private TextView mTvCredit;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvCourseName = (TextView)itemView.findViewById(R.id.tv_course_name);
            mTvScore = (TextView)itemView.findViewById(R.id.tv_score);
            mTvCategory = (TextView) itemView.findViewById(R.id.tv_category);
            mTvCredit = (TextView)itemView.findViewById(R.id.tv_credit);

        }

    }

}
