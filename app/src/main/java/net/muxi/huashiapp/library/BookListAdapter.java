package net.muxi.huashiapp.library;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Book;
import net.muxi.huashiapp.common.util.Logger;

/**
 * Created by ybao on 16/7/2.
 * 同一本书不同条码号
 */
public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.ViewHolder> {

    private Book mBook;

    public BookListAdapter(Book book) {
        super();
        mBook = book;
        Logger.d(mBook.getBooks().size() + "");
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTvState.setText(mBook.getBooks().get(position).getStatus());
        if (mBook.getBooks().get(position).getStatus().equals("借出")){
            holder.mTvPlace.setText(mBook.getBooks().get(position).getDate());
        }else {
            holder.mTvPlace.setText(mBook.getBooks().get(position).getRoom());
        }
    }

    @Override
    public int getItemCount() {
        return mBook.getBooks().size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_state,parent,false);
        return new ViewHolder(v);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvState;
        private TextView mTvPlace;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvState = (TextView)itemView.findViewById(R.id.tv_state);
            mTvPlace = (TextView)itemView.findViewById(R.id.tv_place);
        }
    }
}
