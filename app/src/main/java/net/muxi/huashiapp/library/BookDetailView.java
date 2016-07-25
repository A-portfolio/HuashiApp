package net.muxi.huashiapp.library;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Book;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/7/2.
 */
public class BookDetailView extends RelativeLayout {

    @BindView(R.id.imgbtn_close)
    ImageButton mImgbtnClose;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_author)
    TextView mTvAuthor;
    @BindView(R.id.tv_published)
    TextView mTvPublished;
    @BindView(R.id.tv_info)
    TextView mTvInfo;
    @BindView(R.id.background_layout)
    LinearLayout mBackgroundLayout;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    private Context mContext;
    private Book mBook;
    /**
     * 出版社
     */
    private String published;

    public BookDetailView(Context context, Book book, String published) {
        super(context);
        mContext = context;
        mBook = book;
        this.published = published;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_book_detail, this, true);
        ButterKnife.bind(this);

        mTvTitle.setText(mBook.getBook());
        mTvAuthor.setText(mBook.getAuthor());
        mTvInfo.setText(mBook.getIntro());
        mTvPublished.setText(published);
//        setupStateLayout();
        setupRecyclerview();
    }

//    private void setupStateLayout() {
//        int size = mBook.getBooks().size();
//        RelativeLayout[] childLayouts = new RelativeLayout[size];
//        for (int i = 0; i < size; i++) {
//            childLayouts[i] = new RelativeLayout(mContext);
//            TextView mTvState = new TextView(mContext);
//            TextView mTvPlace = new TextView(mContext);
//            LayoutParams stateParams = new LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            LayoutParams placeParams = new LayoutParams(
//                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
//            );
//            stateParams.addRule(ALIGN_PARENT_LEFT);
//            mTvState.setLayoutParams(stateParams);
//            mTvState.setText(mBook.getBooks().get(i).getStatus());
//            childLayouts[i].addView(mTvState);
//            placeParams.addRule(ALIGN_PARENT_RIGHT);
//            mTvPlace.setLayoutParams(placeParams);
//            if (mBook.getBooks().get(i).getStatus().equals("借出")) {
//                mTvPlace.setText(mBook.getBooks().get(i).getDate());
//            } else {
//                mTvPlace.setText(mBook.getBooks().get(i).getRoom());
//            }
//            childLayouts[i].addView(mTvPlace);
//            mStateLayout.addView(childLayouts[i]);
//        }
//    }

    private void setupRecyclerview() {
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        BookListAdapter bookListAdapter = new BookListAdapter(mBook);
        mRecyclerview.setAdapter(bookListAdapter);
        mRecyclerview.setNestedScrollingEnabled(false);
    }


}
