package net.muxi.huashiapp.library;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.DimensUtil;

/**
 * Created by ybao on 16/5/6.
 */
public class BookDetailView extends ScrollView implements GestureDetector.OnGestureListener {

    public static final int TIME_SLIDE = 200;

    private RelativeLayout detailLayout;
    private RelativeLayout backgroundLayout;

    // 手指按下去时的 y 坐标
    private int my;
    // 移动时当前的 y坐标
    private int curY;

    //上次监听的scrollY值
    private int lastScrollY;

    private ActionBar mActionBar;
    private Toolbar toolbar;
    private Context mContext;

    private GestureDetector mGestureDetector;
    private OnScrollListener mOnScrollListener;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int scrollY = BookDetailView.this.getScrollY();
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 50);
            }
            if (mOnScrollListener != null) {
                mOnScrollListener.onScroll(scrollY);
            }
        }
    };

    private FrameLayout contentLayout;

    private VelocityTracker mVelocityTracker;

    public BookDetailView(Context context) {
        this(context, null);
    }

    public BookDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScrollView(context);
        detailLayout = new RelativeLayout(context);
        mContext = context;

        mGestureDetector = new GestureDetector(this);
    }

    private void initScrollView(Context context) {

        //设置 scrollview 的完全透明,以看到后面的阴影
        View view = LayoutInflater.from(context).inflate(R.layout.view_book_detail, detailLayout, false);
        backgroundLayout = (RelativeLayout) view.findViewById(R.id.background_layout);
        backgroundLayout.setPadding(0, DimensUtil.getScreenHeight() + DimensUtil.getActionbarHeight() + LibraryActivity.ACTIONBAR_DISTANCE, 0, 0);
        backgroundLayout.setBackgroundColor(Color.alpha(Color.BLACK));
        detailLayout = (RelativeLayout) view.findViewById(R.id.detail_layout);
        detailLayout.setBackgroundColor(Color.WHITE);
        FrameLayout.LayoutParams viewParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

//        viewParams.setMargins(0,DimensUtil.getScreenHeight()/3 + DimensUtil.getActionbarHeight() + LibraryActivity.ACTIONBAR_DISTANCE, 0, 0);
//        view.setPadding(0,DimensUtil.getScreenHeight() + DimensUtil.getActionbarHeight() + LibraryActivity.ACTIONBAR_DISTANCE ,0,0);;

        addView(view, viewParams);
        scrollTo(0, DimensUtil.getScreenHeight());
    }


    public RelativeLayout getDetailLayout() {
        return detailLayout;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        mGestureDetector.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                my = (int)ev.getY();
                if (getScrollY() == DimensUtil.getScreenHeight()) {
                    LibraryActivity.detailState = LibraryActivity.APPEAR_TOP;
                }
                if (getScrollY() < DimensUtil.getScreenHeight() + DimensUtil.getActionbarHeight() + LibraryActivity.ACTIONBAR_DISTANCE
                        && getScrollY() > DimensUtil.getScreenHeight()) {
                    LibraryActivity.detailState = LibraryActivity.SLIDE_DOWN;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (getScrollY() < DimensUtil.getScreenHeight()){
                    LibraryActivity.detailState = LibraryActivity.APPEAR_TOP;
                }
//                curY = (int)ev.getY();
//                if (LibraryActivity.detailState == LibraryActivity.APPEAR_TOP &&
//                        curY - my > 0){
//
//                }
            case MotionEvent.ACTION_UP:
                if (getScrollY() < DimensUtil.getScreenHeight() - DimensUtil.getActionbarHeight()){
                    App.getCurrentActivity().onBackPressed();
                }else if (getScrollY() > DimensUtil.getScreenHeight() - DimensUtil.getActionbarHeight()
                        && getScrollY() < DimensUtil.getScreenHeight()){
                    smoothScrollTo(0,DimensUtil.getScreenHeight());
                }
                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 50);
                break;

        }

        return super.onTouchEvent(ev);
    }

    private void slideDownDetail() {
        App.getCurrentActivity().onBackPressed();
    }

    private void slideUpScrollView() {
        this.scrollTo(0, 0);
    }

    private void initToolbar() {
        toolbar = new Toolbar(mContext);
        RelativeLayout.LayoutParams toolbarParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        toolbar.setTitle("详情");
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.delete_edit_login));
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getCurrentActivity().onBackPressed();

            }
        });
        toolbarParams.setMargins(0, -DimensUtil.getActionbarHeight(), 0, 0);
        contentLayout = (FrameLayout) App.getCurrentActivity().findViewById(android.R.id.content);
        contentLayout.addView(toolbar, toolbarParams);

    }

    @Override
    public void fling(int velocityY) {
        //当详情页不处于初始位置时,快速下拉手势下将其移动到初始位置
        if (velocityY < -200 && LibraryActivity.detailState != LibraryActivity.APPEAR_TOP) {
            smoothScrollTo(0, DimensUtil.getScreenHeight());
        } else if (velocityY < -200 && LibraryActivity.detailState == LibraryActivity.APPEAR_TOP){
            App.getCurrentActivity().onBackPressed();
        }else {
            super.fling(velocityY);
        }
    }


    // 将详情页滑动至底端,在退出详情页的时候调用
    public void slideDetailLayoutBottom(){
        smoothScrollTo(0,0);
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    public void removeToolbar() {
        removeView(toolbar);
    }


    private void slideDownToolbar() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,
                -DimensUtil.getActionbarHeight(), 0);
        translateAnimation.setDuration(TIME_SLIDE);
        translateAnimation.setFillAfter(true);
        toolbar.startAnimation(translateAnimation);
    }


    private void slideUpToolbar() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,
                0, -DimensUtil.getActionbarHeight());
        translateAnimation.setDuration(TIME_SLIDE);
        translateAnimation.setFillBefore(true);
        translateAnimation.setFillAfter(true);
        toolbar.startAnimation(translateAnimation);
    }


    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }


    public interface OnScrollListener {
        public void onScroll(int scrollY);
    }


}
