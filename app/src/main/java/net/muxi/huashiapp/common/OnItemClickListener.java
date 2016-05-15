package net.muxi.huashiapp.common;

import android.view.View;

import net.muxi.huashiapp.common.data.Book;

/**
 * Created by ybao on 16/5/4.
 * RecyclerView 的监听接口
 */
public interface OnItemClickListener {
    public void onItemClick(View view,Book book);
}
