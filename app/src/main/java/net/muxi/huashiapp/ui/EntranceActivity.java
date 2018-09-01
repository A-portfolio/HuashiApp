package net.muxi.huashiapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.appbase.BaseAppActivity;
import com.muxistudio.common.util.Logger;
import com.muxistudio.common.util.PreferenceUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.main.MainActivity;


/**
 * Created by ybao on 16/7/30.
 */
//这里是整个app刚刚进去的闪屏活动！
public class EntranceActivity extends BaseAppActivity implements View.OnClickListener {

    private SimpleDraweeView mDrawee;
    private long mSplashUpdate;
    private String mSplashUrl;
    private String mSplashImg;
    private static final int SPLASH_TIME = 200;
    private PreferenceUtil sp;
    private boolean isFirstOpen;

    private TextView mBtnSkip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new PreferenceUtil();
        isFirstOpen = PreferenceUtil.getBoolean(PreferenceUtil.APP_FIRST_OPEN, true);
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

                mBtnSkip = findViewById(R.id.btn_skip);

                mBtnSkip.setOnClickListener(this);
                mDrawee = findViewById(R.id.drawee);
                mDrawee.setImageURI(Uri.parse(PreferenceUtil.getString(Constants.SPLASH_IMG)));
                mDrawee.setOnClickListener(this);
                startMainActivityDelay(2500);
            }
            return;
        }

        GuideActivity.start(this);
        PreferenceUtil.saveBoolean(PreferenceUtil.APP_FIRST_OPEN, false);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_skip){
            MainActivity.start(this);
            this.finish();
        }

        if (v.getId() == R.id.drawee) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(PreferenceUtil.getString(Constants.SPLASH_URL).toString()));
            startActivity(intent);
        }
    }

    public void startMainActivityDelay(long delayMills){
        new Handler().postDelayed(() ->{
            MainActivity.start(EntranceActivity.this);
            EntranceActivity.this.finish();
        },delayMills);
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
