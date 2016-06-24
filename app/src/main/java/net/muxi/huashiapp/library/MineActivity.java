package net.muxi.huashiapp.library;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.db.HuaShiDao;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/15.
 */
public class MineActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.search_view)
    MySearchView mSearchView;
    @Bind(R.id.toolbar_container)
    FrameLayout mToolbarContainer;
    private HuaShiDao dao;
    String[] suggestions;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_mine);
        ButterKnife.bind(this);
        dao = new HuaShiDao();
        initView();


    }


    private void initView() {

        mToolbar.setTitle("我的图书馆");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(mToolbar);

        suggestions = dao.loadSearchHistory().toArray(new String[0]);
        mSearchView.setSuggestions(suggestions);
        mSearchView.setOnQueryTextListener(new MySearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dao.insertSearchHistory(query);
                Intent intent = new Intent(MineActivity.this,LibraryActivity.class);
                intent.putExtra(AppConstants.LIBRARY_QUERY_TEXT,query);
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
        getMenuInflater().inflate(R.menu.menu_library, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()){
            mSearchView.closeSearchView();
            return;
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
