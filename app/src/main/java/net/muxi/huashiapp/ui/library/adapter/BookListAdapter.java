package net.muxi.huashiapp.ui.library.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Book;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/7/2.
 * 同一本书不同条码号
 */
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private Book mBook;

    public BookListAdapter(Book book) {
        super();
        mBook = book;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String bookStatus = mBook.books.get(position).status;
        if (bookStatus.equals("可借")) {
            holder.mTvState.setTextColor(
                    App.getContext().getResources().getColor(R.color.color_selected));
            holder.mItemLayout.setBackgroundResource(R.drawable.bg_book_state_enabled);
        } else {
            holder.mTvState.setTextColor(
                    App.getContext().getResources().getColor(R.color.red));
            holder.mItemLayout.setBackgroundResource(R.drawable.bg_book_state_disabled);
        }

        holder.mTvState.setText(bookStatus);
        holder.mTvTid.setText(String.format("条码号%s", mBook.books.get(position).tid));
        holder.mTvBid.setText(String.format("索书号%s", mBook.bid));
        holder.mTvPlace.setText(mBook.books.get(position).room);
    }

    @Override
    public int getItemCount() {
        return mBook.books.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_state, parent,
                false);
        return new ViewHolder(v);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_layout)
        RelativeLayout mItemLayout;
        @BindView(R.id.tv_state)
        TextView mTvState;
        @BindView(R.id.tv_tid)
        TextView mTvTid;
        @BindView(R.id.tv_bid)
        TextView mTvBid;
        @BindView(R.id.tv_place)
        TextView mTvPlace;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
