package com.muxistudio.appcommon.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.muxistudio.common.base.Global;

/**
 * Created by kolibreath on 18-2-25.
 */

public class CopyBoardUtil {

    public static void copy(String source){
        ClipboardManager manager = (ClipboardManager) Global.getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data =   ClipData.newPlainText(null,source);
        manager.setPrimaryClip(data);
    }
}
