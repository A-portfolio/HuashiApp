package net.muxi.huashiapp.util;

import android.text.TextUtils;

import net.muxi.huashiapp.App;

/**
 * Created by ybao on 17/2/9.
 */

public class UserUtil {

    public static String[] generateYear(int length){
        String[] years = new String[length];
        int startYear;
        if (!TextUtils.isEmpty(App.sUser.sid) && App.sUser.sid.length() > 4){
            String yearStr = App.sUser.sid.substring(0,4);
            startYear = Integer.parseInt(yearStr);
            for (int i = 0;i < length;i ++){
                years[i] = String.valueOf(startYear);
                startYear ++;
            }
            return years;
        }
        return null;
    }
}
