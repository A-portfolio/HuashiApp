package net.muxi.huashiapp.library;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.OnItemClickListener;
import net.muxi.huashiapp.common.data.Book;
import net.muxi.huashiapp.common.data.BookSearchResult;

import butterknife.Bind;

/**
 * Created by ybao on 16/5/3.
 * 图书列表Adapter
 */
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {


    private BookSearchResult mBookSearchResult;

    private OnItemClickListener mOnItemClickListener;
    private Book book;

    public LibraryAdapter(BookSearchResult bookSearchResult) {
        super();
        mBookSearchResult = bookSearchResult;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTvTitle.setText(mBookSearchResult.getResults().get(position).getBook());
        holder.mTvAuthor.setText(mBookSearchResult.getResults().get(position).getAuthor());
        holder.mTvEncoding.setText(mBookSearchResult.getResults().get(position).getId());
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public int getItemCount() {
        return mBookSearchResult.getResults().size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_author)
        TextView mTvAuthor;
        @Bind(R.id.tv_info)
        TextView mTvInfo;
        @Bind(R.id.tv_encoding)
        TextView mTvEncoding;
        @Bind(R.id.book_layout)
        RelativeLayout mBookLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mTvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            mTvEncoding = (TextView) itemView.findViewById(R.id.tv_encoding);
            mBookLayout = (RelativeLayout) itemView.findViewById(R.id.book_layout);
            mBookLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.book_layout:
                    book = new Book();
                    book.setAuthor(mTvTitle.getText().toString());
                    book.setAuthor(mTvAuthor.getText().toString());
                    book.setEncode(mTvEncoding.getText().toString());
                    mOnItemClickListener.onItemClick(v, book);
                    fadeOutAnim();
            }
        }


        public void fadeOutAnim() {
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
//            alphaAnimation.setDuring(LibraryActivity.DURATION_ALPH);
            mTvTitle.startAnimation(alphaAnimation);
            mTvAuthor.startAnimation(alphaAnimation);
            mTvEncoding.startAnimation(alphaAnimation);
        }

    }
}
