package net.muxi.huashiapp.ui.library.adapter;

import android.content.Context;
import android.widget.TextView;

import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.AttentionBook;
import net.muxi.huashiapp.ui.library.BookDetailActivity;

/**
 * Created by ybao on 17/2/21.
 */

public class AttenBookRemindAdapter implements ItemViewDelegate<AttentionBook> {

    private Context mContext;

    public AttenBookRemindAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_my_book_remind;
    }

    @Override
    public boolean isForViewType(AttentionBook item, int position) {
        return item.avb.equals("y");
    }

    @Override
    public void convert(ViewHolder holder, AttentionBook attentionBook, int position) {
        holder.setText(R.id.tv_title, attentionBook.book);
        holder.setText(R.id.tv_remind, "关注图书可借");
        ((TextView) holder.getView(R.id.tv_remind)).setTextColor(
                App.sContext.getResources().getColor(R.color.color_selected));
        holder.getView(R.id.layout_item).setOnClickListener(
                v -> BookDetailActivity.start(mContext, attentionBook.id));
    }
}
