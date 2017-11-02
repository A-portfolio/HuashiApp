package net.muxi.huashiapp.ui.studyroom;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.SuggestionActivity;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static net.muxi.huashiapp.widget.BaseDetailLayout.DISTANCE_TO_SLIDE;

/**
 * Created by december on 17/2/10.
 */

public class StudyRoomCorrectView extends RelativeLayout {


    @BindView(R.id.btn_feedback)
    Button mBtnFeedback;
    @BindView(R.id.view_close_btn)
    ImageView mViewCloseBtn;


    private Context mContext;

    private String mType;

    private int mYDown;
    private int mYDistance;
    private int mCurY;
    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;

    public StudyRoomCorrectView(Context context) {
        super(context);
        mContext = context;
        Logger.d(mType);
        mScroller = new Scroller(context);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_study_room_correct, this, true);
        ButterKnife.bind(this, view);

    }

    @OnClick({R.id.view_close_btn, R.id.btn_feedback})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_close_btn:
                removeAllViews();
                break;
            case R.id.btn_feedback:
                Intent intent = new Intent();
                intent.setClass(mContext, SuggestionActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                break;
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


}
