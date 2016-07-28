package net.muxi.huashiapp.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.News;
import net.muxi.huashiapp.common.net.CampusFactory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        init();
        CampusFactory.getRetrofitService().getNews()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<News>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<News> newsList) {
                        setupRecyclerView(newsList);

                    }
                });




    }

    public void init() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("消息公告");
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mNewsRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());
    }

    private void setupRecyclerView(List<News> newsList){
        MyNewsAdapter adapter = new MyNewsAdapter(newsList);
        mNewsRecyclerView.setAdapter(adapter);
    }
}
