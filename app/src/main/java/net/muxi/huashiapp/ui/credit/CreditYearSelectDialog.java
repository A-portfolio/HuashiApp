package net.muxi.huashiapp.ui.credit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.muxistudio.appcommon.widgets.BottomDialogFragment;

import net.muxi.huashiapp.R;


/**
 * Created by ybao on 17/2/9.
 */

public class CreditYearSelectDialog extends BottomDialogFragment {

    private OnPositiveButtonClickListener mOnPositiveButtonClickListener;
    private TextView mTitle;
    private CreditYearSelectView mCreditYearSelectView;
    private Button mBtnCancel;
    private Button mBtnEnter;

    public static CreditYearSelectDialog newInstance(String start, String end) {
        Bundle args = new Bundle();
        args.putString("start", start);
        args.putString("ending", end);
        CreditYearSelectDialog fragment = new CreditYearSelectDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String startYear = getArguments().getString("start");
        String endYear = getArguments().getString("ending");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_credit_select_year,
                null);
        initView(view);
        Dialog dialog = createBottomDialog(view);

        mTitle.setText(String.format("%s至%s学年", startYear, endYear));

        mCreditYearSelectView.setNpStartYear(startYear);
        mCreditYearSelectView.setNpEndYear(endYear);
        mCreditYearSelectView.setOnValueChangeListener((start, end) -> {
            mTitle.setText(
                    String.format("%s至%s学年", start, end));
        });
        mBtnCancel.setOnClickListener(v -> {
            dismiss();
        });
        mBtnEnter.setOnClickListener(v -> {
            dismiss();
            if (mOnPositiveButtonClickListener != null) {
                mOnPositiveButtonClickListener.onPositiveButtonClick(
                        mCreditYearSelectView.getNpStartYear(), mCreditYearSelectView.getNpEndYear());
            }
        });

        return dialog;
    }

    public void setOnPositiveButtonClickListener(
            OnPositiveButtonClickListener onPositiveButtonClickListener) {
        mOnPositiveButtonClickListener = onPositiveButtonClickListener;
    }

    private void initView(View view) {
        mTitle = view.findViewById(R.id.title);
        mCreditYearSelectView = view.findViewById(R.id.credit_year_select_view);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        mBtnEnter = view.findViewById(R.id.btn_confirm);
    }

    public interface OnPositiveButtonClickListener {
        void onPositiveButtonClick(String start, String end);
    }
}
