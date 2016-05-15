package net.muxi.huashiapp.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

/**
 * Created by ybao on 16/4/19.
 */
public class BaseActivity extends AppCompatActivity{

    protected Menu menu;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ZhugeSDK.getInstance().openDebug();
//        ZhugeSDK.getInstance().openLog();

    }

    public void setContentView(int layoutResId){
        super.setContentView(layoutResId);
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
