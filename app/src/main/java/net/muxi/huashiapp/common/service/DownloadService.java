package net.muxi.huashiapp.common.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

import net.muxi.huashiapp.common.util.Logger;

import java.io.File;

/**
 * Created by ybao on 16/8/11.
 */
public class DownloadService extends Service {

    private DownloadManager mDownloadManager;
    private long enque;
    private BroadcastReceiver mReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/huashiapp.apk")),
                        "application/vnd.android.package-archive");
                startActivity(intent);
                stopSelf();
            }
        };
        String url = intent.getStringExtra("url");
        registerReceiver(mReceiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        Logger.d(url);
        startDownload(url);
        return Service.START_STICKY;
    }

    private void startDownload(String url) {
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        request.setMimeType("application/vnd.android.package-archive");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"" +
                "huashiapp.apk");
        enque = mDownloadManager.enqueue(request);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
