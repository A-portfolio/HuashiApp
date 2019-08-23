package net.muxi.huashiapp.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.common.util.PreferenceUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SplashActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MainActivity.start(this);
        finish();
    }


    public void getConfig(){
        CampusFactory.getRetrofitService().getConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(config -> {
                    PreferenceUtil.saveString(PreferenceUtil.FIRST_WEEK_DATE,config.getStartCountDayPresetForV2());
                    PreferenceUtil.saveString(PreferenceUtil.CALENDAR_ADDRESS,config.getCalendarUrl());


                });


    }
}
