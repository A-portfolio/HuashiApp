package net.muxi.huashiapp.library;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.OnItemClickListener;
import net.muxi.huashiapp.common.base.BaseDetailLayout;
import net.muxi.huashiapp.common.data.Book;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.util.AlarmUtil;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.widget.ShadowView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by ybao on 16/5/1.
 */
public class LibraryActivity extends AppCompatActivity {

    // TODO: 16/5/3 material searchView has bug ...

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.searchview)
    MySearchView mSearchview;
    @Bind(R.id.root_layout)
    FrameLayout mRootLayout;
    @Bind(R.id.content_layout)
    FrameLayout mContentLayout;

    private FrameLayout contentLayout;

    //设置fragment 的高度
    public static final int FRAGMENT_HEIGHT =
            DimensUtil.getScreenHeight() - DimensUtil.getStatusBarHeight() - DimensUtil.dp2px(48);
    //设置 fragment 距离actionbar的距离
    public static final int ACTIONBAR_DISTANCE = DimensUtil.dp2px(48) - DimensUtil.getActionbarHeight();
    //animView 扩张的时间

    public static final int DURATION_SCALE = 250;
    public static final int DURATION_ALPH = 250;

    //点击后详情页布局
    private RelativeLayout detailLayout;
    private Toolbar detailToolbar;

    private LibraryAdapter mLibraryAdapter;
    private View animView;

    private HuaShiDao dao;

    private View mShadowView;

    private String[] suggestions;

    private int my;
    private int curY;

    private BaseDetailLayout mBaseDetailLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.bind(this);

        contentLayout = (FrameLayout) findViewById(android.R.id.content);
        setupRecyclerview();

        dao = new HuaShiDao();

        mSearchview.setOnQueryTextListener(new MySearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dao.insertSearchHistory(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchview.setOnSearchViewListener(new MySearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                mSearchview.showSuggestions();
            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        AlarmUtil.register(this);

        mToolbar.setTitle("图书馆");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(mToolbar);

//        mSearchview.setSuggestions(dao.loadSearchHistory());
//        suggestions = dao.loadSearchHistory();
        mSearchview.setSuggestions(suggestions);
        App.setCurrentActivity(this);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);

    }


    private void fadeOutItem(View view){
        AlphaAnimation animation = new AlphaAnimation(1,0);
        animation.setDuration(DURATION_ALPH);
        view.startAnimation(animation);
    }

    private void slideUpToolbar() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -DimensUtil.getActionbarHeight());
        animation.setDuration(DURATION_SCALE);
        animation.setFillBefore(true);
        animation.setFillAfter(true);
        detailToolbar.startAnimation(animation);
    }

    private void slideDownToolbar() {
        TranslateAnimation animation = new TranslateAnimation(0, 0, -DimensUtil.getActionbarHeight(), 0);
        animation.setDuration(DURATION_SCALE);
        animation.setFillAfter(true);
        detailToolbar.startAnimation(animation);
    }

    private void initDetailToolbar(String title) {
        if (detailToolbar == null) {
            detailToolbar = new Toolbar(LibraryActivity.this);
            detailToolbar.setTitle(title);
            detailToolbar.setTitleTextColor(Color.WHITE);
            detailToolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
            detailToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            RelativeLayout.LayoutParams toolbarParams = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            toolbarParams.setMargins(0, -DimensUtil.getActionbarHeight(), 0, 0);
            contentLayout.addView(detailToolbar, toolbarParams);
        }

    }

    private void setupRecyclerview() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        String[] s = new String[17];
        for (int i = 0; i < 17; i++) {
            s[i] = "title " + i;
        }
        mLibraryAdapter = new LibraryAdapter(s);
        mRecyclerView.setAdapter(mLibraryAdapter);

        mLibraryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Book book) {

                // TODO: 16/5/15 hide item not show after
                fadeOutItem(view);

                startScale(view);
                Observable.timer(DURATION_SCALE, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Long>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();

                            }

                            @Override
                            public void onNext(Long aLong) {
                                addShadowView();
                                mBaseDetailLayout = new BaseDetailLayout(LibraryActivity.this);
                                mContentLayout.addView(mBaseDetailLayout);
                            }
                        });

            }

        });
    }

    private void addShadowView() {
        mShadowView = new ShadowView(LibraryActivity.this);
        mShadowView.setBackgroundColor(Color.BLACK);
        mShadowView.setAlpha(0.6f);
        mContentLayout.addView(mShadowView);
    }


    //强制 scrollview 滑动
    public void forceScrollTo(final ScrollView scrollView, final int deltaY) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, deltaY);
            }
        });
    }


    private void startScale(View view) {
        int viewTop = view.getTop();
        int viewHeight = view.getHeight();
        animView = new View(LibraryActivity.this);
        animView.setBackgroundColor(Color.WHITE);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                viewHeight
        );
        params.setMargins(0, viewTop + DimensUtil.getActionbarHeight(), 0, 0);
        contentLayout.addView(animView, params);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1,
                1,
                1,
                FRAGMENT_HEIGHT / (float)viewHeight,
                0,
                (float) ((viewTop - ACTIONBAR_DISTANCE) * 1.0 /  ((float)FRAGMENT_HEIGHT * 1.0 /(float) viewHeight - 1))
        );

        scaleAnimation.setDuration(DURATION_SCALE);
        scaleAnimation.setFillAfter(true);
        animView.startAnimation(scaleAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animView != null) {
                    contentLayout.removeView(animView);
                }
            }
        },DURATION_SCALE);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_library, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchview.setMenuItem(item);
        return true;
    }


    //当当前有详情页显示时,改写后退键的方法
    @Override
    public void onBackPressed() {

        if (mContentLayout.getChildCount() > 0) {
            mBaseDetailLayout.slideContentView();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mContentLayout.removeView(mBaseDetailLayout);
                    if (mShadowView != null) {
                        mContentLayout.removeView(mShadowView);
                        mShadowView = null;
                    }
                }
            },250);
            return;
        }
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                return true;
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
