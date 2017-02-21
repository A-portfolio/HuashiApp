package net.muxi.huashiapp.ui.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/18.
 */

public class MyBookListFragment extends BaseFragment {

    @BindView(R.id.multi_status_view)
    MultiStatusView mMultiStatusView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mybooks, container, false);
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
