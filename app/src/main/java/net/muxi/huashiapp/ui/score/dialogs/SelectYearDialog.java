package net.muxi.huashiapp.ui.score.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.muxistudio.appcommon.widgets.BottomDialogFragment;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.score.ScoreCreditSelectView;

public class SelectYearDialog extends BottomDialogFragment {

    private OnPositiveButtonClickListener mOnPositiveButtonClickListener;
    private TextView mTitle;
    private ScoreCreditSelectView mScoreCreditSelectView;
    private Button mBtnCancel;
    private Button mBtnEnter;

    //gap is the years in between like 2016 - 2020 the gap is four years
    private int gap;

    /**
     *
     * @param start
     * @param end
     * @return start 和 end 都会作为这个dialog的标题出现
     */
    public static SelectYearDialog newInstance(String start, String end) {
        Bundle args = new Bundle();
        args.putString("start", start);
        args.putString("ending", end);

        SelectYearDialog fragment = new SelectYearDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String startYear = getArguments().getString("start");
        String endYear = getArguments().getString("ending");

        gap = Integer.parseInt(endYear) - Integer.parseInt(startYear);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_credit_select_year,
                null);
        initView(view);
        Dialog dialog = createBottomDialog(view);

        mTitle.setText(String.format("%s至%s学年", startYear, endYear));

        mScoreCreditSelectView.setNpStartYear(startYear);
        mScoreCreditSelectView.setNpEndYear(endYear);
        mScoreCreditSelectView.setOnValueChangeListener((start, end) -> {
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
                        mScoreCreditSelectView.getNpStartYear(), mScoreCreditSelectView.getNpEndYear());
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
        mScoreCreditSelectView = view.findViewById(R.id.credit_year_select_view);
        mBtnCancel = view.findViewById(R.id.btn_cancel);
        mBtnEnter = view.findViewById(R.id.btn_confirm);
    }

    public interface OnPositiveButtonClickListener {
        void onPositiveButtonClick(String start, String end);
    }
}
