package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.widget.RelativeLayout;

import net.muxi.huashiapp.common.util.DimensUtil;

/**
 * Created by ybao on 16/9/20.
 * 指引的背景 view
 */
public class GuideBgLayout extends RelativeLayout{

    private static final int RADIUS = DimensUtil.dp2px(24);
    private static final int DISTANCE = DimensUtil.dp2px(28);

    public GuideBgLayout(Context context) {
        super(context);
        setBackgroundColor(Color.BLACK);
        setAlpha(0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(DimensUtil.getScreenWidth() - DISTANCE, DISTANCE, RADIUS, p);
    }
}
