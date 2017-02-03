package net.muxi.huashiapp.studyroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;

/**
 * Created by december on 17/2/3.
 */

public class OptionListFragment extends BaseFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_option_list,container,false);
        return view;


    }
}
