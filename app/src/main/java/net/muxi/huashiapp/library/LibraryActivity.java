package net.muxi.huashiapp.library;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.OnItemClickListener;
import net.muxi.huashiapp.common.data.Book;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.util.ToastUtil;

import br.com.mauker.materialsearchview.MaterialSearchView;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/1.
 */
public class LibraryActivity extends AppCompatActivity implements MaterialSearchView.OnQueryTextListener {

    // TODO: 16/5/3 material searchView has bug ...

    @Bind(R.id.searchview)
    MaterialSearchView mSearchview;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.root_layout)
    RelativeLayout mRootLayout;
    @Bind(R.id.frame_layout)
    FrameLayout mFrameLayout;

    //设置fragment 的高度
    public static final int FRAGMENT_HEIGHT =
            DimensUtil.getScreenHeight() - DimensUtil.getStatusBarHeight() - DimensUtil.getActionbarHeight() - DimensUtil.dp2px(50);
    //设置 fragment 距离actionbar的距离
    public static final int ACTIONBAR_DISTANCE = DimensUtil.dp2px(50);


    private MaterialSearchView.OnQueryTextListener mOnQueryTextListener;
    private LibraryAdapter mLibraryAdapter;
    private View animView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.bind(this);

        setupRecyclerview();

        mSearchview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("search", "searchview has been clicked");
            }
        });


        mToolbar.setTitle("图书馆");
        setSupportActionBar(mToolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);

    }

    private void setupRecyclerview() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        String[] s = new String[17];
        for (int i = 0; i < 17; i++) {
            s[i] = "it is the title " + i;
        }
        mLibraryAdapter = new LibraryAdapter(s);
        mRecyclerView.setAdapter(mLibraryAdapter);


        mLibraryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Book book) {
                int viewTop = view.getTop();
                int viewHeight = view.getBottom() - view.getTop();
//                view.animate().alpha(0).setDuration(200);
                animView = new View(LibraryActivity.this);
                animView.setBackgroundColor(Color.BLUE);
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        viewHeight
                );
                params.setMargins(0, viewTop, 0, 0);
                mFrameLayout.addView(animView, params);

                ScaleAnimation scaleAnimation = new ScaleAnimation(
                        1,
                        1,
                        1,
                        FRAGMENT_HEIGHT / viewHeight,
                        0,
                        (int) ((viewTop - ACTIONBAR_DISTANCE) * 1.0 * viewHeight / FRAGMENT_HEIGHT));
                ToastUtil.showShort("" + (viewTop - ACTIONBAR_DISTANCE) / FRAGMENT_HEIGHT * viewHeight);
                scaleAnimation.setDuration(200);
                animView.startAnimation(scaleAnimation);

            }
        });


    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d("library", "library");
//        FragmentManager fm = getSupportFragmentManager();
//        Log.d("libraray","feng");
//        FragmentTransaction ft = fm.beginTransaction();
        //TODO: 16/5/3  make the newInstance have the arguments
//        ft.replace(R.id.root_layout,LibraryListFragment.newInstance());
//        ft.addToBackStack(null);
//        ft.commit();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                mSearchview.openSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSearchview.isOpen()) {
                mSearchview.closeSearch();
                return false;
            }
        }
        return super.onKeyUp(keyCode, event);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mSearchview.clearSuggestions();
    }
}
