package net.muxi.huashiapp.ui.library.adapter;

//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.muxistudio.appcommon.data.Book;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;


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
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_state, parent,
                false);
        return new ViewHolder(convertView);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mItemLayout;
        private TextView mTvState;
        private TextView mTvTid;
        private TextView mTvBid;
        private TextView mTvPlace;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemLayout = itemView.findViewById(R.id.item_layout);
            mTvState = itemView.findViewById(R.id.tv_state);
            mTvTid = itemView.findViewById(R.id.tv_tid);
            mTvBid = itemView.findViewById(R.id.tv_bid);
            mTvPlace = itemView.findViewById(R.id.tv_place);
        }
    }

}
