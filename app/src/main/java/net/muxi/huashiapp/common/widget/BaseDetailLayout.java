package net.muxi.huashiapp.common.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.util.DimensUtil;

import butterknife.Bind;

/**
 * Created by ybao on 16/5/20.
 */
public class BaseDetailLayout extends FrameLayout {

    @Bind(R.id.bg_layout)
    RelativeLayout mBgLayout;
    @Bind(R.id.scroll_view)
    ScrollView mScrollView;
    @Bind(R.id.detail_content_layout)
    ContentLayout mDetailContentLayout;

    private BaseActivity mContext;

    private View view;

    private int mYDown;
    private int mYDistance;
    private int mCurY;
    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;

    private Toolbar mToolbar;

    //当scrollView 处于初始状态也就是scrollY为0的状态
    public static final int TOP_STATE = 0;
    //toolbar 所处的状态
    public static final int TOOLBAR_APPEAR = 1;
    public static final int TOOLBAR_NOT_APPEAR = 2;
    //当移除详情页的时候的状态
    public static final int REMOVE_TOOLBAR = 3;

    private int toolbarState = TOP_STATE;

    public static final int DURATION_ANIMATION = 250;
    //能够下拉的的最小滑动距离
    public static final int DISTANCE_TO_SLIDE = DimensUtil.dp2px(80);

    public BaseDetailLayout(BaseActivity context) {
        this(context, null);
    }

    public BaseDetailLayout(BaseActivity context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        view = LayoutInflater.from(context).inflate(R.layout.view_base_detail, this, false);
        this.addView(view);

        initToolbar(context);

        mScroller = new Scroller(context);
        mScrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        mDetailContentLayout = (ContentLayout) view.findViewById(R.id.detail_content_layout);

        mScrollView.setVerticalScrollBarEnabled(false);
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (mScrollView.getScrollY() > 2 * DimensUtil.getActionbarHeight() &&
                        toolbarState != TOOLBAR_APPEAR &&
                        toolbarState != REMOVE_TOOLBAR) {
                    slideDownToolabr();
                }
                if (mScrollView.getScrollY() < 2 * DimensUtil.getActionbarHeight()
                        && toolbarState == TOOLBAR_APPEAR) {
                    slideUpToolbar();

                }
            }
        });

    }


    //添加自定义布局在控件里面
    public void setContent(ViewGroup viewGroup) {
        ViewGroup.LayoutParams contentParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mDetailContentLayout.addView(viewGroup, contentParams);
    }


    //在退出详情页的时候调用
    public void slideContentView() {
        if (toolbarState == TOOLBAR_APPEAR) {
            slideUpToolbar();
        }
        toolbarState = REMOVE_TOOLBAR;
        mDetailContentLayout.smoothScrollTo(-mScrollView.getScrollY() - DimensUtil.getScreenHeight());

    }


    public void addToolbar() {
        FrameLayout.LayoutParams barParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DimensUtil.getActionbarHeight()
        );
        barParams.setMargins(0, -DimensUtil.getActionbarHeight(), 0, 0);
        this.addView(mToolbar, barParams);
    }


    private void slideUpToolbar() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mToolbar, "y", 0, -DimensUtil.getActionbarHeight());
        animator.setDuration(DURATION_ANIMATION);
        animator.start();
        toolbarState = TOOLBAR_NOT_APPEAR;
    }


    private void slideDownToolabr() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mToolbar, "y", -DimensUtil.getActionbarHeight(), 0);
        animator.setDuration(DURATION_ANIMATION);
        animator.start();
        toolbarState = TOOLBAR_APPEAR;
    }


    private void initToolbar(final Context context) {
        mToolbar = new Toolbar(context);
        mToolbar.setTitle("详情");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mToolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setMinimumHeight(DimensUtil.getActionbarHeight());
        mToolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                slideContentView();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BaseDetailLayout.this.removeView(mToolbar);
                    }
                }, DURATION_ANIMATION);
                mContext.onBackPressed();
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, -DimensUtil.getActionbarHeight(), 0, 0);

    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            this.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void smoothScrollTo(int y) {
        int curY = getScrollY();
        int delatY = y - curY;
        mScroller.startScroll(0, curY, 0, delatY);
        invalidate();
    }


    //在滑到详情页顶端的时候拦截触摸事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            return false;
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mYDown = (int) ev.getY();
                if (mToolbar.getParent() == null) {
                    this.addToolbar();
                }
                break;
            case MotionEvent.ACTION_MOVE:

                mYDistance = (int) ev.getY() - mYDown;
                mCurY = (int) ev.getY();
                if (isTop() && mYDistance > 0) {
                    if (mToolbar.getParent() != null) {
                        this.removeView(mToolbar);
                    }
                    return true;
                }
                break;
        }
        return false;
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
                    this.removeView(mToolbar);
                    mContext.onBackPressed();
                } else if ((this.getScrollY() < 0) && (this.getScrollY() > -DISTANCE_TO_SLIDE)) {
                    this.smoothScrollTo(0);
                    break;
                } else if (this.getScrollY() < -DISTANCE_TO_SLIDE) {
                    this.smoothScrollTo(-DimensUtil.getScreenHeight());
                    this.removeView(mToolbar);
                    mContext.onBackPressed();
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


    private boolean isTop() {
        if (mScrollView.getScrollY() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    //在 toolbar 的状态改变的时候回调
    public interface OnStateChangeListener {
        public void onStateChange();
    }

}
