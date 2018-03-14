package net.muxi.huashiapp.util;

import android.widget.TextView;

/**
 * Created by ybao on 17/2/22.
 */

public class MyTextUtil {

    public static boolean checkIsEllipsized(TextView textView,String str){
        boolean isEllipsize = !((textView.getLayout().getText().toString()).equals(str));
        return isEllipsize;
    }

//    public <T> static void List<T>(T[] t){

///    }
}
