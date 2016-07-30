package net.muxi.huashiapp.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.CalendarActivity;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.SettingActivity;
import net.muxi.huashiapp.card.CardActivity;
import net.muxi.huashiapp.common.data.BannerData;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.ui.AboutActivity;
import net.muxi.huashiapp.common.util.AlarmUtil;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.electricity.ElectricityActivity;
import net.muxi.huashiapp.library.LibraryLoginActivity;
import net.muxi.huashiapp.library.MineActivity;
import net.muxi.huashiapp.news.NewsActivity;
import net.muxi.huashiapp.schedule.ScheduleActivity;
import net.muxi.huashiapp.score.ScoreActivity;
import net.muxi.huashiapp.webview.WebViewActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private int[] mpics = {R.drawable.t, R.drawable.t,
            R.drawable.t, R.drawable.t,
            R.drawable.t, R.drawable.t, R.drawable.t, R.drawable.t};

    private String[] mdesc = {"课程表", "学生卡", "成绩查询", "电费查询", "校历查询", "部门信息", "图书馆", "学而",};
    private MainAdapter mAdapter;

    private long exitTime = 0;

    private HuaShiDao dao;
    private List<BannerData> mBannerDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dao = new HuaShiDao();
        getBannerDatas();

        setSupportActionBar(mToolbar);
        AlarmUtil.register(this);
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
        mRecyclerView.addItemDecoration(new MyItemDecoration());
        mAdapter.setOnBannerItemClickListener(new MainAdapter.OnBannerItemClickListener() {
            @Override
            public void onBannerItemClick(BannerData bannerData) {
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
                        intent = new Intent(MainActivity.this, CardActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, ScoreActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, ElectricityActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case 5:

                        break;
                    case 7:
                        if (!App.sLibrarayUser.getSid().equals("0")) {
                            intent = new Intent(MainActivity.this, MineActivity.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(MainActivity.this, LibraryLoginActivity.class);
                            startActivity(intent);
                        }
                        break;

                    case 8:
                        intent = WebViewActivity.newIntent(MainActivity.this, "http://xueer.muxixyz.com/", "学而");
                        startActivity(intent);
                        break;


                }
            }
        });

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
}
