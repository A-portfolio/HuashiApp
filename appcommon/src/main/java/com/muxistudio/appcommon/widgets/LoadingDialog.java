package com.muxistudio.appcommon.widgets;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.muxistudio.appcommon.R;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by ybao on 17/2/13.
 */

public class LoadingDialog extends BottomDialogFragment {

    private TextView mTvLoadingInfo;
    private ImageView mIvPlace;

    private OnSubscriptionCanceledListener mSubscriptionCanceldListener;

  public static LoadingDialog newInstance(){
    LoadingDialog fragment = new LoadingDialog();
    return fragment;
  }

  public void setOnSubscriptionCanceledListener(OnSubscriptionCanceledListener
      listener){
    if(mSubscriptionCanceldListener == null)
      this.mSubscriptionCanceldListener = listener;
  }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getContext(), R.style.FullScreenDialogStyle);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null);

        mIvPlace = view.findViewById(R.id.iv_place);
        mTvLoadingInfo = view.findViewById(R.id.tv_loading_info);


        Uri uri = Uri.parse("asset://net.muxi.huashiapp/loading.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(
                uri).setAutoPlayAnimations(true).build();
        ((SimpleDraweeView) view.findViewById(R.id.drawee)).setController(controller);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wmlp);
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_dialog_background)));
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(getResources().getColor(R.color.light_dialog_background));
        }

      mIvPlace.setOnClickListener(v->{
        if(mSubscriptionCanceldListener!=null)
          mSubscriptionCanceldListener.onClick();
        dialog.dismiss();
      });

        return dialog;
    }


  public void setLoadingInfo(String text) {
    if (mTvLoadingInfo != null) {
      mTvLoadingInfo.setText(text);
    }
  }



    public interface OnSubscriptionCanceledListener{
      void onClick();
    }

}
