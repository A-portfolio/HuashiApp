package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.Book;
import net.muxi.huashiapp.common.net.CampusFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/2/15.
 */

public class BookDetailActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.multi_status_view)
    MultiStatusView mMultiStatusView;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_author)
    TextView mTvAuthor;
    @BindView(R.id.btn_attention)
    Button mBtnAttention;
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.tv_show_all)
    TextView mTvShowAll;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    private View mContentView;

    public static void start(Context context, String bid, String id, String book, String author) {
        Intent starter = new Intent(context, BookDetailActivity.class);
        starter.putExtra("id", id);
        starter.putExtra("book", book);
        starter.putExtra("author", author);
        starter.putExtra("bid", bid);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        setTitle("");
        showLoading();

        mMultiStatusView.setOnRetryListener(v -> {
            loadData();
        });
        loadData();
    }

    private void loadData() {
        Intent intent = getIntent();
        CampusFactory.getRetrofitService().getBookDetail(intent.getStringExtra("id"),
                intent.getStringExtra("book"),
                intent.getStringExtra("author"), intent.getStringExtra("bid"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(book -> {
                    mMultiStatusView.showContent();
                    mContentView = mMultiStatusView.getContentView();
                    ButterKnife.bind(this, mContentView);
                    mTvTitle.setText(book.book);
                    mTvAuthor.setText(book.author);
                    mTvInfo.setText(book.intro);
                    setupRecyclerView(book);
                }, throwable -> {
                    throwable.printStackTrace();
                    mMultiStatusView.showError();
                }, () -> {
                    hideLoading();
                });
    }

    private void setupRecyclerView(Book book) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookListAdapter bookListAdapter = new BookListAdapter(book);
        mRecyclerView.setAdapter(bookListAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
    }


    @Override
    protected boolean canBack() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
