package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.Book;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.ui.library.fragment.BookBorrowedFragment;
import net.muxi.huashiapp.ui.library.fragment.BookDetailFragment;
import net.muxi.huashiapp.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 根据传入的 id 比对本地关注,借阅 id 列表 显示页面
 * Created by ybao on 17/2/15.
 */

public class BookDetailActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.multi_status_view)
    MultiStatusView mMultiStatusView;

    private String id;

    public static void start(Context context, String id) {
        Intent starter = new Intent(context, BookDetailActivity.class);
        starter.putExtra("id", id);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);
        setTitle("");
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
                    if (PreferenceUtil.getString(PreferenceUtil.BORROW_BOOK_IDS).contains(id)){
                        showBorrowedBookFragment(book);
                    }else{
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
        switch (item.getItemId()) {
            case R.id.action_close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
