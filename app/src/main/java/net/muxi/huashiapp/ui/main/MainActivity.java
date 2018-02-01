package net.muxi.huashiapp.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.FrameLayout;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.BuildConfig;
import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.SplashData;
import net.muxi.huashiapp.event.LibLoginEvent;
import net.muxi.huashiapp.event.LoginSuccessEvent;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.service.DownloadService;
import net.muxi.huashiapp.ui.library.fragment.LibraryMainFragment;
import net.muxi.huashiapp.ui.library.fragment.LibraryMineFragment;
import net.muxi.huashiapp.ui.login.LoginActivity;
import net.muxi.huashiapp.ui.more.CheckUpdateDialog;
import net.muxi.huashiapp.ui.more.MoreFragment;
import net.muxi.huashiapp.ui.schedule.TimetableFragment;
import net.muxi.huashiapp.util.AlarmUtil;
import net.muxi.huashiapp.util.FrescoUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.ToastUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view)
    BottomNavigationView mNavView;
    @BindView(R.id.content_layout)
    FrameLayout mContentLayout;

    private Fragment mCurFragment;
    public  static Activity sContext;
    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sContext = MainActivity.this;
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mNavView.setOnNavigationItemSelectedListener(this);
        //开启动态权限
        if (!isStoragePermissionGranted()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        initView();
        initListener();
        handleIntent(getIntent());
        checkNewVersion();
        AlarmUtil.register(this);
        getSplashData();
    }
    private void checkNewVersion() {
        CampusFactory.getRetrofitService().getLatestVersion()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(versionData -> {
                    if (!versionData.getVersion().equals(BuildConfig.VERSION_NAME)) {
                        final CheckUpdateDialog checkUpdateDialog = new CheckUpdateDialog();
                        checkUpdateDialog.setTitle(App.sContext.getString(R.string.title_update) + versionData.getVersion());
                        checkUpdateDialog.setContent(
                                App.sContext.getString(R.string.tip_update_intro) + versionData.getIntro() + "\n" + App.sContext.getString(R.string.tip_update_size) + versionData.getSize());
                        checkUpdateDialog.setOnPositiveButton(App.sContext.getString(R.string.btn_update), () -> {
                                    if (isStoragePermissionGranted()) {
                                        beginUpdate(versionData.download);
                                    }else {
                                        showErrorSnackbarShort(R.string.tip_require_write_permission);
                                    }
                                    checkUpdateDialog.dismiss();
                                });
                        checkUpdateDialog.setOnNegativeButton(App.sContext.getString(R.string.btn_cancel),
                                () -> checkUpdateDialog.dismiss());
                        checkUpdateDialog.show(getSupportFragmentManager(), "dialog_update");
                    }
                }, throwable -> throwable.printStackTrace());
    }
    private void beginUpdate(String download) {
        deleteApkBefore();
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("url", download);
        intent.putExtra("fileType", "apk");
        intent.putExtra("fileName", "ccnubox.apk");
        startService(intent);
        Logger.d("start download");
        ToastUtil.showShort(getString(R.string.tip_start_download_apk));
    }
    private void deleteApkBefore() {
        String path = Environment.getExternalStorageDirectory() + "/Download/" + "ccnubox.apk";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            Logger.d("apk file delete");
        }
        Logger.d("file not exists");
    }
    private void initListener() {RxBus.getDefault().toObservable( LibLoginEvent.class)
                .subscribe(libLoginEvent -> {
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction().remove(mCurFragment).commitAllowingStateLoss();
                    if (fm.findFragmentByTag("lib_mine") != null) {
                        fm.beginTransaction()
                                .replace(R.id.content_layout, fm.findFragmentByTag("lib_mine"))
                                .commitAllowingStateLoss();
                    } else {
                        Fragment fragment = LibraryMineFragment.newInstance();
                        fm.beginTransaction()
                                .replace(R.id.content_layout, fragment, "lib_mine")
                                .addToBackStack("lib_mine")
                                .commitAllowingStateLoss();
                    }
                }, Throwable::printStackTrace);
        Subscription subscription = RxBus.getDefault().toObservable(LoginSuccessEvent.class)
                .subscribe(loginSuccessEvent -> {
                    if (loginSuccessEvent.targetActivityName.equals("table")){
                        ((TimetableFragment)mCurFragment).loadTable();
                    }
                },Throwable::printStackTrace);
        addSubscription(subscription);
    }
    private void getSplashData() {
        CampusFactory.getRetrofitService().getSplash()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<SplashData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(SplashData splashData) {
                        if (splashData.getUpdate() != 0
                                && PreferenceUtil.getLong(Constants.SPLASH_UPDATE)
                                != splashData.getUpdate()) {
                            saveSplashData(splashData);
                            FrescoUtil.savePicture(splashData.getImg(), MainActivity.this,
                                    "splash.jpg");
                        }
                    }
                });
    }

    private void saveSplashData(SplashData splashData) {
        PreferenceUtil.saveLong(Constants.SPLASH_UPDATE, splashData.update);
        PreferenceUtil.saveString(Constants.SPLASH_IMG, splashData.img);
        PreferenceUtil.saveString(Constants.SPLASH_URL, splashData.url);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    //根据 intent 跳转到对应的 fragment
    private void handleIntent(Intent intent) {
        if (!intent.hasExtra("ui")) {
            return;
        }
        String name = intent.getStringExtra("ui");
        switch (name) {
            case "table":
                showFragment("table");
                mNavView.getMenu().getItem(1).setChecked(true);
                break;
            case "lib_mine":
                showFragment("lib_mine");
                mNavView.getMenu().getItem(2).setChecked(true);
                break;
        }
    }

    private void initView() {
        showFragment("main");
        BottomNavigationHelper.disableShiftMode(mNavView);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_main:
                showFragment("main");
                break;
            case R.id.action_timetable:
                if (TextUtils.isEmpty(App.sUser.sid)) {
                    LoginActivity.start(MainActivity.this, "info", "table");
                }
                showFragment("table");
                break;
            case R.id.action_library:
                if (App.isLibLogin()) {
                    showFragment("lib_mine");
                } else {
                    showFragment("lib_main");
                }
                break;
            case R.id.action_more:
                showFragment("more");
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void showFragment(Fragment fragment, String tag) {
        mCurFragment = fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_layout,
                fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
        Logger.d(fragment.getTag());
    }

    public void showFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(tag) != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_layout,
                    fragmentManager.findFragmentByTag(tag), tag);
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
            return;
        }
        Logger.d("begin new fragment instance");
        switch (tag) {
            case "main":
                showFragment(MainFragment.newInstance(), tag);
                break;
            case "table":
                showFragment(TimetableFragment.newInstance(), tag);
                break;
            case "lib_main":
                showFragment(   LibraryMainFragment.newInstance(), tag);
                break;
            case "lib_mine":
                showFragment(LibraryMineFragment.newInstance(), tag);
                break;
            case "more":
                showFragment(MoreFragment.newInstance(), tag);
                break;
        }
    }

    public void removeFragment(String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Logger.d("remove frag");
        if (fragmentManager.findFragmentByTag(tag) != null){
            Logger.d("remove tag");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentManager.findFragmentByTag(tag));
            fragmentTransaction.commit();
        }
    }

    public boolean isStoragePermissionGranted() {
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

}