package net.muxi.huashiapp.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.muxi.huashiapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/2.
 */
public class ListFragment extends Fragment {


    @Bind(R.id.fragment_recyclerview)
    RecyclerView mFragmentRecyclerview;

    public static Fragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
