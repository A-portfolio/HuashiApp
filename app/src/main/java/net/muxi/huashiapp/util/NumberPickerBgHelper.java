package net.muxi.huashiapp.util;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;

/**
 * Created by ybao on 17/2/4.
 * 用户绘制 NumberPicker 控件背景的工具类
 */

public class NumberPickerBgHelper {

    public static final int START_LINE_WIDTH = DimensUtil.dp2px(8);

    public static void drawVerticalPickerBg(Canvas canvas,int width,int height){

        int lineColor = App.sContext.getResources().getColor(R.color.divider);

        Path path = new Path();

        path.moveTo(0,0);
        path.lineTo(width,0);
        path.lineTo(width,height);
        path.lineTo(0,height);

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(lineColor);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(DimensUtil.dp2px(1));
        canvas.drawPath(path,p);

        Path path1 = new Path();

        path1.moveTo(START_LINE_WIDTH / 2,0);
        path1.lineTo(START_LINE_WIDTH / 2,height / 3);
        path1.moveTo(START_LINE_WIDTH / 2,height * 2 / 3);
        path1.lineTo(START_LINE_WIDTH / 2,height);
        p.setStrokeWidth(START_LINE_WIDTH);
        canvas.drawPath(path1,p);

        Path path2 = new Path();
        path2.moveTo(START_LINE_WIDTH / 2,height /3);
        path2.lineTo(START_LINE_WIDTH / 2,height * 2 /3);
        p.setColor(App.sContext.getResources().getColor(R.color.colorAccent));
        canvas.drawPath(path2,p);

        Path path3 = new Path();
        path3.moveTo(START_LINE_WIDTH,height /3);
        path3.lineTo(width,height/3);
        path3.lineTo(width,height*2/3);
        path3.lineTo(START_LINE_WIDTH,height*2/3);
        p.setColor(lineColor);
        p.setStyle(Paint.Style.FILL);
        canvas.drawPath(path3,p);

    }

}
