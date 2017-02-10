package net.muxi.huashiapp.ui.credit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Scores;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/10.
 */

public class CreditGradeAdapter extends RecyclerView.Adapter<CreditGradeAdapter.ViewHolder> {

    private List<Scores> mScoresList;
    private List<Integer> checkedList = new ArrayList<>();

    public CreditGradeAdapter(List<Scores> scoresList) {
        mScoresList = scoresList;
        for (int i =0;i < mScoresList.size();i ++){
            checkedList.add(i);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_credit_grade,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTvCourse.setText(mScoresList.get(position).getCourse());
        holder.mTvCategory.setText(mScoresList.get(position).getKcxzmc());
        holder.mTvCredit.setText(mScoresList.get(position).getCredit());
        holder.mTvProperty.setText(mScoresList.get(position).getType());
        holder.mLayoutItem.setOnClickListener(v -> {
            if (checkedList.contains(position)){
                checkedList.remove((Object)position);
//                holder.mtv
            }
        });
    }

    public List<Integer> getCheckedList(){
        return checkedList;
    }

    @Override
    public int getItemCount() {
        return mScoresList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.layout_item)
        RelativeLayout mLayoutItem;
        @BindView(R.id.tv_property)
        TextView mTvProperty;
        @BindView(R.id.tv_course)
        TextView mTvCourse;
        @BindView(R.id.tv_category)
        TextView mTvCategory;
        @BindView(R.id.tv_credit)
        TextView mTvCredit;
        @BindView(R.id.iv_checked)
        ImageView mIvChecked;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
