package net.muxi.huashiapp.service;

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

import net.muxi.huashiapp.util.Logger;

import java.io.File;

/**
 * Created by ybao on 16/8/11.
 */
public class DownloadService extends Service {

    private DownloadManager mDownloadManager;
    private long enque;
    private BroadcastReceiver mReceiver;

    private static final String FILE_TYPE = "application/vnd.android.package-archive";

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
                    File file = new File(Environment.getExternalStorageDirectory() + "/Download/" + fileName);
                    //if (Build.VERSION.SDK_INT >=24){
                    //    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //    Uri apkFileUri = FileProvider.getUriForFile(App.sContext, App.sContext.getPackageName() + ".provider", file);
                    //    intent.setDataAndType(apkFileUri,FILE_TYPE);
                    //}else{
                    intent.setDataAndType(Uri.fromFile(file),
                            FILE_TYPE);
                    //}

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
