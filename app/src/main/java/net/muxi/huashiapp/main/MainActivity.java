package net.muxi.huashiapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.library.LibraryActivity;
import net.muxi.huashiapp.news.NewsActivity;
import net.muxi.huashiapp.schedule.ScheduleActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private int[] mpics = {R.drawable.t, R.drawable.t,
            R.drawable.t, R.drawable.t,
            R.drawable.t, R.drawable.t};

    private String[] mdesc = {"课程表", "图书查询", "成绩查询", "电费查询", "校历查询", "部门信息"};
    private MainAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        initRecyclerView();

    }

    public void initRecyclerView(){
        mAdapter = new MainAdapter(mdesc, mpics);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new MyItemDecoration());
        mAdapter.setItemClickListener(new MainAdapter.ItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this,ScheduleActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(MainActivity.this, LibraryActivity.class);
                        startActivity(intent1);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_news:
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(intent);
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}
