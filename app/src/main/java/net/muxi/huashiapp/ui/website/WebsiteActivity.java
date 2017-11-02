package net.muxi.huashiapp.ui.website;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.WebsiteData;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.ui.webview.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 16/11/2.
 */

public class WebsiteActivity extends ToolbarActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    public static void start(Context context){
        Intent starter = new Intent(context,WebsiteActivity.class);
        context.startActivity(starter);
    }

    private HuaShiDao mDao;
    private List<WebsiteData> mWebsiteDatas;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        ButterKnife.bind(this);

        mDao = new HuaShiDao();
        try {
            mWebsiteDatas = mDao.loadSite();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mWebsiteDatas != null && mWebsiteDatas.size() > 0) {
            //学生信息服务平台暂时无法使用
            setupRecyclerView(filterData(mWebsiteDatas));
        } else {
            showLoading();
        }


        setTitle("常用网站");
        CampusFactory.getRetrofitService().getWebsite()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<WebsiteData>>() {
                    @Override
                    public void onCompleted() {
                        hideLoading();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showSnackbarShort(getString(R.string.tip_net_error));
                    }

                    @Override
                    public void onNext(List<WebsiteData> websiteData) {
                        for(WebsiteData data:websiteData){

                        Log.d("websites", "onNext: "+data.getSite());
                        }
                        if (mWebsiteDatas == null || websiteData.size() != mWebsiteDatas.size()) {
                            //学生信息服务平台暂时无法使用
                            setupRecyclerView(filterData(mWebsiteDatas));
                            mDao.deleteWebsite();
                            for (WebsiteData data : websiteData) {
                                mDao.insertSite(data);
                            }
                        }
                    }
                });
    }

    public void setupRecyclerView(List<WebsiteData> websiteData) {
        WebsiteAdapter adapter;
        adapter = new WebsiteAdapter(websiteData);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(new WebsiteAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, List<WebsiteData> websiteData, int position) {
                Intent intent = WebViewActivity.newIntent(WebsiteActivity.this, websiteData.get(position).getUrl());
                startActivity(intent);
            }
        });

    }

    private List filterData(List<WebsiteData> dataList){
        List<WebsiteData> filteredList = new ArrayList<>();
        for(WebsiteData data:dataList){
            if(!data.getSite().equals("学生信息服务平台")){
                filteredList.add(data);
            }
        }
        return filteredList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
