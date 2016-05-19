package net.muxi.huashiapp.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by ybao on 16/4/19.
 */
public class BaseActivity extends AppCompatActivity{

    protected Menu menu;
    protected ActionBar mActionBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
//        ZhugeSDK.getInstance().init(getApplicationContext());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ZhugeSDK.getInstance().flush(getApplicationContext());
    }


}
