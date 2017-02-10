package net.muxi.huashiapp.ui.credit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.util.UserUtil;
import net.muxi.huashiapp.widget.BottomDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 17/2/9.
 */

public class CreditYearSelectDialog extends BottomDialogFragment {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.credit_year_select_view)
    CreditYearSelectView mCreditYearSelectView;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;

    private OnPositiveButtonClickListener mOnPositiveButtonClickListener;

    public static CreditYearSelectDialog newInstance(String start, String end) {
        Bundle args = new Bundle();
        args.putString("start", start);
        args.putString("end", end);
        CreditYearSelectDialog fragment = new CreditYearSelectDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String startYear = getArguments().getString("start");
        String endYear = getArguments().getString("end");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_credit_select_year,
                null);
        ButterKnife.bind(this, view);
        Dialog dialog = createBottomDialog(view);

        mTitle.setText(String.format("%s至%s学年", startYear, endYear));

        mCreditYearSelectView.setStartYear(startYear);
        mCreditYearSelectView.setEndYear(endYear);
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
                        mCreditYearSelectView.getStartYear(), mCreditYearSelectView.getEndYear());
            }
        });

        return dialog;
    }

    public void setOnPositiveButtonClickListener(
            OnPositiveButtonClickListener onPositiveButtonClickListener) {
        mOnPositiveButtonClickListener = onPositiveButtonClickListener;
    }

    public interface OnPositiveButtonClickListener {
        void onPositiveButtonClick(String start, String end);
    }
}
