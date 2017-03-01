package net.muxi.huashiapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;

import net.muxi.huashiapp.R;

import java.util.List;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/3/1
 */

public class BottomPickerDialog extends Dialog{

    public BottomPickerDialog(Context context) {
        super(context, R.style.BottomDialogStyle);
        initView();
    }

    private void initView() {
    }

    public BottomPickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }
}
