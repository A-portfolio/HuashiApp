package net.muxi.huashiapp.ui.library.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muxistudio.multistatusview.MultiStatusView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.data.AttentionBook;
import net.muxi.huashiapp.common.data.BorrowedBook;
import net.muxi.huashiapp.event.RefreshAttenBooks;
import net.muxi.huashiapp.event.RefreshBorrowedBooks;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.ui.library.BookDetailActivity;
import net.muxi.huashiapp.ui.library.adapter.AttenBookAdapter;
import net.muxi.huashiapp.ui.library.adapter.AttenBookRemindAdapter;
import net.muxi.huashiapp.ui.login.LoginPresenter;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/2/18.
 */

public class MyBookListFragment extends BaseFragment {

    @BindView(R.id.multi_status_view)
    MultiStatusView mMultiStatusView;

    private RecyclerView mRecyclerView;

    private int type;
    public static final int TYPE_BORROW = 0;
    public static final int TYPE_ATTENTION = 1;

    private Subscription s1;
    private Subscription s2;

    private List<AttentionBook> mAttentionBookList;
    private List<BorrowedBook> mBorrowedBookList;

    public static MyBookListFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        MyBookListFragment fragment = new MyBookListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mybooks, container, false);
        ButterKnife.bind(this, view);

        if (TYPE_BORROW == type) {
            initBorrowView();
            s1 = RxBus.getDefault().toObservable(RefreshBorrowedBooks.class)
                    .subscribe(refreshBorrowedBooks -> {
                        loadBorrowBooks();
                    });
        } else {
            initAttentionView();
            s2 = RxBus.getDefault().toObservable(RefreshAttenBooks.class)
                    .subscribe(refreshAttenBooks -> {
                        loadAttentionBooks();
                    });
        }
        return view;
    }

    private void initAttentionView() {
        mMultiStatusView.setOnRetryListener(v -> loadAttentionBooks());
        if (mAttentionBookList != null && mAttentionBookList.size() != 0) {
            renderAttentionBooks(mAttentionBookList);
        } else {
            loadAttentionBooks();
        }
    }

    public void loadAttentionBooks() {
        CampusFactory.getRetrofitService()
                .getAttentionBooks(App.sUser.sid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(attentionBooksResponse -> {
                    switch (attentionBooksResponse.code()) {
                        case 200:
                            List<AttentionBook> attentionBooks = attentionBooksResponse.body();
                            if (attentionBooks.size() == 0) {
                                throw new RuntimeException();
                            }
                            mAttentionBookList = attentionBooks;
                            Observable.from(attentionBooks)
                                    .toSortedList((attentionBook, attentionBook2) -> {
                                        if (!attentionBook.avb.equals(attentionBook2.avb)) {
                                            if (attentionBook.avb.equals("n")) {
                                                return 1;
                                            } else {
                                                return -1;
                                            }
                                        }
                                        return 0;
                                    })
                                    .subscribe(attentionBooks1 -> {
                                        renderAttentionBooks(attentionBooks1);
                                    }, throwable -> throwable.printStackTrace());

                            Observable.from(attentionBooks).map(attentionBook -> attentionBook.id)
                                    .toList()
                                    .subscribe(strings -> PreferenceUtil.saveString(
                                            PreferenceUtil.ATTENTION_BOOK_IDS,
                                            TextUtils.join(",", strings)));
                            break;
                        case 403:
                            ((BaseActivity) getActivity()).showErrorSnackbarShort(
                                    getString(R.string.tip_err_account));
                            throw new RuntimeException();
                        case 502:
                            ((BaseActivity) getActivity()).showErrorSnackbarShort(
                                    getString(R.string.tip_err_server));
                            throw new RuntimeException();
                        case 404:
                            throw new RuntimeException();
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    mMultiStatusView.showNetError();
                });
    }

    private void initBorrowView() {
        mMultiStatusView.setOnRetryListener(v -> {
            loadBorrowBooks();
        });
        if (mBorrowedBookList != null) {
            renderBorrowedBooks(mBorrowedBookList);
        } else {
            loadBorrowBooks();
        }
    }

    public void loadBorrowBooks() {
        CampusFactory.getRetrofitService()
                .getPersonalBook(App.PHPSESSID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(personalBooks -> {
                    if (personalBooks.size() == 0) {
                        throw new RuntimeException();
                    }
                    mBorrowedBookList = personalBooks;
                    renderBorrowedBooks(personalBooks);
                    //借阅的 ids 存储到本地
                    Observable.from(personalBooks).map(personalBook -> personalBook.id)
                            .toList()
                            .subscribe(strings -> PreferenceUtil.saveString(
                                    PreferenceUtil.BORROW_BOOK_IDS, TextUtils.join(",", strings)));
                }, throwable -> {
                    throwable.printStackTrace();
                    mMultiStatusView.showEmpty();
                    retryLoadBooks();
                });
    }

    public void retryLoadBooks(){
        new LoginPresenter().login(App.sUser)
                .flatMap(aBoolean -> CampusFactory.getRetrofitService()
                        .getPersonalBook(App.PHPSESSID)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()))
                .subscribe(personalBooks -> {
                    mBorrowedBookList = personalBooks;
                    renderBorrowedBooks(personalBooks);
                    //借阅的 ids 存储到本地
                    Observable.from(personalBooks).map(personalBook -> personalBook.id)
                            .toList()
                            .subscribe(strings -> PreferenceUtil.saveString(
                                    PreferenceUtil.BORROW_BOOK_IDS, TextUtils.join(",", strings)));
                });
    }

    public void renderBorrowedBooks(List<BorrowedBook> borrowedBookList) {
        if (borrowedBookList.size() == 0) {
            mMultiStatusView.showEmpty();
            return;
        }
        mMultiStatusView.showContent();
        mRecyclerView = (RecyclerView) mMultiStatusView.getContentView();
        Collections.sort(borrowedBookList, ((personalBook, t1) -> {
            return personalBook.time - t1.time;
        }));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new CommonAdapter<BorrowedBook>(getContext(),
                R.layout.item_my_book_remind, borrowedBookList) {
            @Override
            protected void convert(ViewHolder holder, BorrowedBook borrowedBook,
                    int position) {
                setBorrowItem(holder, borrowedBook, position);
            }
        });
    }

    public void renderAttentionBooks(List<AttentionBook> attentionBookList) {
        if (attentionBookList.size() == 0) {
            mMultiStatusView.showNetError();
        }
        mMultiStatusView.showContent();
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(
                getContext(),
                attentionBookList);
        adapter.addItemViewDelegate(new AttenBookRemindAdapter(getContext()));
        adapter.addItemViewDelegate(new AttenBookAdapter(getContext()));
        mRecyclerView =
                (RecyclerView) mMultiStatusView.getContentView();
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    private void setBorrowItem(ViewHolder holder, BorrowedBook borrowedBook, int position) {
        holder.setText(R.id.tv_title, borrowedBook.book);
        String s = borrowedBook.time < 10 ? "0" + borrowedBook.time : String.valueOf(
                borrowedBook.time);
        holder.setText(R.id.tv_remind, String.format("时间剩余%s天", s));
        if ( borrowedBook.time < 3&&borrowedBook.time>0 ) {
            ((TextView) holder.getView(R.id.tv_remind)).setTextColor(
                    getResources().getColor(R.color.red));
        }else{
            ((TextView) holder.getView(R.id.tv_remind)).setText
                    (String.format("已经超期%s天",Math.abs(borrowedBook.time)));
            ((TextView)holder.getView(R.id.tv_remind)).setTextColor
                    (getResources().getColor(R.color.red));
        }
        holder.getView(R.id.layout_item).setOnClickListener(v -> {
            BookDetailActivity.start(getContext(), borrowedBook.id);
        });
        holder.getView(R.id.layout_item).setClickable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (s1 != null && !s1.isUnsubscribed()) {
            s1.unsubscribe();
        }
        if (s2 != null && !s2.isUnsubscribed()) {
            s2.unsubscribe();
        }
    }
}
