package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.muxistudio.appcommon.data.BookSearchResult;

import net.muxi.huashiapp.R;

import java.util.List;


/**
 * Created by ybao on 17/2/15.
 */

public class LibrarySearchResultAdapter extends BaseAdapter {

    private Context mContext;

    private List<BookSearchResult.ResultsBean> mBookList;
    private TextView mTvBook;
    private TextView mTvAuthor;

    public LibrarySearchResultAdapter(Context context,
                                      List<BookSearchResult.ResultsBean> bookList) {
        mBookList = bookList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mBookList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBookList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //fixme using viewHolder pattern to reuse the view
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_book_search_result,
                viewGroup, false);
        initView(v);
        String s = mBookList.get(i).book;
        int index = mBookList.get(i).book.indexOf(".");
        if (index >= 0 && index < mBookList.get(i).book.length()) {
            s = s.substring(index + 1, s.length());
        }
        mTvBook.setText(s);
        mTvAuthor.setText(mBookList.get(i).author);
        return v;
    }

    private void initView(View v) {
        mTvBook = v.findViewById(R.id.tv_book);
        mTvAuthor = v.findViewById(R.id.tv_author);
    }
}
