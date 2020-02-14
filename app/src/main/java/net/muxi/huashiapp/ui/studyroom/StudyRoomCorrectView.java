package net.muxi.huashiapp.ui.studyroom;

import android.content.Context;
import android.content.Intent;
//import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;

import com.muxistudio.common.util.DimensUtil;
import com.muxistudio.common.util.Logger;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.SuggestionActivity;

import static net.muxi.huashiapp.widget.BaseDetailLayout.DISTANCE_TO_SLIDE;

/**
 * Created by december on 17/2/10.
 */

public class StudyRoomCorrectView extends RelativeLayout {

    private Context mContext;

    private String mType;

    private int mYDown;
    private int mYDistance;
    private int mCurY;
    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;
    private ImageView mViewCloseBtn;
    private TextView mTvTitle;
    private TextView mTvContent;
    private Button mBtnFeedback;

    public StudyRoomCorrectView(Context context) {
        super(context);
        mContext = context;
        Logger.d(mType);
        mScroller = new Scroller(context);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_study_room_correct, this, true);
        initView(view);
    }

    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.view_close_btn) {
            removeAllViews();
        } else if (id == R.id.btn_feedback) {
            Intent intent = new Intent();
            intent.setClass(mContext, SuggestionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }


    public void smoothScrollTo(int y) {
        int curY = getScrollY();
        int delatY = y - curY;
        mScroller.startScroll(0, curY, 0, delatY);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mYDown = (int) event.getY();
                mCurY = mYDown;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mYDistance = (int) (event.getY() - mCurY);
                if (-mYDistance > -getScrollY()) {
                    scrollBy(0, -getScrollY());
                } else {
                    scrollBy(0, -mYDistance);
                }
                mCurY = (int) event.getY();
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                break;

            case MotionEvent.ACTION_UP:
                if (this.getScrollY() < 0 && mVelocityTracker.getYVelocity() > 500) {
                    this.smoothScrollTo(-DimensUtil.getScreenHeight());
                    removeAllViews();
                } else if ((this.getScrollY() < 0) && (this.getScrollY() > -DISTANCE_TO_SLIDE)) {
                    this.smoothScrollTo(0);
                    break;
                } else if (this.getScrollY() < -DISTANCE_TO_SLIDE) {
                    this.smoothScrollTo(-DimensUtil.getScreenHeight());
                    removeAllViews();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                }
                break;

        }
        return true;
    }


  private void initView(View view) {
        mViewCloseBtn = view.findViewById(R.id.view_close_btn);
        mTvTitle = view.findViewById(R.id.tv_book_title);
        mTvContent = view.findViewById(R.id.tv_content);
        mBtnFeedback = view.findViewById(R.id.btn_feedback);
        mViewCloseBtn.setOnClickListener(v -> onViewClicked(v));
        mBtnFeedback.setOnClickListener(v -> onViewClicked(v));
    }
}
