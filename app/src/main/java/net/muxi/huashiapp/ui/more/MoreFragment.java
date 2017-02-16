package net.muxi.huashiapp.ui.more;

import android.os.Bundle;

import net.muxi.huashiapp.common.base.BaseFragment;

/**
 * Created by ybao on 17/2/16.
 */

public class MoreFragment extends BaseFragment{

    public static MoreFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MoreFragment fragment = new MoreFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
