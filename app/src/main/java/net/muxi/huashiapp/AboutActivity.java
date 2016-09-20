package net.muxi.huashiapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxi.material_dialog.MaterialDialog;

import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.VersionData;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.service.DownloadService;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.common.util.ZhugeUtils;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 16/8/1.
 */
public class AboutActivity extends ToolbarActivity {

    private Toolbar mToolbar;
    private TextView mBtnSuggestion;
    private ImageView mImgCcnubox;
    private TextView mTvMuxiLink;
    private TextView mTvVersionname;

    private String downloadUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();

    }

    public void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mBtnSuggestion = (TextView) findViewById(R.id.btn_suggestion);
        mImgCcnubox = (ImageView) findViewById(R.id.img_ccnubox);
        mTvMuxiLink = (TextView) findViewById(R.id.tv_muxi_link);
        mTvVersionname = (TextView) findViewById(R.id.tv_versionname);
        setTitle("关于");
        mTvVersionname.setText(BuildConfig.VERSION_NAME);
        mTvMuxiLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + mTvMuxiLink.getText().toString()));
                ZhugeUtils.sendEvent("打开木犀官网", "打开木犀官网");
                startActivity(browserIntent);
            }
        });

        mBtnSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhugeUtils.sendEvent("点击意见反馈", "点击意见反馈");
                Intent intent = new Intent(AboutActivity.this, SuggestionActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_check_update) {
            checkUpdates();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkUpdates() {
        if (NetStatus.isConnected()) {
            CampusFactory.getRetrofitService().getLatestVersion()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Observer<VersionData>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(final VersionData versionData) {
                            if (versionData.getVersion() != null && !BuildConfig.VERSION_NAME.equals(versionData.getVersion())) {
                                final MaterialDialog materialDialog = new MaterialDialog(AboutActivity.this);
                                materialDialog.setTitle(versionData.getName() + versionData.getVersion() + App.sContext.getString(R.string.title_update));
                                materialDialog.setContent(App.sContext.getString(R.string.tip_update_intro) + versionData.getIntro() + "\n" + App.sContext.getString(R.string.tip_update_size) + versionData.getSize());
                                materialDialog.setButtonColor(getResources().getColor(R.color.colorPrimary));
                                materialDialog.setPositiveButton(getString(R.string.btn_update), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        downloadUrl = versionData.getDownload();
                                        if (isStorgePermissionGranted()) {
                                            beginUpdate(versionData.getDownload());
                                        }
                                        materialDialog.dismiss();

                                    }
                                });
                                materialDialog.setNegativeButton(getString(R.string.btn_not_now), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        materialDialog.dismiss();
                                    }
                                });
                                materialDialog.show();

                            } else {
                                final MaterialDialog materialDialog = new MaterialDialog(AboutActivity.this);
                                materialDialog.setTitle(getString(R.string.title_not_have_to_update));
                                materialDialog.setButtonColor(getResources().getColor(R.color.colorPrimary));
                                materialDialog.setNegativeButtonVisible(false);
                                materialDialog.setPositiveButton(getResources().getString(R.string.btn_positive), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        materialDialog.dismiss();
                                    }
                                });
                                materialDialog.show();
                            }
                        }
                    });
        } else {
            ToastUtil.showShort(getString(R.string.tip_check_net));
        }
    }

    public boolean isStorgePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(AboutActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Logger.d("permission " + permissions[0] + "is" + grantResults[0]);
            Logger.d(downloadUrl);
            if (downloadUrl != null && downloadUrl.length() != 0) {
                beginUpdate(downloadUrl);
            }
        }
    }

    private void beginUpdate(String download) {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("url", download);
        intent.putExtra("fileType", "apk");
        intent.putExtra("fileName", "ccnubox.apk");
        startService(intent);
        Logger.d("download");
        ToastUtil.showShort(getString(R.string.tip_start_download_apk));
    }


    public static class NoUnderlineSpan extends UnderlineSpan {

        public NoUnderlineSpan() {
        }

        public NoUnderlineSpan(Parcel src) {
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
