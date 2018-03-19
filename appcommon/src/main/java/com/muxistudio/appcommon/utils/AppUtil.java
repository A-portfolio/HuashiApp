package com.muxistudio.appcommon.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by ybao on 16/5/19.
 */
public class AppUtil {

    public static FragmentTransaction ft;
    public static FragmentManager fm;


    public static void clipToClipBoard(Context context, String content) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(null, content));
        if (clipboard.hasPrimaryClip()) {
            clipboard.getPrimaryClip().getItemAt(0).getText();
        }
    }

    public static void clipToClipBoard(Context context, CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
        if (clipboard.hasPrimaryClip()) {
            clipboard.getPrimaryClip().getItemAt(0).getText();
        }

    }


    public static void addFragmentToActivity(AppCompatActivity activity, Fragment fragment, int layout) {
        fm = activity.getFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(layout, fragment);
    }

}
