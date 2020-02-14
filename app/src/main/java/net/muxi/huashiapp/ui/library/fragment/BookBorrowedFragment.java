package net.muxi.huashiapp.ui.library.fragment;

import android.graphics.Color;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.appbase.BaseAppActivity;
import com.muxistudio.appcommon.appbase.BaseAppFragment;
import com.muxistudio.appcommon.data.Book;
import com.muxistudio.appcommon.data.BorrowedBook;
import com.muxistudio.appcommon.data.RenewData;
import com.muxistudio.appcommon.event.RefreshBorrowedBooks;
import com.muxistudio.appcommon.event.VerifyCodeSuccessEvent;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.common.util.Logger;

import java.util.Locale;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.library.VerifyCodeDialog;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao  17/2/21.
 */

public class BookBorrowedFragment extends BaseAppFragment {

    private boolean isEllipsized = false;
    private boolean isLineOver = false;

    private Book mBook;
    private String id;
    private String mInputContent;
    private BorrowedBook mBorrowedBook;
    private VerifyCodeSuccessEvent event;
    private Toolbar mToolbar;
    private TextView mTvTitle;
    private TextView mTvAuthor;
    private TextView mTvInfo;
    private TextView mTvShowAll;
    private TextView mTvDay;
    private TextView mTvBid;
    private TextView mTvTid;
    private ImageView mIvPlace;
    private TextView mTvPlace;
    private Button mBtnRenew;

    public static BookBorrowedFragment newInstance(Book book, String id) {

        Bundle args = new Bundle();
        args.putParcelable("book", book);
        args.putString("id", id);
        BookBorrowedFragment fragment = new BookBorrowedFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_borrowed, container, false);

        mBook = getArguments().getParcelable("book");
        id = getArguments().getString("id");
        initView(view);

        return view;
    }

    private void initView(View view) {
        mToolbar = view.findViewById(R.id.toolbar);
        mTvTitle = view.findViewById(R.id.tv_book_title);
        mTvAuthor = view.findViewById(R.id.tv_author);
        mTvInfo = view.findViewById(R.id.tv_info);
        mTvShowAll = view.findViewById(R.id.tv_show_all);
        mTvDay = view.findViewById(R.id.tv_day);
        mTvBid = view.findViewById(R.id.tv_bid);
        mTvTid = view.findViewById(R.id.tv_tid);
        mIvPlace = view.findViewById(R.id.iv_place);
        mTvPlace = view.findViewById(R.id.tv_place);
        mBtnRenew = view.findViewById(R.id.btn_renew);
        if(!TextUtils.isEmpty(mBook.book)) {
            mTvTitle.setText(mBook.book);
            mTvAuthor.setText(mBook.author);
            mTvInfo.setText(mBook.intro);
            mTvBid.setText("索书号" + mBook.bid);
        }
        loadPersonBook();
        ViewTreeObserver vto = mTvInfo.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(() -> {
            Layout l = mTvInfo.getLayout();
            if (l != null) {
                int lines = l.getLineCount();
                if (lines > 0) {
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
        mBtnRenew.setOnClickListener(v -> {
            renewBook();
        });

    }

    private void loadPersonBook() {
        CampusFactory.getRetrofitService().getPersonalBook(UserAccountManager.getInstance().getPHPSESSID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(Observable::from)
                .filter(personalBook -> personalBook.id.equals(id))
                .toList()
                .subscribe(personalBooks -> {
                    if (personalBooks != null && personalBooks.size() > 0) {
                        mTvTid.setText("条码号" + personalBooks.get(0).bar_code);
                        int day=personalBooks.get(0).time;
                        mTvDay.setText(String.format(Locale.CHINESE,"当前借阅（剩余%d天）", day));
                        if (day>3){
                            mBtnRenew.setText("剩余天数不足三天无法续借");
                            mBtnRenew.setEnabled(false);
                            mBtnRenew.setBackgroundColor(Color.GRAY);

                        }
                        mTvPlace.setText(personalBooks.get(0).room);
                        mBorrowedBook = personalBooks.get(0);
                    }
                }, error -> {

                }, () -> {

                });
    }


    private void renewBook() {
        VerifyCodeDialog fragment = VerifyCodeDialog.newInstance();
        fragment.show(getActivity().getSupportFragmentManager(), "inputContent");
        Bundle bundle = this.getArguments();
        mInputContent = bundle.getString("inputContent", null);
        Subscription subscription = RxBus.getDefault().toObservable(VerifyCodeSuccessEvent.class)
                .subscribe(new Subscriber<VerifyCodeSuccessEvent>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(VerifyCodeSuccessEvent verifyCodeSuccessEvent) {
                        mInputContent = verifyCodeSuccessEvent.getCode();
                        if (mInputContent != null) {
                            RenewData renewData = new
                                    RenewData();
                            renewData.bar_code = mBorrowedBook.bar_code;
                            renewData.check = mBorrowedBook.check;
                            CampusFactory.getRetrofitService().renewBook(UserAccountManager.getInstance().getPHPSESSID(), mInputContent, renewData)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(response -> {
                                        switch (response.code()) {
                                            case 200:
                                                ((BaseAppActivity) getActivity()).showSnackbarShort(R.string.renew_ok);
                                                RxBus.getDefault().send(new RefreshBorrowedBooks());
                                                break;
                                            case 406:
                                                ((BaseAppActivity) getActivity()).showErrorSnackbarShort(R.string.renew_not_in_date);
                                                break;
                                            case 403:
                                                ((BaseAppActivity) getActivity()).showErrorSnackbarShort(R.string.renew_already);
                                                break;
                                            default:
                                                ((BaseAppActivity) getActivity()).showErrorSnackbarShort(R.string.request_invalid);
                                                break;
                                        }
                                    },Throwable::printStackTrace,()->{});
                        }
                    }
                });

    }
}
