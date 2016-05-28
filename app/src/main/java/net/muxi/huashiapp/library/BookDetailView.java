package net.muxi.huashiapp.library;

/**
 * Created by ybao on 16/5/6.
// */
//public class BookDetailView extends ScrollView {
//
//
//    private RelativeLayout detailLayout;
//    private RelativeLayout backgroundLayout;
//
//    // 手指按下去时的 y 坐标
//    private int my;
//    // 移动时当前的 y坐标
//    private int curY;
//
//    //上次监听的scrollY值
//    private int lastScrollY;
//
//    //详情页下滑到底部的时间
//    public static final int TIME_SLIDE_BOTTOM = 400;
//
//    private ActionBar mActionBar;
//    private Toolbar toolbar;
//    private Context mContext;
//
//    private OnScrollListener mOnScrollListener;
//
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            int scrollY = BookDetailView.this.getScrollY();
//            if (lastScrollY != scrollY) {
//                lastScrollY = scrollY;
//                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 50);
//            }
//            if (mOnScrollListener != null) {
//                mOnScrollListener.onScroll(scrollY);
//            }
//        }
//    };
//
//    private FrameLayout contentLayout;
//
//    private VelocityTracker mVelocityTracker;
//
//    public BookDetailView(Context context) {
//        this(context, null);
//    }
//
//    public BookDetailView(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public BookDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initScrollView(context);
//        detailLayout = new RelativeLayout(context);
//        mContext = context;
//
//    }
//
//    private void initScrollView(Context context) {
//
//        //设置 scrollview 的完全透明,以看到后面的阴影
//        View view = LayoutInflater.from(context).inflate(R.layout.view_book_detail, detailLayout, false);
//        backgroundLayout = (RelativeLayout) view.findViewById(R.id.background_layout);
//        backgroundLayout.setPadding(0, DimensUtil.getScreenHeight() + DimensUtil.getActionbarHeight() + LibraryActivity.ACTIONBAR_DISTANCE, 0, 0);
//        backgroundLayout.setBackgroundColor(Color.alpha(Color.BLACK));
//        detailLayout = (RelativeLayout) view.findViewById(R.id.detail_layout);
//        detailLayout.setBackgroundColor(Color.WHITE);
//        FrameLayout.LayoutParams viewParams = new LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//        );
//
////        viewParams.setMargins(0,DimensUtil.getScreenHeight()/3 + DimensUtil.getActionbarHeight() + LibraryActivity.ACTIONBAR_DISTANCE, 0, 0);
////        view.setPadding(0,DimensUtil.getScreenHeight() + DimensUtil.getActionbarHeight() + LibraryActivity.ACTIONBAR_DISTANCE ,0,0);;
//
//        addView(view, viewParams);
//    }
//
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        if (mOnScrollListener != null) {
//            mOnScrollListener.onScroll(lastScrollY = this.getScrollY());
//        }
//        int action = MotionEventCompat.getActionMasked(ev);
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                my = (int) ev.getY();
//                if (getScrollY() == DimensUtil.getScreenHeight()) {
//                    LibraryActivity.detailState = LibraryActivity.APPEAR_TOP;
//                }
//                if (getScrollY() < DimensUtil.getScreenHeight() + DimensUtil.getActionbarHeight() + LibraryActivity.ACTIONBAR_DISTANCE
//                        && getScrollY() > DimensUtil.getScreenHeight()) {
//                    LibraryActivity.detailState = LibraryActivity.SLIDE_DOWN;
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (getScrollY() < DimensUtil.getScreenHeight()) {
//                    LibraryActivity.detailState = LibraryActivity.APPEAR_TOP;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                if (getScrollY() < DimensUtil.getScreenHeight() - DimensUtil.getActionbarHeight() - LibraryActivity.ACTIONBAR_DISTANCE) {
//                    App.getCurrentActivity().onBackPressed();
//                }
//                if (getScrollY() > DimensUtil.getScreenHeight() - DimensUtil.getActionbarHeight() - LibraryActivity.ACTIONBAR_DISTANCE
//                        && getScrollY() < DimensUtil.getScreenHeight()) {
//                    this.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            smoothScrollTo(0, DimensUtil.getScreenHeight());
//                        }
//                    });
//                }
//                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 50);
//                break;
//        }
//
//        return super.onTouchEvent(ev);
//    }
//
//
//    @Override
//    public void fling(int velocityY) {
//        //当详情页不处于初始位置时,快速下拉手势下将其移动到初始位置
//        if (velocityY < -5000 && LibraryActivity.detailState != LibraryActivity.APPEAR_TOP) {
//            smoothScrollTo(0, DimensUtil.getScreenHeight());
//        } else if (velocityY < -5000 && LibraryActivity.detailState == LibraryActivity.APPEAR_TOP) {
//            App.getCurrentActivity().onBackPressed();
//        } else {
//            super.fling(velocityY);
//        }
//    }
//
//
//    // 将详情页滑动至底端,在退出详情页的时候调用
//    public void slideDetailLayoutBottom() {
//        smoothScrollTo(0, DimensUtil.getActionbarHeight() + LibraryActivity.ACTIONBAR_DISTANCE);
////        TranslateAnimation animation = new TranslateAnimation(
////                0,
////                0,
////                DimensUtil.getActionbarHeight() + LibraryActivity.ACTIONBAR_DISTANCE,
////                DimensUtil.getScreenHeight());
////        animation.setDuration(TIME_SLIDE_BOTTOM);
////        animation.setFillAfter(true);
////        detailLayout.startAnimation(animation);
//    }
//
//
//    public void setOnScrollListener(OnScrollListener onScrollListener) {
//        this.mOnScrollListener = onScrollListener;
//    }
//
//
//    public interface OnScrollListener {
//        public void onScroll(int scrollY);
//    }
//
//
//}
