package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.R;

import butterknife.BindView;
import butterknife.OnClick;

public class VerifyCodeView extends RelativeLayout {

    @BindView(R.id.et_verify)
    EditText mEtVerify;
    @BindView(R.id.img_verify)
    ImageView mImgVerify;
    @BindView(R.id.btn_verify)
    Button mBtnVerify;
    @BindView(R.id.rl_verify)
    RelativeLayout mRlVerify;

    private static int MAX = 4;
    private String inputContent;
    private String url = "http://202.114.34.15/reader/captcha.php";

    public VerifyCodeView(Context context) {
        this(context, null);
    }

    public VerifyCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.dialog_lib_verify, this);

        View view = showCaptcha(url, getContext());
        view.buildDrawingCache();
        Drawable drawable = new BitmapDrawable(view.getDrawingCache());
        mImgVerify.setImageDrawable(drawable);
        mEtVerify.setCursorVisible(false);
        setEditTextListener();
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

    private InputCompleteListener inputCompleteListener;

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    @OnClick(R.id.btn_verify)
    public void onViewClicked() {
        mRlVerify.setVisibility(View.GONE);

    }

    public interface InputCompleteListener {

        void inputComplete();

        void invalidContent();
    }

    public String getEditContent() {
        return inputContent;
    }

    private View showCaptcha(String url, Context context) {
        SimpleDraweeView view = new SimpleDraweeView(context);
        view.setImageURI(Uri.parse(url));
        return view;
    }

}
