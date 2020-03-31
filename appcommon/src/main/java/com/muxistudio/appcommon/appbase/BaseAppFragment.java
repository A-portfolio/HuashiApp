package com.muxistudio.appcommon.appbase;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.muxistudio.appcommon.R;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.appcommon.widgets.LoadingDialog;
import com.muxistudio.common.base.BaseFragment;



/**
 * Created by ybao on 16/4/19.
 */
public class BaseAppFragment extends BaseFragment{


    private LoadingDialog mLoadingDialog;
    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_empty,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext=getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mContext=null;
    }

    public void showSnackBarShort(String word){
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                word, Snackbar.LENGTH_SHORT).show();
    }

    public void showErrorSnackBarShort(String word){
        Snackbar snackbar;
        snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),
                word, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        view.setBackgroundColor(getResources().getColor(R.color.red));
        snackbar.show();
    }
    public void showSnackBarLong(String word){
        Snackbar.make(getActivity().findViewById(android.R.id.content),
                word, Snackbar.LENGTH_LONG).show();
    }


    public void showLoading() {

        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog();
        }
        if (getActivity()!=null)
            mLoadingDialog.showNow(getActivity().getSupportFragmentManager(),"Loading");
    }

    public void hideLoading() {
        if(mLoadingDialog !=null){
            mLoadingDialog.dismissAllowingStateLoss();
        }
    }


}
