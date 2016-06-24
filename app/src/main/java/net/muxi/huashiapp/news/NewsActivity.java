package net.muxi.huashiapp.news;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by december on 16/4/26.
 */
public class NewsActivity extends ToolbarActivity {


    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.news_recycler_view)
    RecyclerView mNewsRecyclerView;
    private List<String> mDatas;
    MyNewsAdapter mMyNewsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        mToolbar.setTitle("消息公告");
        initData();
        initRecyclerView();


    }

    public void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    public void initRecyclerView() {
        mMyNewsAdapter = new MyNewsAdapter(mDatas);
        mNewsRecyclerView.setAdapter(mMyNewsAdapter);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
    }
}
