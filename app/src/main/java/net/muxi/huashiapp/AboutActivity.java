package net.muxi.huashiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.muxi.material_dialog.MaterialDialog;

import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.VersionData;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.DownloadUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 16/8/1.
 */
public class AboutActivity extends ToolbarActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_info)
    Button mBtnInfo;
    @BindView(R.id.btn_suggestion)
    Button mBtnSuggestion;
    @BindView(R.id.view)
    CardView mView;
    @BindView(R.id.tv_versionname)
    TextView mTvVersionname;
    @BindView(R.id.tv_muxi_link)
    TextView mTvMuxiLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        init();

    }

    public void init() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("关于我们");
        mTvMuxiLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + mTvMuxiLink.getText().toString()));
                startActivity(browserIntent);
            }
        });
    }


    @OnClick({R.id.btn_info, R.id.btn_suggestion})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_info:
                intent = new Intent(AboutActivity.this,InfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_suggestion:
                intent = new Intent(AboutActivity.this, SuggestionActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_check_update){
            checkUpdates();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkUpdates() {
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
                        if (!BuildConfig.VERSION_NAME.equals(versionData.getVersion())){
                            MaterialDialog materialDialog = new MaterialDialog(AboutActivity.this);
                            materialDialog.setTitle(versionData.getName() + versionData.getVersion() + getString(R.string.title_update));
                            materialDialog.setContent(versionData.getIntro() + "\n" + getString(R.string.tip_update_size ) + versionData.getSize());
                            materialDialog.setButtonColor(getResources().getColor(R.color.colorPrimary));
                            materialDialog.setNegativeButton(getString(R.string.btn_update), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    beginUpdate(versionData.getDownload());
                                }
                            });
                            materialDialog.setPositiveButton(getString(R.string.btn_not_now), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });

                        }else {
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
    }

    private void beginUpdate(String download) {
        CampusFactory.getRetrofitService().downloadFile(download)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        DownloadUtils.writeResponseBodyToDisk(responseBody,getString(R.string.app));
                    }
                });
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
