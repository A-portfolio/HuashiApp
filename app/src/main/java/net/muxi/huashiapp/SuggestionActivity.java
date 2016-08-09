package net.muxi.huashiapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.util.MailUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 16/8/1.
 */
public class SuggestionActivity extends ToolbarActivity {

    private static final int MAX_WORD_NUM = 400;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.et_suggestion)
    EditText mEtSuggestion;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.tv_word_length)
    TextView mTvWordLength;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("意见反馈");
        mEtSuggestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setWordNum(mTvWordLength, s.toString());
                if (s.length() > MAX_WORD_NUM) {
                    mEtSuggestion.setText(s.toString().substring(0, 400));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

       mBtnSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               sendEmail("15927448708@163.com","huashiapp",mEtSuggestion.getText().toString());
           }
       });
    }

    public void sendEmail(final String toMail, final String title, final String body){
        new Thread(new Runnable() {
            public void run() {
                MailUtils emailUtil = new MailUtils();
                try {

                    String account = "360322537@qq.com";
                    String password = "987654321feng";

                    emailUtil.sendMail(toMail, account, "smtp.mxhichina.com",
                            account, password, title, body);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //显示当前的字数
    public void setWordNum(TextView tv, String s) {
        if (s.length() <= MAX_WORD_NUM) {
            tv.setText(s.length() + "/" + MAX_WORD_NUM);
        } else {
            tv.setText(MAX_WORD_NUM + "/" + MAX_WORD_NUM);
        }

    }

}
