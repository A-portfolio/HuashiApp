package net.muxi.huashiapp.common.base;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.zhuge.analysis.stat.ZhugeSDK;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.Logger;

/**
 * Created by ybao on 16/4/19.
 */
public class BaseActivity extends AppCompatActivity{

    protected Menu menu;
    protected ActionBar mActionBar;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mProgressDialog = new ProgressDialog(this, R.style.ThemeHoloLightDialogAlert);
        }else {
            mProgressDialog = new ProgressDialog(this);
            Logger.d("lollipop");
        }
        mProgressDialog.setCancelable(false);
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
//        ZhugeSDK.getInstance().openDebug();
//        ZhugeSDK.getInstance().openLog();

    }

    public void setContentView(int layoutResId){
        super.setContentView(layoutResId);
    }

    /**
     * show the progressBarDialog with message
     * @param show
     * @param message
     */
    public void showProgressBarDialog(boolean show,String message){
        if (show){
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }else {
            mProgressDialog.hide();
        }
    }

    public void showProgressBarDialog(boolean show){
        if (show){
            mProgressDialog.show();
        }else {
            mProgressDialog.hide();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //初始化分析跟踪
        ZhugeSDK.getInstance().init(getApplicationContext());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZhugeSDK.getInstance().flush(getApplicationContext());
    }


}
