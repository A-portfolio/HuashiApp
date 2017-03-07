package net.muxi.huashiapp.ui;

import android.content.Context;
import android.content.Intent;

import net.muxi.huashiapp.common.base.ToolbarActivity;

/**
 * Created by december on 17/3/4.
 */

public class FAQActivity extends ToolbarActivity {


    public static void start(Context context){
        Intent starter = new Intent(context,FAQActivity.class);
        context.startActivity(starter);
    }

}
