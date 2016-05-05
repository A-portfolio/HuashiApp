package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

import net.muxi.huashiapp.R;

/**
 * Created by ybao on 16/5/1.
 * 用于登录界面的编辑框
 */
public class SelectEditText extends EditText{

    private Drawable mDrawable;

    public SelectEditText(Context context) {
        super(context);
    }

    public SelectEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDrawable = getResources().getDrawable(R.drawable.delete_edit_login);

    }





}
