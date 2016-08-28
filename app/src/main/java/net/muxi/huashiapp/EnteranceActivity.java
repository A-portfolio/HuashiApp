package net.muxi.huashiapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.SplashData;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.FrescoUtil;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.main.MainActivity;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by ybao on 16/7/30.
 */
public class EnteranceActivity extends BaseActivity implements View.OnClickListener {

    private SimpleDraweeView mDrawee;
    private long mSplashUpdate;
    private String mSplashUrl;
    private String mSplashImg;

    private static final int SPLASH_TIME = 2000;

    private PreferenceUtil sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterance);
        mDrawee = (SimpleDraweeView) findViewById(R.id.drawee);
        sp = new PreferenceUtil();
        Logger.d(sp.getString(AppConstants.SPLASH_IMG));
        if (sp.getLong(AppConstants.SPLASH_UPDATE) == -1) {
            mDrawee.setImageURI(Uri.parse("asset://net.muxi.huashiapp/bg_enterance.png"));
        } else {
            if (!sp.getString(AppConstants.SPLASH_IMG).equals("")) {
                mDrawee.setImageURI(Uri.parse(sp.getString(AppConstants.SPLASH_IMG)));
            }
        }

        getSplashData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(EnteranceActivity.this, MainActivity.class);
                startActivity(intent);
                EnteranceActivity.this.finish();
            }
        }, SPLASH_TIME);
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
