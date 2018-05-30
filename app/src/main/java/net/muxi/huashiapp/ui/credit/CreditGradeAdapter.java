package net.muxi.huashiapp.ui.credit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxistudio.appcommon.data.Score;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ybao on 17/2/10.
 */

public class CreditGradeAdapter extends RecyclerView.Adapter<CreditGradeAdapter.ViewHolder> {


    private List<Score> mScoresList;
    private List<Integer> checkedList = new ArrayList<>();


    public CreditGradeAdapter(List<Score> scoresList) {
        mScoresList = scoresList;
        for (int i = 0; i < mScoresList.size(); i++) {
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
        holder.mTvCourse.setText(mScoresList.get(position).course);
        holder.mTvCategory.setText(ScoreUtil.toCategory(mScoresList.get(position).kcxzmc));
        holder.mTvCredit.setText(String.format("学分：%s", mScoresList.get(position).credit));
        holder.mTvProperty.setText(mScoresList.get(position).type);
        holder.mTvScore.setText(String.format("总成绩：%s", mScoresList.get(position).grade));
        if (checkedList.contains(position)) {
            holder.mIvChecked.setVisibility(View.VISIBLE);
            //成绩有可能出现"缓考" 字段 而不是正常的float变量
            if (Character.isDigit(mScoresList.get(position).grade.charAt(0))) {
                if (Float.parseFloat(mScoresList.get(position).grade) >= 60.0) {
                    holder.mTvProperty.setBackgroundResource(R.drawable.shape_green);
                } else {
                    holder.mTvProperty.setBackgroundResource(R.drawable.shape_red);
                }
            } else {
                holder.mIvChecked.setVisibility(View.INVISIBLE);
                holder.mTvProperty.setBackgroundResource(R.drawable.shape_unchecked);
            }
            holder.mLayoutItem.setOnClickListener(v -> {
                if (checkedList.contains(position)) {
                    checkedList.remove((Object) position);
                    holder.mIvChecked.setVisibility(View.INVISIBLE);
                    holder.mTvProperty.setBackgroundResource(R.drawable.shape_unchecked);
                } else {
                    checkedList.add(position);
                    holder.mIvChecked.setVisibility(View.VISIBLE);
                    if (Float.parseFloat(mScoresList.get(position).grade) >= 60.0) {
                        holder.mTvProperty.setBackgroundResource(R.drawable.shape_green);
                    } else {
                        holder.mTvProperty.setBackgroundResource(R.drawable.shape_red);
                    }
                }
            });
        }
    }

    public List<Integer> getCheckedList() {
        return checkedList;
    }

    public void setAllChecked() {
        if (checkedList == null) {
            return;
        }
        checkedList.clear();
        for (int i = 0; i < mScoresList.size(); i++) {
            checkedList.add(i);
        }
    }

    @Override
    public int getItemCount() {
        return mScoresList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mLayoutItem;
        private TextView mTvProperty;
        private TextView mTvCourse;
        private TextView mTvCategory;
        private TextView mTvScore;
        private TextView mTvCredit;
        private ImageView mIvChecked;

        public ViewHolder(View itemView) {
            super(itemView);
            mLayoutItem = itemView.findViewById(R.id.layout_item);
            mTvProperty = itemView.findViewById(R.id.tv_property);
            mTvCourse = itemView.findViewById(R.id.tv_course);
            mTvCategory = itemView.findViewById(R.id.tv_category);
            mTvScore = itemView.findViewById(R.id.tv_score);
            mTvCredit = itemView.findViewById(R.id.tv_credit);
            mIvChecked = itemView.findViewById(R.id.iv_checked);
        }
    }
}
