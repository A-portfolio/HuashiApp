package net.muxi.huashiapp.ui.news;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.News;
import net.muxi.huashiapp.util.DimensUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static net.muxi.huashiapp.widget.BaseDetailLayout.DISTANCE_TO_SLIDE;

/**
 * Created by december on 16/7/29.
 */
public class NewsDetailView extends RelativeLayout {

    @BindView(R.id.news_float_btn)
    ImageView mNewsFloatBtn;
    @BindView(R.id.news_title)
    TextView mNewsTitle;
    @BindView(R.id.news_date)
    TextView mNewsDate;
    @BindView(R.id.news_content)
    WebView mNewsContent;


    private Context mContext;
    private List<News> mNewsList;
    int mPosition;

    private int mYDown;
    private int mYDistance;
    private int mCurY;

    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;

    public NewsDetailView(Context context, List<News> news, int position) {
        super(context);
        mContext = context;
        mNewsList = news;
        mPosition = position;

        mScroller = new Scroller(context);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_news_detail, this, true);
        ButterKnife.bind(this);
        mNewsTitle.setText(mNewsList.get(mPosition).getTitle());

        mNewsDate.setText(mNewsList.get(mPosition).getDate());
        initWebView();
//        addAppendix();
//        mNewsLink.setText(mNewsList.get(mPosition).getAppendix_list().toString());

        mNewsFloatBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) mContext).onBackPressed();
            }
        });

    }

    private void initWebView() {
        WebSettings webSettings = mNewsContent.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setLoadsImagesAutomatically(false);

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
                    ((BaseActivity) mContext).onBackPressed();
                } else if ((this.getScrollY() < 0) && (this.getScrollY() > -DISTANCE_TO_SLIDE)) {
                    this.smoothScrollTo(0);
                    break;
                } else if (this.getScrollY() < -DISTANCE_TO_SLIDE) {
                    this.smoothScrollTo(-DimensUtil.getScreenHeight());
                    ((BaseActivity) mContext).onBackPressed();
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

//    private void addAppendix() {
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//        int appendixNum = mNewsList.get(mPosition).getAppendix_list().size();
//        final List<String> appendix = mNewsList.get(mPosition).getAppendix_list();
//        TextView[] textViews = new TextView[appendixNum];
//        if (appendixNum == 0) {
//            mTvAppendix.setVisibility(GONE);
//        }
//        Logger.d(appendixNum + "");
//        for (int i = 0; i < appendixNum; i++) {
//            final int j = i;
//            textViews[i] = new TextView(mContext);
//            textViews[i].setText(appendix.get(i).toString());
//            textViews[i].getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//            textViews[i].setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
//
//            textViews[i].setAutoLinkMask(Linkify.WEB_URLS);
//            textViews[i].setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appendix.get(j).toString()));
//                        mContext.startActivity(browserIntent);
//                        Intent intent = WebViewActivity.newIntent(mContext, appendix.get(j).toString());
//                        mContext.startActivity(intent);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        ToastUtil.showShort(App.sContext.getString(R.string.tip_link_unable_open));
//                    }
//                }
//            });
//            textViews[i].setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, DownloadService.class);
//                    intent.putExtra("fileType", "file");
//                    intent.putExtra("url", appendix.get(j));
//                    intent.putExtra("fileName", appendix.get(j));
//                    mContext.startService(intent);
//                }
//            });

//            mLinkLayout.addView(textViews[i], params);
//        }
//    }

}