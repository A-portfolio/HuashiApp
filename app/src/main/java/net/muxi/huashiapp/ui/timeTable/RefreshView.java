package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/24
 */

public class RefreshView extends RelativeLayout {

    @BindView(R.id.drawee)
    SimpleDraweeView mDrawee;
    TextView mTvTip;
    @BindView(R.id.layout_refresh)
    FrameLayout mLayoutRefresh;

    private DraweeController mDraweeController;

    enum Status {
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING,
        REFRESH_FINISHED,
        READY_TO_PULL
    }

    public Status status = Status.READY_TO_PULL;

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    private void initView() {
        if (!PreferenceUtil.getBoolean(PreferenceUtil.IS_FIRST_ENTER_TABLE, true)) {
            setVisibility(VISIBLE);
        }
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_table_refresh, this);
        ButterKnife.bind(this);
        mTvTip = (TextView) view.findViewById(R.id.tv_tip);

        mDraweeController = Fresco.newDraweeControllerBuilder()
                .setOldController(mDrawee.getController())
                .setUri("")
                .build();
    }
    public void setTipText(String s) {
        mTvTip.setText(s);
    }
    public void startRefresh() {
        mDraweeController = Fresco.newDraweeControllerBuilder()
                .setOldController(mDrawee.getController())
                .setUri("asset://net.muxi.huashiapp/table_loading.gif")
                .setAutoPlayAnimations(true)
                .build();
        mDrawee.setController(mDraweeController);
    }
    public void setPullToRefresh() {
        Uri uri = Uri.parse("res:/" + R.drawable.table_loading_final);
        mDraweeController = Fresco.newDraweeControllerBuilder()
                .setOldController(mDrawee.getController())
                .setUri(uri)
                .build();
        mDrawee.setController(mDraweeController);
        mTvTip.setText(R.string.tip_pull_to_refresh);
        status = Status.PULL_TO_REFRESH;
    }
    public void setReleaseToRefresh() {
        mTvTip.setText(R.string.tip_release_to_refresh);
        status = Status.RELEASE_TO_REFRESH;
    }
    public void setRefreshing() {
        startRefresh();
        mTvTip.setText(R.string.tip_refreshing);
        status = Status.REFRESHING;
    }
    public void setRefreshResult(int stringResource) {
        mTvTip.setText(stringResource);
        slideUpTipView();
        mDraweeController = Fresco.newDraweeControllerBuilder()
                .setOldController(mDrawee.getController())
                .setUri("")
                .build();
        mDrawee.setController(mDraweeController);
        status = Status.REFRESH_FINISHED;
    }
    public void setReadyToPull() {
        status = Status.READY_TO_PULL;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mTvTip.setText(R.string.tip_pull_to_refresh);
                mLayoutRefresh.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                slideDownTipView();
            }
        }, 250);
    }
    public void setRefreshViewBackground(int colorRes) {
        mLayoutRefresh.setBackgroundColor(getResources().getColor(colorRes));
    }
    public void slideUpTipView() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, DimensUtil.dp2px(-5));
        animation.setFillAfter(true);
        animation.setDuration(200);
        mTvTip.startAnimation(animation);
    }
    public void slideDownTipView() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, DimensUtil.dp2px(-5), 0);
        animation.setFillAfter(true);
        animation.setDuration(200);
        mTvTip.startAnimation(animation);
    }
}
