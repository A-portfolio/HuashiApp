package net.muxi.huashiapp.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.FrameLayout;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.ui.login.LoginActivity;
import net.muxi.huashiapp.ui.schedule.TimetableFragment;
import net.muxi.huashiapp.util.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view)
    BottomNavigationView mNavView;
    @BindView(R.id.content_layout)
    FrameLayout mContentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mNavView.setOnNavigationItemSelectedListener(this);
        initData();
        initView();
    }

    private void initView() {
        getSupportFragmentManager().beginTransaction().add(MainFragment.newInstance(),
                "main").commit();
    }

    private void initData() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main:
                break;
            case R.id.action_timetable:
                Logger.d(TextUtils.isEmpty(App.sUser.sid) + "");
                Logger.d("navigation");
                if (TextUtils.isEmpty(App.sUser.sid)) {
                    LoginActivity.start(MainActivity.this);
                } else {
                    showFragment(TimetableFragment.newInstance());
                }
                break;
            case R.id.action_library:
                break;
            case R.id.action_more:
                break;
        }
        return true;
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, fragment).commit();
    }
}