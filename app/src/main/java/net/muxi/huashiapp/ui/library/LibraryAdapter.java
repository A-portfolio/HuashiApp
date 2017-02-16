package net.muxi.huashiapp.ui.library;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.listener.OnItemClickListener;
import net.muxi.huashiapp.common.data.BookSearchResult;
import net.muxi.huashiapp.util.NoDoubleClickUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_book, parent, false);

//        return new ViewHolder(v);
        return null;
    }


    public void swap(List<BookSearchResult.ResultsBean> list) {
        mBookList.clear();
        mBookList.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String bookTitle = interceptTitle(mBookList.get(position).getBook());
        holder.mTvBook.setText(bookTitle);
        holder.mTvAuthor.setText(mBookList.get(position).getAuthor());
        holder.mTvInfo.setText(mBookList.get(position).getIntro());
//        holder.mBookLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!NoDoubleClickUtil.isDoubleClick())
//                mOnItemClickListener.onItemClick(v, mBookList.get(position));
//            }
//        });
    }

    //截取数字后的书名
    private String interceptTitle(String book) {
        int index = book.indexOf(".");
        return book.substring(index + 1,book.length());
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

        @BindView(R.id.tv_book)
        TextView mTvBook;
        @BindView(R.id.tv_author)
        TextView mTvAuthor;
        @BindView(R.id.tv_info)
        TextView mTvInfo;
//        @BindView(R.id.tv_encoding)
//        ImageView mTvEncoding;
//        @BindView(R.id.book_layout)
//        RelativeLayout mBookLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
