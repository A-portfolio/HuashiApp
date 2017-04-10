package net.muxi.huashiapp.ui.library.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.AttentionBook;
import net.muxi.huashiapp.ui.library.BookDetailActivity;

/**
 * Created by ybao on 17/2/21.
 */

public class AttenBookAdapter implements ItemViewDelegate<AttentionBook>{

    private Context mContext;

    public AttenBookAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_my_book;
    }

    @Override
    public boolean isForViewType(AttentionBook item, int position) {
        return item.avbl.equals("n");
    }

    @Override
    public void convert(ViewHolder holder, AttentionBook attentionBook, int position) {
        holder.setText(R.id.tv_title,attentionBook.book);
        holder.getView(R.id.item_layout).setOnClickListener(v -> {
            BookDetailActivity.start(mContext,attentionBook.id);
        });
    }
}
