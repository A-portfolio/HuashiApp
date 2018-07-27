package net.muxi.huashiapp.ui.electricity;

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

import com.muxistudio.appcommon.appbase.BaseAppActivity;
import com.muxistudio.appcommon.utils.AppUtil;
import com.muxistudio.appcommon.utils.UtilsExtensionKt;
import com.muxistudio.common.util.DimensUtil;

import net.muxi.huashiapp.R;

import static net.muxi.huashiapp.widget.BaseDetailLayout.DISTANCE_TO_SLIDE;

/**
 * Created by december on 17/3/2.
 */

public class ElectricityPayHintView extends RelativeLayout {


    private Context mContext;

    private int mCurY;

    private Scroller mScroller;

    private VelocityTracker mVelocityTracker;

    private ElectricityPayHintView mView;
    private TextView mTvName;

    public ElectricityPayHintView(Context context) {
        super(context);
        mContext = context;
        mScroller = new Scroller(context);
        initView();
    }

    private void initView() {
        ImageView mViewCloseBtn = findViewById(R.id.view_close_btn);

        mTvName = findViewById(R.id.tv_name);
        TextView mTvCopy = findViewById(R.id.tv_copy);
        //the prototype of the lambda is setOnclickListener(v -> onClick())
        mViewCloseBtn.setOnClickListener(this::onClick);
        mTvCopy.setOnClickListener(this::onClick);
    }

    public void smoothScrollTo(int y) {
        int curY = getScrollY();
        int delatY = y - curY;
        mScroller.startScroll(0, curY, 0, delatY);
        invalidate();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.view_close_btn){
            removeAllViews();
        }else if (id == R.id.tv_copy){

            AppUtil.clipToClipBoard(getContext(), mTvName.getText());
            ((BaseAppActivity) mContext).showSnackbarShort("成功复制到粘贴板");

            UtilsExtensionKt.intent2Wx(new AppUtil(),this.getContext());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mCurY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                int mYDistance = (int) (event.getY() - mCurY);
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
