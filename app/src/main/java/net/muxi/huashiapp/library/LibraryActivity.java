package net.muxi.huashiapp.library;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.OnItemClickListener;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.Book;
import net.muxi.huashiapp.common.data.BookSearchResult;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.DimensUtil;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.common.widget.BaseDetailLayout;
import net.muxi.huashiapp.common.widget.DividerItemDecoration;
import net.muxi.huashiapp.common.widget.ShadowView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/5/1.
 */
public class LibraryActivity extends ToolbarActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.searchview)
    MySearchView mSearchview;
    @BindView(R.id.content_layout)
    FrameLayout mContentLayout;

    private BookDetailView mBookDetailView;

//    private List<BookSearchResult.ResultsBean> bookList;

    private List<BookSearchResult.ResultsBean> mBookList;
    private FrameLayout contentLayout;
    //设置fragment 的高度
    public static final int FRAGMENT_HEIGHT =
            DimensUtil.getScreenHeight() - DimensUtil.getStatusBarHeight() - DimensUtil.dp2px(48);
    //设置 fragment 距离actionbar的距离
    public static final int ACTIONBAR_DISTANCE = DimensUtil.dp2px(48) - DimensUtil.getActionbarHeight();
    //animView 扩张的时间

    public static final int DURATION_SCALE = 250;
    public static final int DURATION_ALPH = 180;

    //点击后详情页布局

    private LibraryAdapter mLibraryAdapter;
    private View animView;

    private HuaShiDao dao;

    private View mShadowView;

    private String[] suggestions;

    //关键字
    private String mKeyword;
    //页数
    private int mPage = 1;
    //最大页数
    private int mMax = 0;

    private BaseDetailLayout mBaseDetailLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        ButterKnife.bind(this);

        mBookList = new ArrayList<>();
        String query = getIntent().getStringExtra(AppConstants.LIBRARY_QUERY_TEXT);
        mKeyword = query;
        searchBook(query);
        contentLayout = (FrameLayout) findViewById(android.R.id.content);

        initVariables();
        initViews();

    }

    private void initViews() {
        mSearchview.setSuggestions(suggestions);
        mSearchview.setTintViewBackground(getResources().getColor(android.R.color.transparent));
        mSearchview.setOnQueryTextListener(new MySearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dao.insertSearchHistory(query);
                mSearchview.closeSearchView();
                mSearchview.hideKeyboard(mSearchview);
                mPage = 1;
                mKeyword = query;
//                searchBook(query);
                mRecyclerView.scrollToPosition(0);
                loadData(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mToolbar.setTitle("图书馆");
        setSupportActionBar(mToolbar);


    }

    private void initVariables() {
        dao = new HuaShiDao();
        suggestions = dao.loadSearchHistory().toArray(new String[0]);

    }

    private void searchBook(String query) {
        CampusFactory.getRetrofitService().searchBook(query, mPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookSearchResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(BookSearchResult bookSearchResult) {
                        if (bookSearchResult != null) {
                            mBookList.addAll(bookSearchResult.getResults());
                            setupRecyclerview();
                            mMax = bookSearchResult.getMeta().getMax();
                        } else {
                            ToastUtil.showLong("无相关符合图书");

                        }
                    }
                });
    }

    private void setupRecyclerview() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mLibraryAdapter = new LibraryAdapter(mBookList);
        mRecyclerView.setAdapter(mLibraryAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.addOnScrollListener(getOnBottomListener());
        mLibraryAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, BookSearchResult.ResultsBean resultsBean) {
                Logger.d("id:" + resultsBean.getId() + " book:" + resultsBean.getBook() + " author" + resultsBean.getAuthor());
                String bookTitle = interceptTitle(resultsBean.getBook());
                final String published = resultsBean.getIntro();
                CampusFactory.getRetrofitService().getBookDetail(resultsBean.getId(), bookTitle, resultsBean.getAuthor(),resultsBean.getBid())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.newThread())
                        .subscribe(new Observer<Book>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(Book book) {
                                Logger.d(book.getBook());
                                setDetailData(book,published);
                            }
                        });

                //当阴影的 view 为null时创建
                if (mShadowView == null) {
                    final View itemView = view;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            addShadowView();
                            startScale(itemView);
                        }
                    }, DURATION_ALPH);

                    setupDetailLayout();
                }
            }

        });
    }

    //截取数字后面的书名
    private String interceptTitle(String book) {
        int index = book.indexOf(".");
        return book.substring(index + 1, book.length());
    }

    private RecyclerView.OnScrollListener getOnBottomListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                View lastChildView = recyclerView.getLayoutManager().getChildAt(
                        recyclerView.getChildCount() - 1
                );
                int lastViewBottom = lastChildView.getBottom();
                int recyclerviewBottom = recyclerView.getBottom();
                int positon = recyclerView.getLayoutManager().getPosition(lastChildView);
                Logger.d(positon + "  " + lastViewBottom + "  " + recyclerView.getTop());
                Logger.d(recyclerView.getLayoutManager().getItemCount() + "  " + recyclerviewBottom + "");
                if (lastViewBottom + recyclerView.getTop() + 5 >= recyclerviewBottom && positon == recyclerView.getLayoutManager().getItemCount() - 1) {
                    Logger.d(mPage + "");
                    if (mPage <= mMax - 1 && mPage <= mBookList.size() / 20) {
                        mPage++;
                        loadData(false);
                    } else {

                    }
                }
            }
        };
    }

    private void loadData(final boolean clean) {
        CampusFactory.getRetrofitService().searchBook(mKeyword, mPage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookSearchResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(BookSearchResult bookSearchResult) {
                        mMax = bookSearchResult.getMeta().getMax();
                        if (clean) {
                            mBookList.clear();
                        }
                        mBookList.addAll(bookSearchResult.getResults());
                        mLibraryAdapter.swap(mBookList);
                    }
                });
    }

    private void setupDetailLayout() {
        Observable.timer(DURATION_ALPH + DURATION_SCALE, TimeUnit.MILLISECONDS)
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
                        mBaseDetailLayout = new BaseDetailLayout(LibraryActivity.this);
                        mContentLayout.addView(mBaseDetailLayout);
                        mBookDetailView = new BookDetailView(LibraryActivity.this);
//                        mBookDetailView.setDraweeBg("res://net.muxi.huashiapp/" + R.drawable.img_lib_bookbg);
                        mBaseDetailLayout.setContent(mBookDetailView);
                        mBookDetailView.setOnCloseClickListener(new BookDetailView.OnCloseClickListener() {
                            @Override
                            public void onCloseClick() {
                                onBackPressed();
                            }
                        });
                    }
                });
    }

    private void setDetailData(Book book,String published){
//        BookDetailView bookDetailView = new BookDetailView(LibraryActivity.this,book,published);
//        mBaseDetailLayout.setContent(bookDetailView);
      mBookDetailView.setBookData(book,published);
    }

    private void addShadowView() {
        mShadowView = new ShadowView(LibraryActivity.this);
        mShadowView.setBackgroundColor(Color.BLACK);
        mShadowView.setAlpha(0.6f);
        mContentLayout.addView(mShadowView);
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
        animView.setBackgroundColor(Color.WHITE);

        final ScaleAnimation scaleAnimation = new ScaleAnimation(
                1,
                1,
                1,
                FRAGMENT_HEIGHT / (float) viewHeight,
                0,
                (float) ((viewTop - ACTIONBAR_DISTANCE) * 1.0 / ((float) FRAGMENT_HEIGHT * 1.0 / (float) viewHeight - 1))
        );

        scaleAnimation.setDuration(DURATION_SCALE);
        scaleAnimation.setFillAfter(true);

        animView.startAnimation(scaleAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animView != null) {
                    contentLayout.removeView(animView);
                    animView = null;
                }
            }
        }, DURATION_SCALE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (App.sLibrarayUser.getSid().equals("0")) {
            getMenuInflater().inflate(R.menu.menu_library_logout, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_library_login, menu);
        }
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchview.setMenuItem(item);
        return true;
    }


    //当当前有详情页显示时,改写后退键的方法
    @Override
    public void onBackPressed() {

        if (mBaseDetailLayout != null) {
            mBaseDetailLayout.slideContentView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mContentLayout.removeView(mBaseDetailLayout);
                    mContentLayout.removeView(mShadowView);
                    mShadowView = null;
                    mBaseDetailLayout = null;
                }
            }, 250);
            return;
        }

        if (mSearchview.isSearchOpen()) {
            mSearchview.closeSearchView();
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
                break;
            case R.id.action_logout:
                App.clearLibUser();
                Intent intent = new Intent(LibraryActivity.this, LibraryLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.action_login:
                this.finish();
                break;
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
