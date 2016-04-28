package net.muxi.huashiapp.common.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.adapter.NewsRecyclerViewAdapter;
import net.muxi.huashiapp.common.util.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by december on 16/4/26.
 */
public class NewsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<News> mList;
    private NewsRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        initPersonData();

        mAdapter = new NewsRecyclerViewAdapter(mList, NewsActivity.this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void initPersonData() {
        mList = new ArrayList<>();
        mList.add(new News(getString(R.string.news_one_title),getString(R.string.news_one_desc),R.drawable.p1));
        mList.add(new News(getString(R.string.news_two_title),getString(R.string.news_two_desc),R.drawable.p2));
        mList.add(new News(getString(R.string.news_three_title),getString(R.string.news_tree_desc),R.drawable.p3));
        mList.add(new News(getString(R.string.news_one_title),getString(R.string.news_one_desc),R.drawable.p4));


    }
}
