package net.muxi.huashiapp.ui.login;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.muxistudio.common.util.PreferenceUtil;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.CenterDialogFragment;

public class LoginPromptDialog extends CenterDialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.dialog_login_prompt,null);
        CheckBox checkBox=view.findViewById(R.id.prompt_checkbox);
        view.findViewById(R.id.prompt_ok).setOnClickListener(v -> {
            if (checkBox.isChecked()){
                PreferenceUtil.saveBoolean("prompt",false);
            }
            dismiss();
        });


        return createCenterDialog(view);
    }
}