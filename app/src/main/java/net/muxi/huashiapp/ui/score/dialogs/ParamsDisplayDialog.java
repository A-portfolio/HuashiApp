package net.muxi.huashiapp.ui.score.dialogs;

import android.app.Dialog;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.widget.CenterDialogFragment;

public class ParamsDisplayDialog extends CenterDialogFragment {

    private TextView mBtnConfirm;
    private TextView mBtnCancel;
    private TextView mTvYearValue;
    private TextView mTvTermValue;

    private OnPositiveClickListener mListener;

    public static ParamsDisplayDialog newInstance(String yearName,String termName){
        Bundle args = new Bundle();
        args.putString("year",yearName);
        args.putString("term",termName);

        ParamsDisplayDialog dialog = new ParamsDisplayDialog();
        dialog.setArguments(args);
        return dialog;
    }

    public void setOnPositiveClickListener(OnPositiveClickListener listener){
        this.mListener = listener;
    }

    private void initView(View view){
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        mBtnConfirm = view.findViewById(R.id.btn_confirm);

        mTvYearValue = view.findViewById(R.id.tv_year_value);
        mTvTermValue = view.findViewById(R.id.tv_term_value);

        mBtnCancel.setOnClickListener(v->{
            dismiss();
        });
        mBtnConfirm.setOnClickListener(v->{
            mListener.onClick();
            dismiss();
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_params_default,null,false);
        initView(view);
        String year = (String) getArguments().get("year");
        String term = (String) getArguments().get("term");

        mTvTermValue.setText(term);
        mTvYearValue.setText(year);

        Dialog dialog = createCenterDialog(view);
        return dialog;
    }

    public interface OnPositiveClickListener{
        void onClick();
    }
}
