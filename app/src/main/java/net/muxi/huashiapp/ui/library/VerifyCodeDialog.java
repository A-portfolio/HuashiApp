package net.muxi.huashiapp.ui.library;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.event.VerifyCodeSuccessEvent;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.widget.CenterDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyCodeDialog extends CenterDialogFragment {

    @BindView(R.id.txv_verify)
    TextView mTxvVerify;
    @BindView(R.id.btn_retry)
    Button mBtnRetry;
    @BindView(R.id.img_verify)
    ImageView mImgVerify;
    @BindView(R.id.et_verify)
    EditText mEtVerify;
    @BindView(R.id.rl_verify)
    RelativeLayout mRlVerify;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;

    private static int MAX = 4;
    private static String inputContent;
    private String url = "http://202.114.34.15/reader/captcha.php";
    private InputCompleteListener inputCompleteListener;
    private VerifyCodeDialog.OnPositiveButtonClickListener mOnPositiveButtonClickListener;

    public static VerifyCodeDialog newInstance() {
        VerifyCodeDialog fragment = new VerifyCodeDialog();
        Bundle args = new Bundle();
        args.putString("inputContent", inputContent);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_lib_verify, null);
        ButterKnife.bind(this, view);
        showCaptcha(url, getContext());
        setInputCompleteListener(inputCompleteListener);
        setEditTextListener();
        mBtnRetry.setOnClickListener(v -> {
            showCaptcha(url, getContext());
        });
        mBtnCancel.setOnClickListener(v -> {
            dismiss();
        });
        mBtnEnter.setOnClickListener(v -> {
            if (mOnPositiveButtonClickListener != null) {
                mOnPositiveButtonClickListener.onPositiveButtonClick(getEditContent());
            }
            RxBus.getDefault().send(new VerifyCodeSuccessEvent(inputContent));
            dismiss();
        });

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wmlp);
        window.setBackgroundDrawableResource(R.drawable.bg_center_dialog);
        return dialog;
    }

    private void setEditTextListener() {
        mEtVerify.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputContent = mEtVerify.getText().toString();
//                RxBus.getDefault().send();
                if (inputCompleteListener != null) {
                    if (inputContent.length() >= MAX) {
                        inputCompleteListener.inputComplete();
                    } else {
                        inputCompleteListener.invalidContent();
                    }
                }
            }
        });
    }

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public interface InputCompleteListener {

        void inputComplete();

        void invalidContent();
    }

    public void setOnPositiveButtonClickListener(
            VerifyCodeDialog.OnPositiveButtonClickListener onPositiveButtonClickListener) {
        mOnPositiveButtonClickListener = onPositiveButtonClickListener;
    }

    public interface OnPositiveButtonClickListener {
        void onPositiveButtonClick(String inputContent);
    }

    public String getEditContent() {
        return inputContent;
    }

    private View showCaptcha(String url, Context context) {
        Picasso.with(context).load(url).into(mImgVerify, new Callback() {
            @Override
            public void onSuccess() {
                Logger.d("验证码图片加载成功");
            }

            @Override
            public void onError() {
                Logger.d("验证码图片加载失败");
            }
        });
        return mImgVerify;
    }
}
