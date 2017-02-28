package net.muxi.huashiapp.ui.more;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.BottomDialogFragment;

import butterknife.BindView;

/**
 * Created by december on 17/2/26.
 */

public class ShareDialog extends BottomDialogFragment {

    @BindView(R.id.grid_share)
    GridLayout mGridShare;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_share, null);

        Dialog dialog = createBottomDialog(view);
        return dialog;


    }

}
