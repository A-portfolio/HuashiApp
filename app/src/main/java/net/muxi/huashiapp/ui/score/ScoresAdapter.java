package net.muxi.huashiapp.ui.score;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.DetailScores;
import net.muxi.huashiapp.common.data.Scores;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/6/20.
 */
public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {

    private static final int SPACE_MIN_HEIGHT = DimensUtil.dp2px(12);
    private static final int SPACE_MAX_HEIGHT = DimensUtil.dp2px(32);

    private List<Scores> mScoresList;
    private Context mContext;
    private List<DetailScores> mDetailScores;
    //存放已展开的卡片
    private List<Integer> mList;

    public ScoresAdapter(List<Scores> scoresList) {
        super();
        mScoresList = scoresList;
        mDetailScores = new ArrayList<>();
        mList = new ArrayList<>();
    }

    public void swap(List<Scores> scoresList, List<DetailScores> detailScores) {
        mScoresList.clear();
        mScoresList = scoresList;
        mDetailScores.clear();
        mDetailScores = detailScores;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mScoresList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTvCourseName.setText(mScoresList.get(position).getCourse());
        holder.mTvCourseCategory.setText(mScoresList.get(position).getCategory());
        holder.mTvCourseProperty.setText(mScoresList.get(position).getType());
        if (mScoresList.get(position).getGrade().equals("")) {
            holder.mTvScoreTotal.setText("无");
        } else {
            holder.mTvScoreTotal.setText(mScoresList.get(position).getGrade());
        }
        holder.mTvCredit.setText("学分:" + mScoresList.get(position).getCredit());

        if (mList != null) {
            setDetailLayout(holder, position);
        }
        DetailScores detailScores = getDetailScore(mScoresList.get(position).getCourse());
        if (detailScores != null) {
            if (detailScores.getUsual().equals("")) {
                holder.mTvScoreUsual.setText("无");
            } else {
                holder.mTvScoreUsual.setText(detailScores.getUsual());
            }
            if (detailScores.getEnding().equals("")) {
                holder.mTvScoreEnding.setText("无");
            } else {
                holder.mTvScoreEnding.setText(detailScores.getEnding());
            }
        }else {
            holder.mTvScoreUsual.setText("");
            holder.mTvScoreEnding.setText("");
        }

        holder.mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment curFragment = ((BaseActivity) mContext).getSupportFragmentManager().findFragmentById(
//
//                        R.id.fragment_layout);
//                ((ScoreDetailFragment) curFragment).loadSpecifiedScore(position);
                if (!mList.contains(position)) {
                    scaleSpace(holder.mSpaceAnim, SPACE_MIN_HEIGHT, SPACE_MAX_HEIGHT);
                    Logger.d("load more");
                    holder.mImgLoadMore.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Logger.d("expand");
                            holder.mDetailLayout.setVisibility(View.VISIBLE);
                            holder.mImgClose.setVisibility(View.VISIBLE);
                        }
                    }, 250);
                    mList.add(position);
                } else {
                    scaleSpace(holder.mSpaceAnim, SPACE_MAX_HEIGHT, SPACE_MIN_HEIGHT);
                    holder.mDetailLayout.setVisibility(View.GONE);
                    holder.mImgClose.setVisibility(View.GONE);
                    Logger.d("close ");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Logger.d("switch");
                            holder.mImgLoadMore.setVisibility(View.VISIBLE);
                        }
                    }, 250);
                    mList.remove((Integer) position);
                }
            }
        });
    }

    private void setDetailLayout(ViewHolder holder, int position) {
        if (mList.contains(position)) {
            holder.mImgLoadMore.setVisibility(View.GONE);
            holder.mImgClose.setVisibility(View.VISIBLE);
            holder.mSpaceAnim.getLayoutParams().height = SPACE_MAX_HEIGHT;
            holder.mDetailLayout.setVisibility(View.VISIBLE);
        } else {
            holder.mImgLoadMore.setVisibility(View.VISIBLE);
            holder.mSpaceAnim.getLayoutParams().height = SPACE_MIN_HEIGHT;
            holder.mImgClose.setVisibility(View.GONE);
            holder.mDetailLayout.setVisibility(View.GONE);
        }
    }

    private void setDetailScore(ViewHolder holder, DetailScores detailScore) {
        if (detailScore.getUsual().equals("")) {
            holder.mTvScoreUsual.setText("无");
        } else {
            holder.mTvScoreUsual.setText(detailScore.getUsual());
        }
        if (detailScore.getEnding().equals("")) {
            holder.mTvScoreEnding.setText("无");
        } else {
            holder.mTvScoreEnding.setText(detailScore.getEnding());
        }
    }

    public DetailScores getDetailScore(String course) {
        for (DetailScores detailScore : mDetailScores) {
            if (detailScore.getCourse().equals(course)) {
                return detailScore;
            }
        }
        return null;
    }

    public void addDetailScore(DetailScores score, int position) {
        mDetailScores.add(score);
        notifyDataSetChanged();
    }

    private void scaleSpace(final Space spaceAnim, int startHeight, int endHeight) {
//        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.score_detail_scale);
//        spaceAnim.setAnimation(animation);
//        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 1, 1, 32, 1, 0);
//        scaleAnimation.setDuration(250);
//        scaleAnimation.setFillAfter(true);
//        spaceAnim.startAnimation(scaleAnimation);
        ValueAnimator valueAnimator;
        valueAnimator = ValueAnimator.ofInt(startHeight, endHeight);
        valueAnimator.setDuration(250);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                spaceAnim.getLayoutParams().height = value.intValue();
                spaceAnim.requestLayout();
            }
        });
        valueAnimator.start();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
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
        @BindView(R.id.tv_score_total)
        TextView mTvScoreTotal;
        @BindView(R.id.normal_layout)
        RelativeLayout mNormalLayout;
        @BindView(R.id.img_load_more)
        ImageView mImgLoadMore;
        @BindView(R.id.img_close)
        ImageView mImgClose;
        @BindView(R.id.tv_score_usual)
        TextView mTvScoreUsual;
        @BindView(R.id.tv_score_ending)
        TextView mTvScoreEnding;
        @BindView(R.id.detail_layout)
        RelativeLayout mDetailLayout;
        @BindView(R.id.space_anim)
        Space mSpaceAnim;
        @BindView(R.id.root_layout)
        CardView mRootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
