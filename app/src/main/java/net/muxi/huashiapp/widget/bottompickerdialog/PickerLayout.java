package net.muxi.huashiapp.widget.bottompickerdialog;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import net.muxi.huashiapp.util.NumberPickerHelper;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/3/1
 */

public class PickerLayout extends RelativeLayout{

    public PickerLayout(Context context) {
        this(context,null);
    }

    public PickerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        NumberPickerHelper.drawVerticalPickerBg(canvas, getWidth(), getHeight());
    }
}
