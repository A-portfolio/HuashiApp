package com.muxistudio.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by fengminchao on 18/3/19
 */

public abstract class BaseFragment extends Fragment{

    public static Fragment newInstance(){
        Fragment fragment = new Fragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
