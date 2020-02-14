package net.muxi.huashiapp.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
//import android.support.annotation.Nullable;
//import android.support.v4.content.FileProvider;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import net.muxi.huashiapp.BuildConfig;

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
        if(intent == null){
            return START_REDELIVER_INTENT;
        }
        String fileType = intent.getStringExtra("fileType");
        final String fileName = intent.getStringExtra("fileName");
        if (fileType.equals("apk")) {
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    File file = new File(Environment.getExternalStorageDirectory() + "/Download/" + fileName);
                    //N 系统以上需要 file uri 访问
                    if (Build.VERSION.SDK_INT >= 24) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri apkFileUri = FileProvider.getUriForFile(context,
                                BuildConfig.APPLICATION_ID + ".fileProvider", file);
                        intent.setDataAndType(apkFileUri, FILE_TYPE);
                        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                            boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                            if(!hasInstallPermission){
                                //注意这个是8.0新API
                                Intent intent1 = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent1);
                            }
                        }
                    } else {
                        intent.setDataAndType(Uri.fromFile(file),
                                FILE_TYPE);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    startActivity(intent);
                    stopSelf();
                }
            };
            registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }
        String url = intent.getStringExtra("url");
        startDownload(url, fileName, fileType);
        return Service.START_STICKY;
    }

    private void startDownload(String url, String fileName, String fileType) {
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
