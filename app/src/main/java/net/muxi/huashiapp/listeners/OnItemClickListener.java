package net.muxi.huashiapp.listeners;

import android.view.View;

import com.muxistudio.appcommon.data.BookSearchResult;

/**
 * Created by ybao on 16/5/4.
 * RecyclerView 的监听接口
 */
public interface OnItemClickListener {
    void onItemClick(View view, BookSearchResult.ResultsBean resultsBean);
}

