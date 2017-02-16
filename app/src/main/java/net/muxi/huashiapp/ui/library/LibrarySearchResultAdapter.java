package net.muxi.huashiapp.ui.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.BookSearchResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/15.
 */

public class LibrarySearchResultAdapter extends BaseAdapter {

    @BindView(R.id.tv_book)
    TextView mTvBook;
    @BindView(R.id.tv_author)
    TextView mTvAuthor;

    private List<BookSearchResult.ResultsBean> mBookList;

    public LibrarySearchResultAdapter(
            List<BookSearchResult.ResultsBean> bookList) {
        mBookList = bookList;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(view.getContext()).inflate(R.layout.item_book_search_result,
                null);
        ButterKnife.bind(this,view);
        mTvBook.setText(mBookList.get(i).getBook());
        mTvAuthor.setText(mBookList.get(i).getBook());
        return v;
    }

}
