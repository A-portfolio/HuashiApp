package net.muxi.huashiapp.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Book;

/**
 * Created by ybao on 16/7/2.
 */
public class BookDetailView extends RelativeLayout {

    private TextView mTvTitle;
    private TextView mTvAuthor;
    private TextView mTvEncoding;
    private TextView mTvInfo;
    private LinearLayout mStateLayout;
//    private RecyclerView mRecyclerview;
    private Context mContext;
    private Book mBook;

    public BookDetailView(Context context, Book book) {
        super(context);
        mContext = context;
        mBook = book;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_book_detail, this, true);
        mTvTitle = (TextView)view.findViewById(R.id.tv_title);
        mTvAuthor = (TextView)view.findViewById(R.id.tv_author);
        mTvEncoding = (TextView)view.findViewById(R.id.tv_encoding);
        mTvInfo = (TextView)view.findViewById(R.id.tv_info);
        mStateLayout = (LinearLayout) view.findViewById(R.id.state_layout);

//        mRecyclerview = (RecyclerView)view.findViewById(R.id.recyclerview);
        mTvTitle.setText(mBook.getBook());
        mTvAuthor.setText(mBook.getAuthor());
        mTvEncoding.setText(mBook.getBid());
        mTvInfo.setText(mBook.getIntro());
//        setupRecyclerview();
        setupStateLayout();
    }

    private void setupStateLayout() {
        int size = mBook.getBooks().size();
        RelativeLayout[] childLayouts = new RelativeLayout[size];
        for (int i = 0;i < size;i ++){
            childLayouts[i] = new RelativeLayout(mContext);
            TextView mTvState = new TextView(mContext);
            TextView mTvPlace = new TextView(mContext);
            RelativeLayout.LayoutParams stateParams = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            RelativeLayout.LayoutParams placeParams = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            stateParams.addRule(ALIGN_PARENT_LEFT);
            mTvState.setLayoutParams(stateParams);
            mTvState.setText(mBook.getBooks().get(i).getStatus());
            childLayouts[i].addView(mTvState);
            placeParams.addRule(ALIGN_PARENT_RIGHT);
            mTvPlace.setLayoutParams(placeParams);
            if (mBook.getBooks().get(i).getStatus().equals("借出")){
                mTvPlace.setText(mBook.getBooks().get(i).getDate());
            }else {
                mTvPlace.setText(mBook.getBooks().get(i).getRoom());
            }
            childLayouts[i].addView(mTvPlace);
            mStateLayout.addView(childLayouts[i]);
        }
    }

//    private void setupRecyclerview() {
//        mRecyclerview.setHasFixedSize(true);
//        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
//        BookListAdapter bookListAdapter = new BookListAdapter(mBook);
//        mRecyclerview.setAdapter(bookListAdapter);
//        mRecyclerview.getParent().requestDisallowInterceptTouchEvent(false);
//    }


}
