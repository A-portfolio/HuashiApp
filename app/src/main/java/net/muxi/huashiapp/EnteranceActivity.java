package net.muxi.huashiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.SplashData;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.FrescoUtil;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.main.MainActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by ybao on 16/7/30.
 */
public class EnteranceActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.drawee)
    SimpleDraweeView mDrawee;
    @BindView(R.id.root_layout)
    RelativeLayout mRootLayout;
    private long mSplashUpdate;
    private String mSplashUrl;
    private String mSplashImg;

    private static final int SPLASH_TIME = 2;

    private PreferenceUtil sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterance);
        ButterKnife.bind(this);
        sp = new PreferenceUtil();
        Logger.d(sp.getString(AppConstants.SPLASH_IMG));
        if (sp.getLong(AppConstants.SPLASH_UPDATE) == -1) {
            mDrawee.setImageURI(Uri.parse("res://net.muxi.huashiapp/" + R.drawable.bg_enterance));
        } else {
            if (!sp.getString(AppConstants.SPLASH_IMG).equals("")) {
                mDrawee.setImageURI(Uri.parse(sp.getString(AppConstants.SPLASH_IMG)));
            }
        }

        getSplashData();
        Observable.timer(SPLASH_TIME, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Intent intent;
                        intent = new Intent(EnteranceActivity.this, MainActivity.class);
                        startActivity(intent);
                        EnteranceActivity.this.finish();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.drawee) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sp.getString(AppConstants.SPLASH_URL).toString()));
            startActivity(intent);
        }
    }

    private void getSplashData() {
        CampusFactory.getRetrofitService().getSplash()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<SplashData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SplashData splashData) {
                        if (splashData.getUpdate() != 0 && mSplashUpdate != splashData.getUpdate()) {
                            saveSplashData(splashData);
                            Logger.d("save splash data");
                            FrescoUtil.savePicture(splashData.getImg(), EnteranceActivity.this, "splash.jpg");
                        } else {
                            sp.saveLong(AppConstants.SPLASH_UPDATE, -1);
                        }
                    }
                });
    }

    private void saveSplashData(SplashData splashData) {
        mSplashUpdate = splashData.getUpdate();
        mSplashImg = splashData.getImg();
        mSplashUrl = splashData.getUrl();
        sp.saveLong(AppConstants.SPLASH_UPDATE, mSplashUpdate);
        sp.saveString(AppConstants.SPLASH_IMG, mSplashImg);
        sp.saveString(AppConstants.SPLASH_URL, mSplashUrl);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
