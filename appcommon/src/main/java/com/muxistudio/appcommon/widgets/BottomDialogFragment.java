package com.muxistudio.appcommon.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.muxistudio.appcommon.R;
import com.muxistudio.common.util.Logger;


/**
 * Created by ybao on 17/2/2.
 */


public class BottomDialogFragment extends DialogFragment {

    public static final int DIALOG_MARGIN = 8 * 3;
    public BottomDialogFragment() {
    }

    public static BottomDialogFragment newInstance() {
        Bundle args = new Bundle();
        BottomDialogFragment fragment = new BottomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    public Dialog  createBottomDialog(View view){
        Dialog dialog = new Dialog(getContext(), R.style.BottomDialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wmlp);
        window.setBackgroundDrawableResource(R.drawable.bg_bottom_dialog);
        return dialog;
    }

    public void setContentView(View view){
        getDialog().setContentView(view);
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        return super.show(transaction, tag);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try{
          super.show(manager, tag);
        }catch (Exception e){
            e.printStackTrace();
            Logger.d("fragment exception ");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}