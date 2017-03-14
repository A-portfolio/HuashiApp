package net.muxi.huashiapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.SplashData;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.util.FrescoUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.ui.main.MainActivity;

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

    private static final int SPLASH_TIME = 200;

    private PreferenceUtil sp;

    private boolean isFirstOpen;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sp = new PreferenceUtil();
        isFirstOpen = sp.getBoolean(PreferenceUtil.APP_FIRST_OPEN);

        if (!isFirstOpen) {
            GuideActivity.start(this);
            sp.saveBoolean(PreferenceUtil.APP_FIRST_OPEN, true);
            this.finish();
            return;
        } else {


            setContentView(R.layout.activity_enterance);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            mDrawee = (SimpleDraweeView) findViewById(R.id.drawee);
            Logger.d(sp.getString(Constants.SPLASH_IMG));
            if (sp.getLong(Constants.SPLASH_UPDATE) == -1) {
                mDrawee.setImageURI(Uri.parse("asset://net.muxi.huashiapp/bg_enterance.png"));
            } else {
                if (!sp.getString(Constants.SPLASH_IMG).equals("")) {
                    mDrawee.setImageURI(Uri.parse(sp.getString(Constants.SPLASH_IMG)));
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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.drawee) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(sp.getString(Constants.SPLASH_URL).toString()));
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
                        if (splashData.getUpdate() != 0
                                && mSplashUpdate != splashData.getUpdate()) {
                            saveSplashData(splashData);
                            Logger.d("save splash data");
                            FrescoUtil.savePicture(splashData.getImg(), EnteranceActivity.this,
                                    "splash.jpg");
                        } else {
                            sp.saveLong(Constants.SPLASH_UPDATE, -1);
                        }
                    }
                });
    }

    private void saveSplashData(SplashData splashData) {
        mSplashUpdate = splashData.getUpdate();
        mSplashImg = splashData.getImg();
        mSplashUrl = splashData.getUrl();
        sp.saveLong(Constants.SPLASH_UPDATE, mSplashUpdate);
        sp.saveString(Constants.SPLASH_IMG, mSplashImg);
        sp.saveString(Constants.SPLASH_URL, mSplashUrl);
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
