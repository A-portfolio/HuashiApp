package net.muxi.huashiapp.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.appbase.BaseAppActivity;
import com.muxistudio.appcommon.data.SplashData;
import com.muxistudio.appcommon.data.StatisticsData;
import com.muxistudio.appcommon.db.HuaShiDao;
import com.muxistudio.appcommon.event.LibLoginEvent;
import com.muxistudio.appcommon.event.LoginSuccessEvent;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.appcommon.utils.FrescoUtil;
import com.muxistudio.common.util.Logger;
import com.muxistudio.common.util.PreferenceUtil;
import com.muxistudio.common.util.ToastUtil;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.BuildConfig;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.login.CcnuCrawler3;
import net.muxi.huashiapp.login.SingleCCNUClient;
import net.muxi.huashiapp.service.DownloadService;
import net.muxi.huashiapp.statistics.work.UploadWork;
import net.muxi.huashiapp.ui.card.CardActivity;
import net.muxi.huashiapp.ui.library.fragment.LibraryMainFragment;
import net.muxi.huashiapp.ui.library.fragment.LibraryMineFragment;
import net.muxi.huashiapp.ui.login.LoginActivity;
import net.muxi.huashiapp.ui.more.CheckUpdateDialog;
import net.muxi.huashiapp.ui.more.MoreFragment;
import net.muxi.huashiapp.ui.score.activtities.ScoreCreditActivity;
import net.muxi.huashiapp.ui.timeTable.CourseAuditSearchActivity;
import net.muxi.huashiapp.ui.timeTable.TimetableFragment;
import net.muxi.huashiapp.utils.AlarmUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseAppActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {


    private Fragment mCurFragment;
    private BottomNavigationView mNavView;
    private CcnuCrawler3 ccnuCrawler3;
    private String curFragmentTag = "def";
    private final static String TAG = "MAINLOGIN";
    private static int REQUEST_PERMISSION_CODE = 1111;

    public static void start(Context context) {
        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavView = findViewById(R.id.nav_view);
        mNavView.setOnNavigationItemSelectedListener(this);

        //开启动态权限
        if (!isStoragePermissionGranted()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, REQUEST_PERMISSION_CODE);
        }
        initView();
        handleIntent(getIntent());
        checkNewVersion();

        //这个提醒好像不能用,先暂停
        //AlarmUtil.register(this);

        if (UserAccountManager.getInstance().isInfoUserLogin()) {
            ccnuCrawler3 = new CcnuCrawler3();
            ccnuCrawler3.performLogin(new Subscriber<ResponseBody>() {
                @Override
                public void onCompleted() {
                    Log.i(TAG, "onCompleted: ");
                    ccnuCrawler3.getClient().saveCookieToLocal();

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof HttpException) {
                        Log.e(TAG, "onError: httpexception code " + ((HttpException) e).response().code());
                        try {
                            Log.e(TAG, "onError:  httpexception errorbody: " + ((HttpException) e).response().errorBody().string());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    } else if (e instanceof NullPointerException)
                        Log.e(TAG, "onError: null   " + e.getMessage());
                    else
                        Log.e(TAG, "onError: ");
                    e.printStackTrace();


                }

                @Override
                public void onNext(ResponseBody responseBody) {
                    Log.i(TAG, "onNext: " + "login success");


                }
            }, UserAccountManager.getInstance().getInfoUser());


        }


    }


   public void setUploadWork(){
       Constraints constraints=new Constraints.Builder()
               .setRequiredNetworkType(NetworkType.CONNECTED)
               .setRequiresDeviceIdle(true)
               .build();
       OneTimeWorkRequest request=new OneTimeWorkRequest.Builder(UploadWork.class)
               .setConstraints(constraints)
               .build();
       WorkManager.getInstance(this).enqueue(request);
   }


    private void checkNewVersion() {
        CampusFactory.getRetrofitService().getLatestVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(versionData -> {
                    if (versionData == null) {
                        return;
                    }
                    if (!versionData.getVersion().equals(BuildConfig.VERSION_NAME)) {
                        final CheckUpdateDialog checkUpdateDialog = new CheckUpdateDialog();
                        checkUpdateDialog.setTitle(App.sContext.getString(R.string.title_update) + versionData.getVersion());
                        checkUpdateDialog.setContent(
                                App.sContext.getString(R.string.tip_update_intro) + versionData.getIntro() + "\n" + App.sContext.getString(R.string.tip_update_size) + versionData.getSize());
                        checkUpdateDialog.setOnPositiveButton(App.sContext.getString(R.string.btn_update), () -> {
                            if (isStoragePermissionGranted()) {
                                beginUpdate(versionData.download);
                            } else {
                                showErrorSnackbarShort(R.string.tip_require_write_permission);
                            }
                            checkUpdateDialog.dismiss();
                        });
                        checkUpdateDialog.setOnNegativeButton(App.sContext.getString(R.string.btn_cancel),
                                () -> checkUpdateDialog.dismiss());
                        checkUpdateDialog.show(getSupportFragmentManager(), "dialog_update");
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    Log.i(TAG, "checkNewVersion: onerror");

                });
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


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    // TODO: 18-8-20  
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
        int itemId = item.getItemId();
        if (itemId == R.id.action_main) {
            showFragment("main");
        } else if (itemId == R.id.action_timetable) {
            if (TextUtils.isEmpty(UserAccountManager.getInstance().getInfoUser().sid))
                LoginActivity.start(MainActivity.this, "info", "table");

            showFragment("table");

        } else if (itemId == R.id.action_library) {
            if (UserAccountManager.getInstance().isLibLogin()) {
                showFragment("lib_mine");
            } else {
                showFragment("lib_main");
            }
        } else if (itemId == R.id.action_more) {
            showFragment("more");
        }
        return true;
    }

    private  long lastBackTime=0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis()-lastBackTime<=2000){
            super.onBackPressed();
        }else {
            ToastUtil.showShort("再按一次退出");
            lastBackTime=System.currentTimeMillis();

        }
    }

    public void showFragment(Fragment fragment, String tag, FragmentTransaction fragmentTransaction) {
        fragmentTransaction.add(R.id.content_layout, fragment, tag);
        curFragmentTag = tag;
        fragmentTransaction.commitNow();
        Logger.d(fragment.getTag());
    }

    public void showFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment targetFragment = fragmentManager.findFragmentByTag(tag);
        Fragment curFragment = fragmentManager.findFragmentByTag(curFragmentTag);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (curFragment != null)
            fragmentTransaction.hide(curFragment);

        if (targetFragment != null) {
            curFragmentTag = tag;
            fragmentTransaction.show(targetFragment);
            fragmentTransaction.commitNow();
            return;
        }

        Logger.d("begin new fragment instance");
        switch (tag) {
            case "main":
                showFragment(MainFragment.newInstance(), tag, fragmentTransaction);
                break;
            case "table":
                showFragment(TimetableFragment.newInstance(), tag, fragmentTransaction);
                break;
            case "lib_main":
                showFragment(LibraryMineFragment.newInstance(), tag, fragmentTransaction);
                break;
            case "lib_mine":
                showFragment(LibraryMineFragment.newInstance(), tag, fragmentTransaction);
                break;
            case "more":
                showFragment(MoreFragment.newInstance(), tag, fragmentTransaction);
                break;
        }
    }

    public void removeFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Logger.d("remove frag");
        if (fragmentManager.findFragmentByTag(tag) != null) {
            Logger.d("remove tag");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragmentManager.findFragmentByTag(tag));
            fragmentTransaction.commit();
        }
    }


    public boolean isStoragePermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ccnuCrawler3 != null) {
            ccnuCrawler3.unsubscription();
        }
        setUploadWork();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                String id=tm.getDeviceId();
                if (id!=null) {
                    PreferenceUtil.saveString(PreferenceUtil.DEVICES_ID,id);
                    App.setDevicesId(id);
                }
            }

        }

    }

}