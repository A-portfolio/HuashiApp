package net.muxi.huashiapp.library;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.data.PersonalBook;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.Base64Util;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.NetStatus;
import net.muxi.huashiapp.main.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/5/15.
 */
public class MineActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.search_view)
    MySearchView mSearchView;
    @BindView(R.id.toolbar_container)
    FrameLayout mToolbarContainer;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.img_empty)
    ImageView mImgEmpty;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private HuaShiDao dao;
    String[] suggestions;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_mine);
        ButterKnife.bind(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        dao = new HuaShiDao();
        if (NetStatus.isConnected()) {
            CampusFactory.getRetrofitService().getPersonalBook(Base64Util.createBaseStr(App.sLibrarayUser))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<PersonalBook>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            mSwipeRefreshLayout.setRefreshing(false);
                            setImgEmpty(getString(R.string.tip_school_server_error));
                        }

                        @Override
                        public void onNext(List<PersonalBook> personalBooks) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            Logger.d("get personal");
                            if (personalBooks.size() > 0 ) {
                                setupRecyclerview(personalBooks);
                            }else {
                                setImgEmpty(getString(R.string.lib_mine_none_book));
                            }
                        }
                    });
        }else {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        initView();
    }

    private void setupRecyclerview(List<PersonalBook> personalBooks) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        MyBookAdapter bookAdapter = new MyBookAdapter(personalBooks);
        mRecyclerView.setAdapter(bookAdapter);
    }

    public void setImgEmpty(String tip){
        mImgEmpty.setVisibility(View.VISIBLE);
        mTvEmpty.setVisibility(View.VISIBLE);
        mTvEmpty.setText(tip);
    }

    private void initView() {

        mToolbar.setTitle("我的图书馆");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(mToolbar);

        suggestions = dao.loadSearchHistory().toArray(new String[0]);
        mSearchView.setSuggestions(suggestions);
        mSearchView.setOnSearchViewListener(new MySearchView.OnSearchViewListener() {
            @Override
            public void onSearchShown() {
                suggestions = dao.loadSearchHistory().toArray(new String[0]);
                mSearchView.setSuggestions(suggestions);
            }

            @Override
            public void onSeachClose() {

            }
        });
        mSearchView.setOnQueryTextListener(new MySearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dao.insertSearchHistory(query);
                mSearchView.closeSearchView();
                Intent intent = new Intent(MineActivity.this, LibraryActivity.class);
                intent.putExtra(AppConstants.LIBRARY_QUERY_TEXT, query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
//        mSearchView.setSuggestions(dao.loadSearchHistory());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_library_login, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_logout) {
            App.clearLibUser();
            Logger.d(App.sLibrarayUser.getSid());
            Intent intent = new Intent(MineActivity.this, LibraryLoginActivity.class);
            startActivity(intent);
            MineActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearchView();
            return;
        }
        if (App.sLibrarayUser.getSid() != null && !App.sLibrarayUser.getSid().equals("0")) {
            Intent intent = new Intent(MineActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
