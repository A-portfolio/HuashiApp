package net.muxi.huashiapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.ui.SettingActivity;
import net.muxi.huashiapp.electricity.ElectricityActivity;
import net.muxi.huashiapp.library.LibrarySearchActivity;
import net.muxi.huashiapp.news.NewsActivity;
import net.muxi.huashiapp.schedule.ScheduleActivity;
import net.muxi.huashiapp.webview.WebViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private int[] mpics = {R.drawable.t, R.drawable.t,
            R.drawable.t, R.drawable.t,
            R.drawable.t, R.drawable.t, R.drawable.t, R.drawable.t};

    private String[] mdesc = {"课程表", "图书查询", "成绩查询", "电费查询", "校历查询", "部门信息", "学而", "学生卡查询"};
    private MainAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        initRecyclerView();

    }

    public void initRecyclerView() {
        mAdapter = new MainAdapter(mdesc, mpics);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new MyItemDecoration());
        mAdapter.setItemClickListener(new MainAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(MainActivity.this, LibrarySearchActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = WebViewActivity.newIntent(MainActivity.this, "http://xueer.ccnuer.cn", "学而");
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(MainActivity.this, ElectricityActivity.class);
                        startActivity(intent3);

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
        }
        return super.onOptionsItemSelected(item);
    }
}
