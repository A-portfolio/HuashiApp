package net.muxi.huashiapp.library;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.Book;
import net.muxi.huashiapp.common.util.Logger;

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
        Logger.d(mBook.getBooks().size() + "");
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String bookStatus = mBook.getBooks().get(position).getStatus();
        if (bookStatus.equals("可借")){
            holder.mTvState.setTextColor(App.getContext().getResources().getColor(R.color.state_available));
        }else {
            holder.mTvState.setTextColor(App.getContext().getResources().getColor(R.color.state_unavailable));
        }
        holder.mTvState.setText(bookStatus);

        holder.mTvTid.setText(mBook.getBooks().get(position).getTid());
        holder.mTvBid.setText(mBook.getBid());
        holder.mTvPlace.setText("                       " + mBook.getBooks().get(position).getRoom());
    }

    @Override
    public int getItemCount() {
        return mBook.getBooks().size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_state, parent, false);
        return new ViewHolder(v);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_state)
        TextView mTvState;
        @BindView(R.id.tv_tid)
        TextView mTvTid;
        @BindView(R.id.tv_bid)
        TextView mTvBid;
        @BindView(R.id.tv_period)
        TextView mTvPeriod;
        @BindView(R.id.tv_place)
        TextView mTvPlace;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
