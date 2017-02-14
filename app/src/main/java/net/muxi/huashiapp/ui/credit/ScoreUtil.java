package net.muxi.huashiapp.ui.credit;

/**
 * Created by ybao on 17/2/10.
 */

public class ScoreUtil {

    /**
     * 专业必修课  -> 专业课必修
     * @param s
     * @return
     */
    public static String toCategory(String s){
        if (s != null && s.length() > 4){
            return s.substring(0,2) + "课" + s.substring(2,4);
        }
        return null;
    }

    public static String toCate1(String s){
        if (s != null && s.length() > 4){
            return s.substring(0,2) + "课";
        }
        return null;
    }

    public static String toCate2(String s){
        if (s != null && s.length() > 4){
            return s.substring(2,4);
        }
        return null;
    }
}
