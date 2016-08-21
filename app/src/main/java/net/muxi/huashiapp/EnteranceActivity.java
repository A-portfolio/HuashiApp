package net.muxi.huashiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.main.MainActivity;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;

/**
 * Created by ybao on 16/7/30.
 */
public class EnteranceActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterance);
        Observable.timer(1, TimeUnit.SECONDS)
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
