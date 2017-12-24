package net.muxi.huashiapp.ui.library;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
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

public class VerifyCodeView extends RelativeLayout {

    EditText mEtVerify;
    ImageView mImgVerify;
    RelativeLayout mRlVerify;
    TextView mRetryHint;

    private static int MAX = 4;
    private String inputContent;
    private String url = "http://202.114.34.15/reader/captcha.php";

    public VerifyCodeView(Context context) {
        this(context,null,0);
    }

    public VerifyCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View root = View.inflate(context, R.layout.dialog_lib_verify, this);
        mEtVerify  = (EditText) root.findViewById(R.id.et_verify);
        mImgVerify = (ImageView) root.findViewById(R.id.img_verify);
     //   mBtnVerify = (Button) root.findViewById(R.id.btn_verify);
        mRlVerify  = (RelativeLayout) root.findViewById(R.id.rl_verify);
        mRetryHint  = (TextView) findViewById(R.id.tv_retry);
        showCaptcha(url,getContext());
        refreshVerifyCode();
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


    public interface InputCompleteListener {

        void inputComplete();

        void invalidContent();
    }

    public String getEditContent() {
        return inputContent;
    }

    private void refreshVerifyCode(){
        mRetryHint.setOnClickListener(view->{
            showCaptcha(url,getContext());
        });
    }

    public void setVisible(){
        mEtVerify.setVisibility(VISIBLE);
        mRetryHint.setVisibility(VISIBLE);
        mImgVerify.setVisibility(VISIBLE);
        mRlVerify.setVisibility(VISIBLE);
    }

    private View showCaptcha(String url,Context context){
        ImageView view = (ImageView) findViewById(R.id.img_verify);
        Picasso.with(context).load(url).into(view, new Callback() {
            @Override
            public void onSuccess() {
                Logger.d("验证码图片加载成功");
                RxBus.getDefault().send(new VerifyCodeSuccessEvent());
            }
            @Override
            public void onError()
            {
                Logger.d("验证码图片加载失败");
            }
        })  ;
        return view;
    }
   // private SimpleDraweeView showCaptcha(String url, Context context) {
     //   Piccasso
     //   return view;
    //}

}
