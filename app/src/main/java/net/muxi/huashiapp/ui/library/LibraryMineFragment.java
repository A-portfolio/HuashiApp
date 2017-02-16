package net.muxi.huashiapp.ui.library;

import android.os.Bundle;

import net.muxi.huashiapp.common.base.BaseFragment;

/**
 * Created by ybao on 17/2/16.
 */

public class LibraryMineFragment extends BaseFragment{

    public static LibraryMineFragment newInstance() {
        Bundle args = new Bundle();
        LibraryMineFragment fragment = new LibraryMineFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
