package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.BookSearchResult;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.utils.CommonTextUtils;
import com.muxistudio.appcommon.widgets.LoadingDialog;
import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/2/13.
 */

public class LibrarySearchResultActivity extends ToolbarActivity implements
        AbsListView.OnScrollListener {

    private ListView mListView;
    private List<BookSearchResult.ResultsBean> mBookList;
    private LibrarySearchResultAdapter mLibrarySearchResultAdapter;

    private LoadingDialog mLoadingDialog ;


    private String query;
    private int page = 1;
    private int max = 5;
    private int preLastItem = 0;

    private TextView footerView;
    private MultiStatusView mMultiStatusView;

    public static void start(Context context, String query) {
        Intent starter = new Intent(context, LibrarySearchResultActivity.class);
        starter.putExtra("query", query);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_result);
        initView();
        setTitle("搜索结果");
        mLoadingDialog =  showLoading(CommonTextUtils.generateRandomLoginText());

        query = getIntent().getStringExtra("query");
        mBookList = new ArrayList<>();
        mLibrarySearchResultAdapter = new LibrarySearchResultAdapter(this, mBookList);
        footerView = (TextView) LayoutInflater.from(this).inflate(R.layout.view_footer, null);

        mMultiStatusView.setOnRetryListener(view -> {
            page = 1;
            mLoadingDialog = showLoading(CommonTextUtils.generateRandomLoginText());
            loadBooks();
        });
        loadBooks();
                                     }

    private void loadBooks() {
        Subscription subscription  = CampusFactory.getRetrofitService().searchBook(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bookSearchResult -> {
                    try {
                        max = bookSearchResult.getMeta().getMax();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (page == 1) {
                        mMultiStatusView.showContent();
                        mListView = (ListView) mMultiStatusView.getContentView();
                        mListView.setOnScrollListener(this);
                        mListView.setAdapter(mLibrarySearchResultAdapter);
                        mListView.addFooterView(footerView, null, false);
                        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
                            if(mBookList!=null && mBookList.size() != 0) {
                                BookSearchResult.ResultsBean book = mBookList.get(i);
                                BookDetailActivity.start(LibrarySearchResultActivity.this, book.id);
                            }
                        });
                    } else if (page >= max) {
                        footerView.setText("－到底啦，没有更多啦－");
                    }
                    mListView = (ListView) mMultiStatusView.getContentView();
                    mBookList.addAll(bookSearchResult.getResult());
                    mLibrarySearchResultAdapter.notifyDataSetChanged();
                }, throwable -> {
                    throwable.printStackTrace();
                    mMultiStatusView.showError();
                    mMultiStatusView.setOnRetryListener(view -> {
                        page = 1;
                        mLoadingDialog = showLoading(CommonTextUtils.generateRandomLoginText());
                        loadBooks();
                    });
                }, () -> {
                    hideLoading();
                });

        mLoadingDialog.setOnSubscriptionCanceledListener(() ->{
          if(!subscription.isUnsubscribed())
            subscription.unsubscribe();
      });
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        if (absListView.getId() == R.id.view_content) {
            int lastItem = i + i1;
            if (lastItem == i2) {
                if (preLastItem != lastItem && page <= max) {
                    page++;
                    loadBooks();
                    preLastItem = lastItem;
                }
            }
        }
    }

    private void initView() {
        mMultiStatusView = findViewById(R.id.multi_status_view);
    }
}
