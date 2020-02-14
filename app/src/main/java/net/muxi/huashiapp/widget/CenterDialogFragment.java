package net.muxi.huashiapp.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import net.muxi.huashiapp.R;

/**
 * Created by december on 17/2/23.
 */

public class CenterDialogFragment extends DialogFragment {

    public static final int DIALOG_MARGIN = 8 * 3;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }


    public Dialog createCenterDialog(View view){
        Dialog dialog = new Dialog(getContext(),R.style.CenterDialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = (WindowManager.LayoutParams.MATCH_PARENT);
        params.height = (WindowManager.LayoutParams.WRAP_CONTENT);
        window.setAttributes(params);
        window.setBackgroundDrawableResource(R.drawable.bg_center_dialog);

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
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
