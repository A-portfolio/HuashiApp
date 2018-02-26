package net.muxi.huashiapp.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.News;
import net.muxi.huashiapp.net.CampusFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 16/4/26.
 */
public class NewsActivity extends ToolbarActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.news_recycler_view)
    RecyclerView mNewsRecyclerView;
    @BindView(R.id.content_layout)
    RelativeLayout mContentLayout;


    public static void start(Context context) {
        Intent starter = new Intent(context, NewsActivity.class);
        context.startActivity(starter);
    }


    private NewsDetailView newsDetailView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        init();


        showLoading();
        CampusFactory.getRetrofitService().getNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(news -> setupRecyclerView(news),Throwable::printStackTrace, this::hideLoading);
    }

    public void init() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("校园通知");
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupRecyclerView(List<News> newsList) {
        MyNewsAdapter adapter;
        adapter = new MyNewsAdapter(newsList);
        mNewsRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, newsList1, position) -> {
            newsDetailView = new NewsDetailView(NewsActivity.this, newsList1, position);
            Animation animation = AnimationUtils.loadAnimation(NewsActivity.this, R.anim.view_show);
            newsDetailView.startAnimation(animation);
            mContentLayout.addView(newsDetailView);
        });
    }


    @Override
    public void onBackPressed() {
        if (getWindow().getDecorView().equals(newsDetailView)) {
            mContentLayout.removeView(newsDetailView);
        } else {
            super.onBackPressed();
        }
    }

}

