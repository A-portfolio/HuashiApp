package net.muxi.huashiapp.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import net.muxi.huashiapp.App;

/**
 * Created by kolibreath on 18-2-25.
 */

public class CopyBoardUtil {

    public static void copy(String source){
        ClipboardManager manager = (ClipboardManager) App.sContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data =   ClipData.newPlainText(null,source);
        manager.setPrimaryClip(data);
    }
}
