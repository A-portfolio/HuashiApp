package net.muxi.huashiapp.common.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import net.muxi.huashiapp.R;

/**
 * Created by ybao on 16/4/19.
 */
public class BaseFragment extends Fragment {


    public static Fragment newInstance(){
        Fragment fragment = new Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //设置activity中 toolbar 的 title
    public void setTitle(CharSequence title){
//        getActivity().getActionBar().setTitle(title);
        getActivity().getActionBar().setTitle(title);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_empty,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
