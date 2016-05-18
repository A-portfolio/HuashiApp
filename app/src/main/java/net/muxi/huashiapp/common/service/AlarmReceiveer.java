package net.muxi.huashiapp.common.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.muxi.huashiapp.common.util.NotifyUtil;
import net.muxi.huashiapp.library.LibraryActivity;

/**
 * Created by ybao on 16/5/16.
 */
public class AlarmReceiveer extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        NotifyUtil.show(context, LibraryActivity.class,"library","the book is ");
    }
}
