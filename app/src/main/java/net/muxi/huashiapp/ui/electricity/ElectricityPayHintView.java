package net.muxi.huashiapp.ui.electricity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.util.DimensUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static net.muxi.huashiapp.widget.BaseDetailLayout.DISTANCE_TO_SLIDE;

/**
 * Created by december on 17/3/2.
 */

public class ElectricityPayHintView extends RelativeLayout {

    @BindView(R.id.view_close_btn)
    ImageView mViewCloseBtn;
    @BindView(R.id.tv_copy)
    TextView mTvCopy;
    @BindView(R.id.tv_name)
    TextView mTvName;

    private Context mContext;

    private int mYDown;
    private int mYDistance;
    private int mCurY;

    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;

    public ElectricityPayHintView(Context context) {
        super(context);
        mContext = context;

        mScroller = new Scroller(context);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_electricity_pay_hint, this, true);
        ButterKnife.bind(this, view);

    }

    public void smoothScrollTo(int y) {
        int curY = getScrollY();
        int delatY = y - curY;
        mScroller.startScroll(0, curY, 0, delatY);
        invalidate();
    }

    @OnClick({R.id.view_close_btn, R.id.tv_copy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_close_btn:
                ((BaseActivity) mContext).onBackPressed();
                break;
            case R.id.tv_copy:
                ClipboardManager manager = (ClipboardManager) getContext()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText(null, mTvName.getText()));
                if (manager.hasPrimaryClip()) {
                    manager.getPrimaryClip().getItemAt(0).getText();
                }
                ((BaseActivity) mContext).showSnackbarShort("成功复制到粘贴板");
                break;
        }
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
}
