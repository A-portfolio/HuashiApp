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


    //第二个参数是要从哪里开始换行,如果长度小于index 则不处理
    public static String getStringWithNextLine(String string,int index){
      if(string.length()>index){
        String pieces[] = new String[2];
        pieces[0] = string.substring(0,index)+"\n";
        pieces[1] = string.substring(index,string.length());
        return pieces[0]  + pieces[1];
      }else{
        return string;
      }
    }


    //public static String

//    public <T> static void List<T>(T[] t){

///    }
}
