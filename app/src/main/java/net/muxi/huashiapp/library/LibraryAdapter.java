package net.muxi.huashiapp.library;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.OnItemClickListener;
import net.muxi.huashiapp.common.data.Book;

/**
 * Created by ybao on 16/5/3.
 */
public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    private String[] mTitles;

    private OnItemClickListener mOnItemClickListener;
    private Book book;


    public LibraryAdapter(String[] titles) {
        super();
        mTitles = titles;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("length", mTitles.length + "");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTvTitle.setText(mTitles[position]);

        if (mOnItemClickListener != null) {
            holder.mTvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }


    @Override
    public int getItemCount() {
        return mTitles.length;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTvTitle;
        private RelativeLayout mBookLayout;
        private TextView mTvAuthor;
        private TextView mTvInfo;
        private TextView mTvEncoding;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mBookLayout = (RelativeLayout) itemView.findViewById(R.id.book_layout);
            mTvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            mTvInfo = (TextView) itemView.findViewById(R.id.tv_info);
            mTvEncoding = (TextView) itemView.findViewById(R.id.tv_encoding);
            mBookLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.book_layout:

                    book = new Book();

                    book.setAuthor(mTvTitle.getText().toString());
                    book.setAuthor(mTvAuthor.getText().toString());
                    book.setInfo(mTvInfo.getText().toString());
                    book.setEncode(mTvEncoding.getText().toString());
                    mOnItemClickListener.onItemClick(v, book);
            }
        }

        private void beginAnim(View v) {
        }


    }
}
