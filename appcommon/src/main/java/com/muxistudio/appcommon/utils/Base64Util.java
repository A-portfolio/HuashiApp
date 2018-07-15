package com.muxistudio.appcommon.utils;

import android.util.Base64;

import com.muxistudio.appcommon.data.User;

/**
 * Created by ybao on 16/6/21.
 */
public class Base64Util{

    public static String decrypt(User user){
        String verification = user.getSid() + ":" + user.getPassword();
        return new String(Base64.encodeToString(verification.getBytes(),Base64.NO_WRAP));
    }

    //在前端添加Basic,生成 Base 加密字符串
    public static String createBaseStr(User user){
        return "Basic " + decrypt(user);
    }


}
