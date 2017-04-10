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
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.data.AttentionBook;
import net.muxi.huashiapp.common.data.PersonalBook;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.ui.library.BookDetailActivity;
import net.muxi.huashiapp.ui.library.adapter.AttenBookAdapter;
import net.muxi.huashiapp.ui.library.adapter.AttenBookRemindAdapter;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.PreferenceUtil;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
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

    public static MyBookListFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        MyBookListFragment fragment = new MyBookListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mybooks, container, false);
        ButterKnife.bind(this, view);

        type = getArguments().getInt("type");
        if (TYPE_BORROW == type) {
            initBorrowView();
        } else {
            initAttentionView();
        }

        return view;
    }

    private void initAttentionView() {
        mMultiStatusView.setOnRetryListener(v -> loadAttentionBooks());
        loadAttentionBooks();
    }

    public void loadAttentionBooks() {
        mMultiStatusView.showContent();
        CampusFactory.getRetrofitService().getAttentionBooks(
                Base64Util.createBaseStr(App.sLibrarayUser))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(attentionBooksResponse -> {
                    switch (attentionBooksResponse.code()) {
                        case 200:
                            List<AttentionBook> attentionBooks = attentionBooksResponse.body();
                            if (attentionBooks.size() == 0) {
                                throw new RuntimeException();
                            }
                            Observable.from(attentionBooks)
                                    .toSortedList((attentionBook, attentionBook2) -> {
                                        if (!attentionBook.avbl.equals(attentionBook2.avbl)) {
                                            if (attentionBook.avbl.equals("n")) {
                                                return 1;
                                            } else {
                                                return -1;
                                            }
                                        }
                                        return 0;
                                    })
                                    .subscribe(attentionBooks1 -> {
                                        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(
                                                getContext(),
                                                attentionBooks1);
                                        adapter.addItemViewDelegate(new AttenBookRemindAdapter(getContext()));
                                        adapter.addItemViewDelegate(new AttenBookAdapter(getContext()));
                                        mRecyclerView =
                                                (RecyclerView) mMultiStatusView.getContentView();
                                        mRecyclerView.setLayoutManager(
                                                new LinearLayoutManager(getContext()));
                                        mRecyclerView.setHasFixedSize(true);
                                        mRecyclerView.setAdapter(adapter);
                                    }, throwable -> throwable.printStackTrace());

                            //关注的图书 id 存储到本地
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
        loadBorrowBooks();
    }

    public void loadBorrowBooks() {
        mMultiStatusView.showContent();
        CampusFactory.getRetrofitService().getPersonalBook(
                Base64Util.createBaseStr(App.sLibrarayUser))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(personalBooks -> {
                    if (personalBooks.size() == 0) {
                        throw new RuntimeException();
                    }
                    mRecyclerView = (RecyclerView) mMultiStatusView.getContentView();
                    Collections.sort(personalBooks, ((personalBook, t1) -> {
                        return personalBook.time - t1.time;
                    }));
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    mRecyclerView.setHasFixedSize(true);
                    mRecyclerView.setAdapter(new CommonAdapter<PersonalBook>(getContext(),
                            R.layout.item_my_book_remind, personalBooks) {
                        @Override
                        protected void convert(ViewHolder holder, PersonalBook personalBook,
                                int position) {
                            setBorrowItem(holder, personalBook, position);
                        }
                    });
                    //借阅的 ids 存储到本地
                    Observable.from(personalBooks).map(personalBook -> personalBook.id)
                            .toList()
                            .subscribe(strings -> PreferenceUtil.saveString(
                                    PreferenceUtil.BORROW_BOOK_IDS, TextUtils.join(",", strings)));
                }, throwable -> {
                    throwable.printStackTrace();
                    mMultiStatusView.showEmpty();
                });
    }

    private void setBorrowItem(ViewHolder holder, PersonalBook personalBook, int position) {
        holder.setText(R.id.tv_title, personalBook.book);
        String s = personalBook.time < 10 ? "0" + personalBook.time : String.valueOf(
                personalBook.time);
        holder.setText(R.id.tv_remind, String.format("时间剩余%s天", s));
        if (personalBook.time < 3) {
            ((TextView) holder.getView(R.id.tv_remind)).setTextColor(
                    getResources().getColor(R.color.red));
        }
        holder.getView(R.id.layout_item).setOnClickListener(v -> {
            BookDetailActivity.start(getContext(), personalBook.id);
        });
        holder.getView(R.id.layout_item).setClickable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.d("book mylist");
    }
}
