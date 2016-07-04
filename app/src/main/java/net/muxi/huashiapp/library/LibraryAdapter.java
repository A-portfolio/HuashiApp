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
import net.muxi.huashiapp.common.data.BookSearchResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ybao on 16/5/3.
 * 图书列表Adapter
 */
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {


    private List<BookSearchResult.ResultsBean> mBookList;

    private OnItemClickListener mOnItemClickListener;

    public LibraryAdapter(List<BookSearchResult.ResultsBean> bookList) {
        super();
        mBookList = new ArrayList<>();
        mBookList.addAll(bookList);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);

        return new ViewHolder(v);
    }


    public void swap(List<BookSearchResult.ResultsBean> list){
        mBookList.clear();
        mBookList.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mTvTitle.setText(mBookList.get(position).getBook());
        holder.mTvAuthor.setText(mBookList.get(position).getAuthor());
        holder.mTvEncoding.setText(mBookList.get(position).getBid());
        holder.mBookLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,mBookList.get(position));
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public int getItemCount() {
        return mBookList.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_author)
        TextView mTvAuthor;
        @BindView(R.id.tv_info)
        TextView mTvInfo;
        @BindView(R.id.tv_encoding)
        TextView mTvEncoding;
        @BindView(R.id.book_layout)
        RelativeLayout mBookLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_book);
            mTvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            mTvEncoding = (TextView) itemView.findViewById(R.id.tv_encoding);
            mBookLayout = (RelativeLayout) itemView.findViewById(R.id.book_layout);
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
