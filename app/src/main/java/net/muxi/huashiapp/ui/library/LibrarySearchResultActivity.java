package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.BookSearchResult;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.util.Logger;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/2/13.
 */

public class LibrarySearchResultActivity extends ToolbarActivity implements
        AbsListView.OnScrollListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.multi_status_view)
    MultiStatusView mMultiStatusView;

    private ListView mListView;
    private List<BookSearchResult.ResultsBean> mBookList;
    private LibrarySearchResultAdapter mLibrarySearchResultAdapter;

    private String query;
    private int page = 1;
    private int max = 5;
    private int preLastItem = 0;

    private TextView footerView;

    public static void start(Context context, String query) {
        Intent starter = new Intent(context, LibrarySearchResultActivity.class);
        starter.putExtra("query", query);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_result);
        ButterKnife.bind(this);
        showLoading();

        query = getIntent().getStringExtra("query");
        mBookList = new ArrayList<>();
        mLibrarySearchResultAdapter = new LibrarySearchResultAdapter(this,mBookList);
        footerView = (TextView) LayoutInflater.from(this).inflate(R.layout.view_footer, null);

        mMultiStatusView.setOnRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                showLoading();
                loadBooks();
            }
        });
        loadBooks();
    }

    private void loadBooks() {
        CampusFactory.getRetrofitService().searchBook(query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bookSearchResult -> {
                    max = bookSearchResult.getMeta().getMax();
                    if (page == 1) {
                        mMultiStatusView.showContent();
                        mListView = (ListView) mMultiStatusView.getContentView();
                        mListView.setOnScrollListener(this);
                        mListView.setAdapter(mLibrarySearchResultAdapter);
                        mListView.addFooterView(footerView);
                        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
                            BookSearchResult.ResultsBean book = mBookList.get(i);
                            BookDetailActivity.start(LibrarySearchResultActivity.this,book.id);
                        });
                    } else if (page >= max) {
                        footerView.setText("－到底啦，没有更多啦－");
                    }
                    mListView = (ListView) mMultiStatusView.getContentView();
                    mBookList.addAll(bookSearchResult.getResults());
                    mLibrarySearchResultAdapter.notifyDataSetChanged();
                }, throwable -> {
                    throwable.printStackTrace();
                    mMultiStatusView.showError();
                    mMultiStatusView.setOnRetryListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            page = 1;
                            showLoading();
                            loadBooks();
                        }
                    });
                }, () -> {
                    hideLoading();
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
}
