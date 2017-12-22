package net.muxi.huashiapp.ui.library.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.data.Book;
import net.muxi.huashiapp.common.data.BorrowedBook;
import net.muxi.huashiapp.common.data.RenewData;
import net.muxi.huashiapp.event.RefreshBorrowedBooks;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/2/21.
 */

public class BookBorrowedFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_author)
    TextView mTvAuthor;
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.tv_show_all)
    TextView mTvShowAll;
    @BindView(R.id.tv_day)
    TextView mTvDay;
    @BindView(R.id.tv_bid)
    TextView mTvBid;
    @BindView(R.id.tv_tid)
    TextView mTvTid;
    @BindView(R.id.iv_place)
    ImageView mIvPlace;
    @BindView(R.id.tv_place)
    TextView mTvPlace;
    @BindView(R.id.btn_renew)
    Button mBtnRenew;

    private boolean isEllipsized = false;
    private boolean isLineOver = false;

    private Book mBook;
    private String id;
    private BorrowedBook mBorrowedBook;

    public static BookBorrowedFragment newInstance(Book book,String id) {

        Bundle args = new Bundle();
        args.putParcelable("book", book);
        args.putString("id",id);
        BookBorrowedFragment fragment = new BookBorrowedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_borrowed, container, false);
        ButterKnife.bind(this, view);

        mBook = getArguments().getParcelable("book");
        id = getArguments().getString("id");

        initView();
        return view;
    }

    private void initView() {
        mTvTitle.setText(mBook.book);
        mTvAuthor.setText(mBook.author);
        mTvInfo.setText(mBook.intro);
        mTvBid.setText("索书号" + mBook.bid);

        loadPersonBook();
        ViewTreeObserver vto = mTvInfo.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Layout l = mTvInfo.getLayout();
                if ( l != null){
                    int lines = l.getLineCount();
                    if ( lines > 0) {
                        if (l.getEllipsisCount(lines - 1) > 0) {
                            Logger.d("text is ellips");
                            isEllipsized = true;
                            isLineOver = true;
                        }else {
                            isEllipsized = false;
                        }
                    }else {
                        isEllipsized = false;
                    }

                }
                if (!isLineOver){
                    mTvShowAll.setVisibility(View.GONE);
                }else {
                    mTvShowAll.setOnClickListener(v -> {
                        if (isEllipsized){
                            mTvInfo.setMaxLines(Integer.MAX_VALUE);
                            mTvShowAll.setText(R.string.fold_all);
                        }else {
                            mTvInfo.setMaxLines(2);
                            mTvShowAll.setText(R.string.expand_all);
                        }
                    });
                }
            }
        });
        mBtnRenew.setOnClickListener(v -> {
            renewBook();
        });
    }

    private void loadPersonBook() {
        CampusFactory.getRetrofitService().getPersonalBook(Base64Util.createBaseStr(
                App.sLibrarayUser))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(Observable::from)
                .filter(personalBook -> personalBook.id.equals(id))
                .toList()
                .subscribe(personalBooks -> {
                    if (personalBooks != null && personalBooks.size() > 0){
                        mTvTid.setText("条码号" + personalBooks.get(0).bar_code);
                        mTvDay.setText(String.format("当前借阅（剩余%d天）",personalBooks.get(0).time));
                        mTvPlace.setText(personalBooks.get(0).room);
                        mBorrowedBook = personalBooks.get(0);
                    }
                });
    }

    private void renewBook() {
        RenewData renewData = new RenewData();
        renewData.bar_code = mBorrowedBook.bar_code;
        renewData.check = mBorrowedBook.check;
        CampusFactory.getRetrofitService().renewBook(Base64Util.createBaseStr(App.sLibrarayUser),renewData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    switch (response.code()){
                        case 200:
                            ((BaseActivity) getActivity()).showSnackbarShort(R.string.renew_ok);
                            RxBus.getDefault().send(new RefreshBorrowedBooks());
                            break;
                        case 406:
                            ((BaseActivity)getActivity()).showErrorSnackbarShort(R.string.renew_not_in_date);
                            break;
                        case 403:
                            ((BaseActivity)getActivity()).showErrorSnackbarShort(R.string.renew_already);
                            break;
                        default:
                            ((BaseActivity)getActivity()).showErrorSnackbarShort(R.string.request_invalid);
                            break;
                    }
                });

    }


}
