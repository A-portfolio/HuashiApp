package net.muxi.huashiapp.common.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.ButterKnife;

/**
 * Created by ybao on 16/7/7.
 */
public class AboutActivity extends ToolbarActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
//        mBanner.
    }
}
