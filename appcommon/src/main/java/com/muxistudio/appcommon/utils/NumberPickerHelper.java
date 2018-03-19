package com.muxistudio.appcommon.utils;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.widget.NumberPicker;

import com.muxistudio.appcommon.R;
import com.muxistudio.common.base.Global;
import com.muxistudio.common.util.DimensUtil;

import java.lang.reflect.Field;

/**
 * Created by ybao on 17/2/4.
 * 用户绘制 NumberPicker 控件的工具类
 */

public class NumberPickerHelper {

    public static final int START_LINE_WIDTH = DimensUtil.dp2px(8);

    public static void drawVerticalPickerBg(Canvas canvas,int width,int height){
        int lineColor = Global.getApplication().getResources().getColor(R.color.divider);
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
        p.setColor(Global.getApplication().getResources().getColor(R.color.colorAccent));
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

    public static void setDividerColor(NumberPicker picker, int color) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
