package net.muxi.huashiapp.ui.schedule;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by ybao on 17/1/31.
 */

public class WeekEditView extends RelativeLayout{

    public WeekEditView(Context context) {
        super(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

    }

    public WeekEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
