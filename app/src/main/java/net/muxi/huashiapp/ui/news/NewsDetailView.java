package net.muxi.huashiapp.ui.news;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.muxistudio.appcommon.data.News;
import com.muxistudio.common.util.DimensUtil;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import net.muxi.huashiapp.R;

import java.util.List;

import static net.muxi.huashiapp.widget.BaseDetailLayout.DISTANCE_TO_SLIDE;

/**
 * Created by december on 16/7/29.
 */
public class NewsDetailView extends RelativeLayout {

    private Context mContext;
    private List<News> mNewsList;
    int mPosition;

    private int mYDown;
    private int mYDistance;
    private int mCurY;

    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;
    private RelativeLayout mBackgroundLayout;
    private ImageView mNewsFloatBtn;
    private WebView mNewsContent;

    public NewsDetailView(Context context, List<News> news, int position) {
        super(context);
        mContext = context;
        mNewsList = news;
        mPosition = position;

        mScroller = new Scroller(context);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_news_detail, this, true);
        initView(view);
        initWebView();

        mNewsFloatBtn.setOnClickListener(v -> removeAllViews());

    }

    private void initWebView() {
        WebSettings webSettings = mNewsContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadsImagesAutomatically(false);
        webSettings.setTextSize(WebSettings.TextSize.SMALLER);


        mNewsContent.loadData(mNewsList.get(mPosition).getContent(), "text/html; charset=UTF-8", null);


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
        mBackgroundLayout = view.findViewById(R.id.background_layout);
        mNewsFloatBtn = view.findViewById(R.id.news_float_btn);
        mNewsContent = view.findViewById(R.id.news_content);
    }


}