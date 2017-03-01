package net.muxi.huashiapp.widget.IndicatedView;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/28
 */

public class IndicatedView extends FrameLayout {

    private View tipView;
    private ImageView mImageView;

    public static final int DIRECTION_DOWN = 0;
    public static final int DIRECTION_UP = 1;
    public static final int SIDE_MARGIN = DimensUtil.dp2px(20);
    public static final int TIP_VIEW_HEIGHT = DimensUtil.dp2px(48);
    public static final int LINE_HEIGHT = DimensUtil.dp2px(42);
    public static final int DOWN_LINE_MARGIN_BOTTOM = DimensUtil.dp2px(10);

    private int tipViewWidth;
    private int tipViewHeight;

    private int direction;

    public IndicatedView(Context context) {
        super(context);

        tipView = LayoutInflater.from(context).inflate(R.layout.view_indicated, this, false);
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
    public void setIndicatedViewPosition(int x, int y, ViewGroup view, int direction) {
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
                ivParams.setMargins(0, TIP_VIEW_HEIGHT, SIDE_MARGIN, 0);
                indicatedParams.setMargins(x - (tipView.getWidth() - DimensUtil.dp2px(7)),
                        y - indicatedParams.height, 0, 0);
            } else {
                ivParams.gravity = Gravity.LEFT;
                ivParams.setMargins(SIDE_MARGIN, TIP_VIEW_HEIGHT, 0, 0);
                indicatedParams.setMargins(x - DimensUtil.dp2px(7) - SIDE_MARGIN,
                        y - indicatedParams.height, 0, 0);
            }
        } else {
            if (y > DimensUtil.getScreenWidth() / 2) {
                ivParams.gravity = Gravity.TOP | Gravity.RIGHT;
                ivParams.setMargins(0, 0, SIDE_MARGIN, DOWN_LINE_MARGIN_BOTTOM);
                indicatedParams.setMargins(
                        x - (tipView.getWidth() - DimensUtil.dp2px(7) - SIDE_MARGIN), y, 0, 0);
            } else {
                ivParams.gravity = Gravity.TOP | Gravity.LEFT;
                ivParams.setMargins(SIDE_MARGIN, 0, 0, DOWN_LINE_MARGIN_BOTTOM);
                indicatedParams.setMargins(x - DimensUtil.dp2px(7) - SIDE_MARGIN, y, 0, 0);
            }
            ((FrameLayout.LayoutParams) tipView.getLayoutParams()).setMargins(0,
                    LINE_HEIGHT, 0, 0);
        }
        addView(mImageView, ivParams);

        Logger.d(indicatedParams.leftMargin + " " + indicatedParams.topMargin + " "
                + indicatedParams.rightMargin + " " + indicatedParams.bottomMargin);
        view.addView(this, indicatedParams);

        postDelayed(() -> {
            if (this.getVisibility() != GONE) {
                this.setVisibility(GONE);
            }
        }, 5000);
    }

}
