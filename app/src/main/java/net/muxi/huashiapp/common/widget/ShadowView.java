package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ybao on 16/5/7.
 */
public class ShadowView extends View {

    public ShadowView(Context context) {
        this(context,null);
    }

    public ShadowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.BLACK);
        setAlpha(0.7f);
    }


}
