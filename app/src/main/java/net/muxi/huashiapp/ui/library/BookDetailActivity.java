package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.Book;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.common.util.PreferenceUtil;
import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.library.fragment.BookBorrowedFragment;
import net.muxi.huashiapp.ui.library.fragment.BookDetailFragment;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 根据传入的 id 比对本地关注,借阅 id 列表 显示页面
 * Created by ybao on 17/2/15.
 */

public class BookDetailActivity extends ToolbarActivity {

    private String id;
    private MultiStatusView mMultiStatusView;
    private FrameLayout mContentLayout;

    public static void start(Context context, String id) {
        Intent starter = new Intent(context, BookDetailActivity.class);
        starter.putExtra("id", id);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        setTitle("");
        initView();
        id = getIntent().getStringExtra("id");

        mMultiStatusView.setOnRetryListener(v -> {
            loadData();
        });
        loadData();
    }

    private void loadData() {
        showLoading();
        Intent intent = getIntent();
        CampusFactory.getRetrofitService().getBookDetail(intent.getStringExtra("id"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(book -> {
                    mMultiStatusView.showContent();
                    if (PreferenceUtil.getString(PreferenceUtil.BORROW_BOOK_IDS).contains(id)) {
                        showBorrowedBookFragment(book);
                    } else {
                        showDetailFragment(book);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    mMultiStatusView.showError();
                }, () -> {
                    hideLoading();
                });
    }

    public void showDetailFragment(Book book) {
        getSupportFragmentManager().beginTransaction().add(R.id.content_layout,
                BookDetailFragment.newInstance(book, id)).commit();
    }

    public void showBorrowedBookFragment(Book book) {
        getSupportFragmentManager().beginTransaction().add(R.id.content_layout,
                BookBorrowedFragment.newInstance(book, id)).commit();
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
        int id = item.getItemId();
        if (id == R.id.action_close){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mMultiStatusView = findViewById(R.id.multi_status_view);
        mContentLayout = findViewById(R.id.content_layout);
    }
}
