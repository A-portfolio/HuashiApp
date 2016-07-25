package net.muxi.huashiapp.main;

import android.content.Intent;
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
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.ui.AboutActivity;
import net.muxi.huashiapp.common.ui.CalendarActivity;
import net.muxi.huashiapp.common.ui.SettingActivity;
import net.muxi.huashiapp.common.util.AlarmUtil;
import net.muxi.huashiapp.electricity.ElectricityActivity;
import net.muxi.huashiapp.library.LibraryLoginActivity;
import net.muxi.huashiapp.library.MineActivity;
import net.muxi.huashiapp.news.NewsActivity;
import net.muxi.huashiapp.schedule.ScheduleActivity;
import net.muxi.huashiapp.score.ScoreActivity;
import net.muxi.huashiapp.webview.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private int[] mpics = {R.drawable.t, R.drawable.t,
            R.drawable.t, R.drawable.t,
            R.drawable.t, R.drawable.t, R.drawable.t, R.drawable.t};

    private String[] mdesc = {"课程表", "图书查询", "成绩查询", "电费查询", "校历查询", "部门信息", "学而", "学生卡查询"};
    private MainAdapter mAdapter;

    private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        initRecyclerView();

        AlarmUtil.register(this);
    }

    public void initRecyclerView() {
        mAdapter = new MainAdapter(mdesc, mpics);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new MyItemDecoration());
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
                        if (!App.sLibrarayUser.getSid().equals("0")) {
                            intent = new Intent(MainActivity.this, MineActivity.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(MainActivity.this, LibraryLoginActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, ScoreActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        Intent intent3 = new Intent(MainActivity.this, ElectricityActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        Intent intent2 = WebViewActivity.newIntent(MainActivity.this, "http://xueer.ccnuer.cn", "学而");
                        startActivity(intent2);
                        break;

                }
            }
        });

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
