package net.muxi.huashiapp.library;

import android.support.v4.app.Fragment;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/2.
 */
public class LibraryDetailFragment extends Fragment {


    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_author)
    TextView mTvAuthor;
    @Bind(R.id.tv_info)
    TextView mTvInfo;
    @Bind(R.id.detail_layout)
    RelativeLayout mDetailLayout;

    public static Fragment newInstance() {
        LibraryDetailFragment libraryDetailFragment = new LibraryDetailFragment();
        return libraryDetailFragment;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
