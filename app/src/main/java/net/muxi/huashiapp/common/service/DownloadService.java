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
        String fileType = intent.getStringExtra("fileType");
        final String fileName = intent.getStringExtra("fileName");
        if (fileType.equals("apk")) {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Download/" + fileName)),
                            "application/vnd.android.package-archive");
                    startActivity(intent);
                    stopSelf();
                }
            };
            registerReceiver(mReceiver,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
        String url = intent.getStringExtra("url");
        Logger.d(url);
        startDownload(url,fileName,fileType);
        return Service.START_STICKY;
    }

    private void startDownload(String url,String fileName,String fileType) {
        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(url));
        if (fileType.equals("apk")) {
            request.setMimeType("application/vnd.android.package-archive");
        }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        enque = mDownloadManager.enqueue(request);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
