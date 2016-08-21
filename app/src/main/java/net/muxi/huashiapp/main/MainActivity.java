package net.muxi.huashiapp.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import net.muxi.huashiapp.AboutActivity;
import net.muxi.huashiapp.App;
import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.BuildConfig;
import net.muxi.huashiapp.CalendarActivity;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.SettingActivity;
import net.muxi.huashiapp.apartment.ApartmentActivity;
import net.muxi.huashiapp.card.CardActivity;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.BannerData;
import net.muxi.huashiapp.common.data.PatchData;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.AlarmUtil;
import net.muxi.huashiapp.common.util.DownloadUtils;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.util.ZhugeUtils;
import net.muxi.huashiapp.electricity.ElectricityActivity;
import net.muxi.huashiapp.electricity.ElectricityDetailActivity;
import net.muxi.huashiapp.library.LibraryLoginActivity;
import net.muxi.huashiapp.library.MineActivity;
import net.muxi.huashiapp.news.NewsActivity;
import net.muxi.huashiapp.schedule.ScheduleActivity;
import net.muxi.huashiapp.score.ScoreActivity;
import net.muxi.huashiapp.webview.WebViewActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private int[] mpics = {R.drawable.ic_main_curschedule, R.drawable.ic_main_idcard,
            R.drawable.ic_main_mark, R.drawable.ic_main_power_rate,
            R.drawable.ic_main_school_calendar, R.drawable.ic_main_workschedule,
            R.drawable.ic_main_library, R.drawable.ic_xueer};

    private String[] mdesc = {"课程表", "学生卡", "成绩查询", "电费查询", "校历查询", "部门信息", "图书馆", "学而",};
    private MainAdapter mAdapter;

    private long exitTime = 0;

    private HuaShiDao dao;
    private List<BannerData> mBannerDatas;

    private Context context;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initXGPush();





        //检查本地是否有补丁包
        try {
            if (!DownloadUtils.isFileExists(AppConstants.CACHE_DIR + "/" + AppConstants.APATCH_NAME)) {
                downloadPatch();
                Logger.d("download patch");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d("cache dir not found");
        }

        dao = new HuaShiDao();
        getBannerDatas();

        AlarmUtil.register(this);
    }

    //信鸽注册和启动
    private void initXGPush(){
        Logger.d("initXGPush");
        context = getApplicationContext();
        XGPushConfig.enableDebug(this,true);
        XGPushConfig.getToken(this);
        XGPushManager.registerPush(context, "users"
                , new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int i) {
                        Log.d("TPush", "注册成功，设备token为：" + data);

                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);

                    }
                });
    }







    private void downloadPatch() {
        CampusFactory.getRetrofitService().getPatch()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<PatchData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<PatchData> patchDatas) {
                        for (PatchData patchData : patchDatas){
                            if (BuildConfig.VERSION_NAME.equals(patchData.getVersion())){
                                CampusFactory.getRetrofitService().downloadFile(patchData.getDownload())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribeOn(Schedulers.newThread())
                                        .subscribe(new Observer<ResponseBody>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                e.printStackTrace();
                                            }

                                            @Override
                                            public void onNext(ResponseBody responseBody) {
                                                DownloadUtils.writeResponseBodyToDisk(responseBody, AppConstants.APATCH_NAME);
                                            }
                                        });
                            }
                        }
                    }
                });

    }

    private void getBannerDatas() {
        mBannerDatas = dao.loadBannerData();
        if (mBannerDatas.size() > 0) {
            initRecyclerView();
            Logger.d("init recyclerview");
        } else {
            initRecyclerView();
            Logger.d("please link the net");
        }
        if (NetStatus.isConnected()) {
            //本地保存的更新时间
            CampusFactory.getRetrofitService().getBanner()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe(new Observer<List<BannerData>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(List<BannerData> bannerDatas) {
                            if (getTheLastUpdateTime(bannerDatas) > getTheLastUpdateTime(mBannerDatas)) {
                                mBannerDatas.clear();
                                mBannerDatas.addAll(bannerDatas);
                                dao.deleteAllBannerData();
                                for (int i = 0; i < mBannerDatas.size(); i++) {
                                    dao.insertBannerData(mBannerDatas.get(i));
                                }
                                updateRecyclerView(bannerDatas);
                                Logger.d("update recyclerview");
                            }
                            Logger.d("get bannerdatas");
                        }
                    });
        }

    }


    /**
     * 在 list 中 获取最近的更新时间
     *
     * @param bannerDatas
     * @return
     */
    public long getTheLastUpdateTime(List<BannerData> bannerDatas) {
        long lastTime = -1;
        if (bannerDatas.size() > 0) {
            for (int i = 0; i < bannerDatas.size(); i++) {
                if (lastTime < bannerDatas.get(i).getUpdate()) {
                    lastTime = bannerDatas.get(i).getUpdate();
                }
            }
        }
        return lastTime;
    }


    public void initRecyclerView() {
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mAdapter = new MainAdapter(mdesc, mpics, mBannerDatas);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.isBannerPosition(position) ? layoutManager.getSpanCount() : 1;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addItemDecoration(new MyItemDecoration());
        mAdapter.setOnBannerItemClickListener(new MainAdapter.OnBannerItemClickListener() {
            @Override
            public void onBannerItemClick(BannerData bannerData) {
                ZhugeUtils.sendEvent("点击 banner","点解 banner");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bannerData.getUrl()));
                startActivity(browserIntent);
            }
        });
        mAdapter.setItemClickListener(new MainAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, ScheduleActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        ZhugeUtils.sendEvent("学生卡查询","学生卡查询");
                        intent = new Intent(MainActivity.this, CardActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, ScoreActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        PreferenceUtil sp = new PreferenceUtil();
                        String eleQuery = sp.getString(PreferenceUtil.ELE_QUERY_STRING);
                        if (eleQuery.equals("")) {
                            intent = new Intent(MainActivity.this, ElectricityActivity.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(MainActivity.this, ElectricityDetailActivity.class);
                            intent.putExtra("query", eleQuery);
                            startActivity(intent);
                        }
                        break;
                    case 4:
                        ZhugeUtils.sendEvent("查询校历","查询校历");
                        intent = new Intent(MainActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        ZhugeUtils.sendEvent("查看部门信息","查看部门信息");
                        intent = new Intent(MainActivity.this, ApartmentActivity.class);
                        startActivity(intent);

                        break;
                    case 7:
                        ZhugeUtils.sendEvent("进入图书馆","进入图书馆");
                        if (!App.sLibrarayUser.getSid().equals("0")) {
                            intent = new Intent(MainActivity.this, MineActivity.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(MainActivity.this, LibraryLoginActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case 8:
                        ZhugeUtils.sendEvent("进入学而","进入学而");
                        intent = WebViewActivity.newIntent(MainActivity.this, "https://xueer.muxixyz.com/", "学而","华师选课经验平台","http://f.hiphotos.baidu.com/image/h%3D200/sign=6f05c5f929738bd4db21b531918a876c/6a600c338744ebf8affdde1bdef9d72a6059a702.jpg");
                        startActivity(intent);
                        break;


                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void updateRecyclerView(List<BannerData> bannerDatas) {
        mAdapter.swap(bannerDatas);
//        mAdapter = new MainAdapter(mdesc,mpics,bannerDatas);
//        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 3000) {
                Toast.makeText(getApplicationContext(), "再按一次后退键退出应用程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        switch (id) {
            case R.id.action_news:
                ZhugeUtils.sendEvent("查看消息公告","查看消息公告");
                intent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_settings:
                intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.action_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean canBack() {
        return false;
    }

}
