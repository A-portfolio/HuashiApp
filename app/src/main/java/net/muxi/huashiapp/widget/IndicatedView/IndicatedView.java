package net.muxi.huashiapp.widget.IndicatedView;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.common.util.DimensUtil;
import com.muxistudio.common.util.Logger;

import net.muxi.huashiapp.R;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/3/3
 *
 * 使用
 * IndicatedView indicatedView = new IndicatedView(getContext());
 * indicatedView.setTipViewText("设置当前周也可以点这里噢");
 * TipViewUtil.addToContent(getContext(),indicatedView,DIRECTION_DOWN,DimensUtil.getScreenWidth() - DimensUtil.dp2px(38),DimensUtil.dp2px(38));
 */

public class IndicatedView extends FrameLayout {

    private View tipView;
    private ImageView mImageView;

    public static final int DIRECTION_DOWN = 0;
    public static final int DIRECTION_UP = 1;
    public static final int SIDE_MARGIN = DimensUtil.dp2px(16);
    public static final int LINE_MARGIN_VERTICAL = DimensUtil.dp2px(48);
    public static final int LINE_HEIGHT = DimensUtil.dp2px(42);

    private int tipViewWidth;
    private int tipViewHeight;

    private LayoutParams mLayoutParams;

    private int direction;
    private int x, y;

    public IndicatedView(Context context) {
        super(context);
        tipView = LayoutInflater.from(context).inflate(R.layout.view_indicated, this,false);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addView(tipView, params);
        tipViewWidth = tipView.getWidth();
        tipViewHeight = tipView.getHeight();
        final ViewTreeObserver observer = tipView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (tipView.getViewTreeObserver().isAlive()) {
                    tipViewHeight = tipView.getHeight();
                    tipViewWidth = tipView.getWidth();
                    tipView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        tipView.findViewById(R.id.iv_close).setOnClickListener(v -> {
            this.setVisibility(GONE);
        });
    }

    public void setTipViewText(String s) {
        ((TextView) tipView.findViewById(R.id.tv_indicate)).setText(s);
    }

    /**
     * 添加 IndicatedView的位置, viewgroup 需为 FrameLayout
     */
    public void setIndicatedViewPosition(int x, int y, int direction) {
        this.direction = direction;
        mImageView = new ImageView(getContext());
        mImageView.setImageResource(R.drawable.line_indicator);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        FrameLayout.LayoutParams ivParams = new LayoutParams(DimensUtil.dp2px(15),
                LINE_HEIGHT);
        FrameLayout.LayoutParams indicatedParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        Logger.d(tipViewHeight + "");
        if (direction == DIRECTION_UP) {
            mImageView.setRotation(180);
            if (y > DimensUtil.getScreenWidth() / 2) {
                ivParams.gravity = Gravity.RIGHT;
                ivParams.setMargins(0, LINE_MARGIN_VERTICAL, SIDE_MARGIN, 0);
                indicatedParams.setMargins(x - (tipView.getWidth() - DimensUtil.dp2px(7)),
                        y - indicatedParams.height, 0, 0);
            } else {
                ivParams.gravity = Gravity.LEFT;
                ivParams.setMargins(SIDE_MARGIN, LINE_MARGIN_VERTICAL, 0, 0);
                indicatedParams.setMargins(x - DimensUtil.dp2px(7) - SIDE_MARGIN,
                        y - indicatedParams.height, 0, 0);
            }
        } else {
            if (y > DimensUtil.getScreenWidth() / 2) {
                ivParams.gravity = Gravity.TOP | Gravity.RIGHT;
                ivParams.setMargins(0, 0, SIDE_MARGIN, LINE_MARGIN_VERTICAL);
                indicatedParams.setMargins(
                        x - (tipView.getWidth() - DimensUtil.dp2px(7) - SIDE_MARGIN), y, 0, 0);
            } else {
                ivParams.gravity = Gravity.TOP | Gravity.LEFT;
                ivParams.setMargins(SIDE_MARGIN, 0, 0, LINE_MARGIN_VERTICAL);
                indicatedParams.setMargins(x - DimensUtil.dp2px(7) - SIDE_MARGIN, y, 0, 0);
            }
            ((LayoutParams) tipView.getLayoutParams()).gravity = Gravity.BOTTOM;

        }
        addView(mImageView, ivParams);

        Logger.d(indicatedParams.leftMargin + " " + indicatedParams.topMargin + " "
                + indicatedParams.rightMargin + " " + indicatedParams.bottomMargin);
        ((ViewGroup) ((AppCompatActivity) getContext()).getWindow().findViewById(
                android.R.id.content)).addView(this, indicatedParams);

        post(() -> {
           mLayoutParams = (FrameLayout.LayoutParams)getLayoutParams();
//            mLayoutParams.setMargins();
        });
        postDelayed(() -> {
            if (this.getVisibility() != GONE) {
                this.setVisibility(GONE);
            }
        }, 3000);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


    }

    public static class Builder {

        private Context mContext;
        private String title;
        private int x, y;
        private int direction;

        public Builder(Context context) {
            mContext = context;
        }

        public void setTipText(String s) {
            title = s;
        }


        public void build() {

        }
    }

//        public }

}
