package net.muxi.huashiapp.widget;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.Logger;

/**
 * Created by ybao on 17/2/13.
 */

public class LoadingDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.LightBackgroundDialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null);
        Uri uri = Uri.parse("asset://net.muxi.huashiapp/loading.gif");
        DraweeController controller = Fresco.newDraweeControllerBuilder().setUri(
                uri).setAutoPlayAnimations(true).build();
        ((SimpleDraweeView) view.findViewById(R.id.drawee)).setController(controller);
        dialog.setContentView(view);
        return dialog;
    }

}
