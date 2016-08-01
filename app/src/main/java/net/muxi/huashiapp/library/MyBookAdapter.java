package net.muxi.huashiapp.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.PersonalBook;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/7/30.
 * 已借图书的 adapter
 */
public class MyBookAdapter extends RecyclerView.Adapter<MyBookAdapter.ViewHolder> {


    private List<PersonalBook> mPersonalBooks;
    private Context mContext;

    public MyBookAdapter(List<PersonalBook> personalBooks) {
        super();
        mPersonalBooks = personalBooks;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (Integer.valueOf(mPersonalBooks.get(position).getTime()) < 0) {
            holder.mTvState.setText(mContext.getResources().getString(R.string.library_mybook_overdue));
            holder.mTvState.setTextColor(mContext.getResources().getColor(R.color.state_warn));
            holder.mTvDay.setVisibility(View.GONE);
            holder.mTvDeadline.setTextColor(mContext.getResources().getColor(R.color.state_warn));
        } else {
            holder.mTvState.setText(mPersonalBooks.get(position).getTime());
            holder.mTvState.setTextColor(mContext.getResources().getColor(R.color.state_normal));
            holder.mTvDeadline.setTextColor(mContext.getResources().getColor(R.color.state_normal));
        }
        holder.mTvBook.setText(mPersonalBooks.get(position).getBook());
        holder.mTvAuthor.setText(mPersonalBooks.get(position).getAuthor());
        holder.mTvBorrowDate.setText(mPersonalBooks.get(position).getItime());
        holder.mTvDeadline.setTextSize(18);
        holder.mTvDeadline.setText(mPersonalBooks.get(position).getOtime());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mybook, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mPersonalBooks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_state)
        TextView mTvState;
        @BindView(R.id.tv_day)
        TextView mTvDay;
        @BindView(R.id.state_layout)
        LinearLayout mStateLayout;
        @BindView(R.id.tv_book_title)
        TextView mTvBookTitle;
        @BindView(R.id.tv_book)
        TextView mTvBook;
        @BindView(R.id.tv_author)
        TextView mTvAuthor;
        @BindView(R.id.tv_title_borrow)
        TextView mTvTitleBorrow;
        @BindView(R.id.tv_borrow_date)
        TextView mTvBorrowDate;
        @BindView(R.id.tv_deadline_title)
        TextView mTvDeadlineTitle;
        @BindView(R.id.tv_deadline)
        TextView mTvDeadline;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
