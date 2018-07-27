package net.muxi.huashiapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.appbase.BaseAppActivity;
import com.muxistudio.common.util.Logger;
import com.muxistudio.common.util.PreferenceUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.main.MainActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;


/**
 * Created by ybao on 16/7/30.
 */
//这里是整个app刚刚进去的闪屏活动！
public class EntranceActivity extends BaseAppActivity implements View.OnClickListener {

    private long mSplashUpdate;
    private String mSplashUrl;
    private String mSplashImg;
    private static final int SPLASH_TIME = 2000;
    private PreferenceUtil sp;

    private Subscription mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new PreferenceUtil();
        boolean isFirstOpen = PreferenceUtil.getBoolean(PreferenceUtil.APP_FIRST_OPEN, true);

        Button mBtnSkip = findViewById(R.id.btn_skip);
        mBtnSkip.setOnClickListener(this);

        if (!isFirstOpen) {

            if (PreferenceUtil.getString(Constants.SPLASH_IMG).equals("")) {
                startMainActivityDelay(0);
            } else {
                setContentView(R.layout.activity_entrance);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

                SimpleDraweeView mDrawee = (SimpleDraweeView) findViewById(R.id.drawee);
                mDrawee.setImageURI(Uri.parse(PreferenceUtil.getString(Constants.SPLASH_IMG)));
                mDrawee.setOnClickListener(this);
                startMainActivityDelay(SPLASH_TIME);
            }
            return;
        }

        GuideActivity.start(this);
        PreferenceUtil.saveBoolean(PreferenceUtil.APP_FIRST_OPEN, false);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_skip) {
            mHandler.unsubscribe();
            finish();
        }
        if (v.getId() == R.id.drawee) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(PreferenceUtil.getString(Constants.SPLASH_URL)));
            startActivity(intent);
        }
    }

    public void startMainActivityDelay(long delayMills){
        mHandler = Observable.timer(delayMills, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    MainActivity.start(EntranceActivity.this);
                    EntranceActivity.this.finish();
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d("splash onresume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
