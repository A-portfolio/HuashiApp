package net.muxi.huashiapp.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.FrameLayout;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.ui.library.fragment.LibraryMainFragment;
import net.muxi.huashiapp.ui.library.fragment.LibraryMineFragment;
import net.muxi.huashiapp.ui.login.LoginActivity;
import net.muxi.huashiapp.ui.more.MoreFragment;
import net.muxi.huashiapp.ui.schedule.TimetableFragment;
import net.muxi.huashiapp.util.AlarmUtil;
import net.muxi.huashiapp.util.ZhugeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view)
    BottomNavigationView mNavView;
    @BindView(R.id.content_layout)
    FrameLayout mContentLayout;

    private Fragment mCurFragment;

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
        //开启动态权限
        if (!isStorgePermissionGranted()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        initData();
        initView();
        handleIntent(getIntent());
        AlarmUtil.register(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    //根据 intent 跳转到对应的 fragment
    private void handleIntent(Intent intent) {
        if (!intent.hasExtra("ui")){
            return;
        }
        String name = intent.getStringExtra("ui");
        switch (name){
            case "table":
                showFragment(TimetableFragment.newInstance());
                mNavView.getMenu().getItem(1).setChecked(true);
                break;
            case "lib":
                showFragment(LibraryMineFragment.newInstance());
                mNavView.getMenu().getItem(2).setChecked(true);
                break;
        }
    }

    private void initView() {
        getSupportFragmentManager().beginTransaction().add(R.id.content_layout,
                MainFragment.newInstance()).commit();
        BottomNavigationHelper.disableShiftMode(mNavView);
    }

    private void initData() {

    }

    /**
     * 当返回至主界面时重新刷新界面获取数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mCurFragment == null) {
            return;
        }
        switch (mCurFragment.getTag()) {
            case "table":
                showFragment(TimetableFragment.newInstance());
                break;
            case "lib_main":
            case "lib_mine":
                if (App.isLibLogin()) {
                    if (getSupportFragmentManager().findFragmentByTag("lib_mine") != null){
                        return;
                    }
                    showFragment(LibraryMineFragment.newInstance());
                } else {
                    if (getSupportFragmentManager().findFragmentByTag("lib_main") != null){
                        return;
                    }
                    showFragment(LibraryMainFragment.newInstance());
                }
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main:
                showFragment(MainFragment.newInstance());
                break;
            case R.id.action_timetable:
                showFragment(TimetableFragment.newInstance());
                if (TextUtils.isEmpty(App.sUser.sid)) {
                    LoginActivity.start(MainActivity.this, "info");
                }
                break;
            case R.id.action_library:
                if (App.isLibLogin()) {
                    showFragment(LibraryMineFragment.newInstance());
                } else {
                    showFragment(LibraryMainFragment.newInstance());
                }
                break;
            case R.id.action_more:
                showFragment(MoreFragment.newInstance());
                break;
        }
        return true;
    }

    public void showFragment(Fragment fragment) {
        mCurFragment = fragment;
        String tag = "";
        if (fragment instanceof MainFragment) {
            tag = "main";
        } else if (fragment instanceof TimetableFragment) {
            tag = "table";
        } else if (fragment instanceof LibraryMainFragment) {
            tag = "lib_main";
        } else if (fragment instanceof LibraryMineFragment) {
            tag = "lib_mine";
        } else if (fragment instanceof MoreFragment) {
            tag = "more";
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout,
                fragment, tag).commit();
    }

    public boolean isStorgePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//            @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Logger.d("able to write external");
//        }
//    }

}