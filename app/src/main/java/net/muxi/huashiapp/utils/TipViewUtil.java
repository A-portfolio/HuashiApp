package net.muxi.huashiapp.utils;

import android.content.Context;
//import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.muxistudio.common.util.DimensUtil;
import com.muxistudio.common.util.Logger;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.IndicatedView.IndicatedView;

import static android.view.View.GONE;
import static net.muxi.huashiapp.widget.IndicatedView.IndicatedView.DIRECTION_UP;
import static net.muxi.huashiapp.widget.IndicatedView.IndicatedView.LINE_HEIGHT;
import static net.muxi.huashiapp.widget.IndicatedView.IndicatedView.LINE_MARGIN_VERTICAL;
import static net.muxi.huashiapp.widget.IndicatedView.IndicatedView.SIDE_MARGIN;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/3/3
 *
 * 使用
 * IndicatedView indicatedView = new IndicatedView(getContext());
 * indicatedView.setTipViewText("设置当前周也可以点这里噢");
 * TipViewUtil.addToContent(getContext(),indicatedView,DIRECTION_DOWN,DimensUtil.getScreenWidth() - DimensUtil.dp2px(38),DimensUtil.dp2px(38));
 */

public class TipViewUtil {

    public static void addToContent(Context context, IndicatedView indicatedView, int direction, int x, int y){

        View tipView = indicatedView.findViewById(R.id.layout_card);
        ImageView mImageView = new ImageView(context);
        mImageView.setImageResource(R.drawable.line_indicator);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        FrameLayout.LayoutParams ivParams = new FrameLayout.LayoutParams(DimensUtil.dp2px(15),
                LINE_HEIGHT);
        FrameLayout.LayoutParams indicatedParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        ((FrameLayout) ((AppCompatActivity) context).getWindow().findViewById(
                android.R.id.content)).addView(indicatedView, indicatedParams);

        tipView.post(() -> {
            if (direction == DIRECTION_UP) {
                mImageView.setRotation(180);
                if (x > DimensUtil.getScreenWidth() / 2) {
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
                if (x > DimensUtil.getScreenWidth() / 2) {
                    ivParams.gravity = Gravity.TOP | Gravity.RIGHT;
                    ivParams.setMargins(0, 0, SIDE_MARGIN, LINE_MARGIN_VERTICAL);
                    indicatedParams.setMargins(
                            x - (tipView.getWidth() - DimensUtil.dp2px(7) - SIDE_MARGIN), y, 0, 0);
                } else {
                    ivParams.gravity = Gravity.TOP | Gravity.LEFT;
                    ivParams.setMargins(SIDE_MARGIN, 0, 0, LINE_MARGIN_VERTICAL);
                    indicatedParams.setMargins(x - DimensUtil.dp2px(7) - SIDE_MARGIN, y, 0, 0);
                }
                ((FrameLayout.LayoutParams) tipView.getLayoutParams()).gravity = Gravity.BOTTOM;

            }
            indicatedView.addView(mImageView, ivParams);
            Logger.d(indicatedParams.leftMargin + " " + indicatedParams.topMargin);
            indicatedView.postDelayed(() -> {
                if (indicatedView.getVisibility() != GONE) {
                    indicatedView.setVisibility(GONE);
                }
            }, 5000);
        });


    }
}
