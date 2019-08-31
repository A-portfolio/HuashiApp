package net.muxi.huashiapp.ui.library.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.TextView;

import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.appbase.BaseAppActivity;
import com.muxistudio.appcommon.appbase.BaseAppFragment;
import com.muxistudio.appcommon.data.Book;
import com.muxistudio.appcommon.data.BookId;
import com.muxistudio.appcommon.data.BookPost;
import com.muxistudio.appcommon.event.RefreshAttenBooks;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.appcommon.utils.MyBooksUtils;
import com.muxistudio.common.util.Logger;
import com.muxistudio.common.util.PreferenceUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.login.LoginActivity;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/2/21.
 */

public class BookDetailFragment extends BaseAppFragment {

    private Book mBook;
    private String id;

    private boolean isEllipsized = false;
    private boolean isLineOver = false;

    private boolean hasAttention = false;
    private boolean startAttenStatus = false;

    private BookPost mBookPost;
    private TextView mTvTitle;
    private TextView mTvAuthor;
    private CheckBox mBtnAttention;
    private TextView mTvInfo;
    private TextView mTvShowAll;
    private RecyclerView mRecyclerView;

    public static BookDetailFragment newInstance(Book book, String id) {
        Bundle args = new Bundle();
        args.putParcelable("book", book);
        args.putString("id", id);
        BookDetailFragment fragment = new BookDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        mTvTitle = view.findViewById(R.id.tv_book_title);
        mTvAuthor = view.findViewById(R.id.tv_author);
        mBtnAttention = view.findViewById(R.id.btn_attention);
        mTvInfo = view.findViewById(R.id.tv_info);
        mTvShowAll = view.findViewById(R.id.tv_show_all);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        mBook = getArguments().getParcelable("book");
        id = getArguments().getString("id");
        Logger.d(id);
        initView();

        return view;
    }

    private void initView() {
        mTvTitle.setText(mBook.book);
        mTvAuthor.setText(mBook.author);
        mTvInfo.setText(mBook.intro);

        //设置...
        ViewTreeObserver vto = mTvInfo.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(() -> {
            Layout l = mTvInfo.getLayout();
            if (l != null) {

                int lines = l.getLineCount();
                if (lines > 0) {
                    //被缩略的字符数量大于0
                    if (l.getEllipsisCount(lines - 1) > 0) {
                        Logger.d("text is ellips");
                        isEllipsized = true;
                        isLineOver = true;
                    } else {
                        isEllipsized = false;
                    }
                } else {
                    isEllipsized = false;
                }

            }
            if (!isLineOver) {
                mTvShowAll.setVisibility(View.GONE);
            } else {
                mTvShowAll.setOnClickListener(v -> {
                    if (isEllipsized) {
                        mTvInfo.setMaxLines(Integer.MAX_VALUE);
                        mTvShowAll.setText(R.string.fold_all);
                    } else {
                        mTvInfo.setMaxLines(2);
                        mTvShowAll.setText(R.string.expand_all);
                    }
                });
            }
        });

        if (PreferenceUtil.getString(PreferenceUtil.ATTENTION_BOOK_IDS, "").contains(id)) {
            mBtnAttention.setText(getString(R.string.has_atten));
            mBtnAttention.setChecked(true);
            hasAttention = true;
        } else {
            mBtnAttention.setText(getString(R.string.atten));
            hasAttention = false;
        }
        startAttenStatus = hasAttention;
        mBtnAttention.setOnClickListener(v -> {
            //ToastUtil.showShort("我的图书馆功能还在修复中~敬请期待");
            if (!UserAccountManager.getInstance().isInfoUserLogin()) {
                LoginActivity.start(getContext(), "lib");
                mBtnAttention.setChecked(false);
                return;
            }
            if (hasAttention) {
                delAtten();
//                changeAttenStatus(false);
            } else {
                createAtten();
//                changeAttenStatus(true);
            }
        });


        Logger.d(mBook.book);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(
                new CommonAdapter<Book.BooksBean>(getContext(), R.layout.item_book_state,
                        mBook.books) {

                    @Override
                    protected void convert(ViewHolder holder, Book.BooksBean booksBean,
                                           int position) {
                        TextView tvState = holder.getView(R.id.tv_state);
                        if (booksBean.status.equals(getString(R.string.can_borrow))) {
                            tvState.setText(R.string.can_borrow_book);
                            tvState.setTextColor(getResources().getColor(R.color.color_selected));
                            holder.getView(R.id.item_layout).setBackgroundResource(
                                    R.drawable.bg_book_state_enabled);
                        } else {
                            tvState.setText(booksBean.status);
                            tvState.setTextColor(getResources().getColor(R.color.red));
                            holder.getView(R.id.item_layout).setBackgroundResource(
                                    R.drawable.bg_book_state_disabled);
                        }
                        holder.setText(R.id.tv_tid, "条码号" + booksBean.tid);
                        holder.setText(R.id.tv_bid, "索书号" + mBook.bid);
                        holder.setText(R.id.tv_place, booksBean.room);
                    }
                });

    }

    private void delAtten() {
        BookId bookId = new BookId();
        bookId.id = id;
        CampusFactory.getRetrofitService().delAttentionBook(UserAccountManager.getInstance().getInfoUser().sid, bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    switch (response.code()) {
                        case 200:
                            changeAttenStatus(false);
                            RxBus.getDefault().send(new RefreshAttenBooks());
                            break;
                        case 404:
                            mBtnAttention.setChecked(true);
                            ((BaseAppActivity) getActivity()).showErrorSnackbarShort(
                                    R.string.request_invalid);
                            break;
                        //case 403:
                        //    ((BaseAppActivity) getActivity()).showErrorSnackbarShort(
                        //            R.string.request_invalid);
                        //    break;
                        default:
                            mBtnAttention.setChecked(true);
                            ((BaseAppActivity) getActivity()).showErrorSnackbarShort(
                                    R.string.tip_err_server);
                            break;
                    }
                });
    }

    public void createAtten() {
        if (mBookPost == null) {
            mBookPost = new BookPost();
            mBookPost.book = mBook.book;
            mBookPost.author = mBook.author;
            mBookPost.bid = mBook.bid;
            mBookPost.book_id = id;
        }
        CampusFactory.getRetrofitService().createAttentionBook(UserAccountManager.getInstance().getInfoUser().sid, mBookPost)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    switch (response.code()) {
                        case 200:
                            changeAttenStatus(true);
                            RxBus.getDefault().send(new RefreshAttenBooks());
                            break;
                        case 401:
                            mBtnAttention.setChecked(false);
                            ((BaseAppActivity) getActivity()).showErrorSnackbarShort(
                                    R.string.request_invalid);
                            break;
                        //case 403:
                        //    ((BaseAppActivity) getActivity()).showErrorSnackbarShort(
                        //            R.string.request_invalid);
                        //    break;
                        //case 409:
                        //    break;
                        default:
                            mBtnAttention.setChecked(false);
                            ((BaseAppActivity) getActivity()).showErrorSnackbarShort(
                                    R.string.tip_err_server);
                            break;
                    }
                });
    }

    public void changeAttenStatus(boolean b) {
        if (b) {
            mBtnAttention.setText(R.string.has_atten);
            mBtnAttention.setChecked(true);
        } else {
            mBtnAttention.setText(R.string.atten);
            mBtnAttention.setChecked(false);
        }
        hasAttention = b;
    }

    public boolean checkIsEllipsized(TextView textView, String str) {
        boolean isEllipsize = false;
        Layout l = textView.getLayout();
        if (l != null) {
            if (!l.getText().toString().equals(str)) {
                isEllipsize = true;
                Logger.d(l.getText().toString());
            }
        }
        ViewTreeObserver vto = textView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Layout l = textView.getLayout();
                if (l != null) {
                    int lines = l.getLineCount();
                    if (lines > 0)
                        if (l.getEllipsisCount(lines - 1) > 0)
                            Logger.d("text is ellips");
                }
            }
        });
//        boolean isEllipsize = !((textView.getLayout().getText().toString()).equals(str));
        return isEllipsize;
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d("onpause");
        if (startAttenStatus != hasAttention) {
            List<String> attenIds = MyBooksUtils.getAttentionBooksId();
            if (hasAttention) {
                attenIds.add(id);
            } else {
                attenIds.remove(id);
            }
            PreferenceUtil.saveString(PreferenceUtil.ATTENTION_BOOK_IDS,
                    TextUtils.join(",", attenIds));
        }
    }
}
