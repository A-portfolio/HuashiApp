package net.muxi.huashiapp.ui.main;

import android.content.Context;
import android.content.Intent;
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
import net.muxi.huashiapp.ui.library.LibraryMainFragment;
import net.muxi.huashiapp.ui.library.LibraryMineFragment;
import net.muxi.huashiapp.ui.login.LoginActivity;
import net.muxi.huashiapp.ui.more.MoreFragment;
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

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

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
        getSupportFragmentManager().beginTransaction().add(R.id.content_layout,
                MainFragment.newInstance()).commit();
        BottomNavigationHelper.disableShiftMode(mNavView);
    }

    private void initData() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main:
                showFragment(MainFragment.newInstance());
                break;
            case R.id.action_timetable:
                Logger.d(TextUtils.isEmpty(App.sUser.sid) + "");
                if (TextUtils.isEmpty(App.sUser.sid)) {
                    LoginActivity.start(MainActivity.this,"info");
                } else {
                    showFragment(TimetableFragment.newInstance());
                }
                break;
            case R.id.action_library:
                if (App.isLibLogin()){
                    showFragment(LibraryMineFragment.newInstance());
                }else {
                    showFragment(LibraryMainFragment.newInstance());
                }
                break;
            case R.id.action_more:

                break;
        }
        return true;
    }

    public void showFragment(Fragment fragment) {
        String tag;
        if (fragment instanceof MainFragment){
            tag = "main";
        }else if (fragment instanceof TimetableFragment){
            tag = "table";
        }else if (fragment instanceof LibraryMainFragment){
            tag = "lib_main";
        }else if (fragment instanceof LibraryMineFragment){
            tag = "lib_mine";
        }else if (fragment instanceof MoreFragment){
            tag = "more";
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,
                fragment,tag).commit();
    }


}