package net.muxi.huashiapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

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
    @BindView(R.id.et_contact)
    EditText mEtContact;

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
    }

//        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mEtSuggestion.getText().length() == 0) {
//                    ToastUtil.showShort(getString(R.string.tip_write_suggestion_first));
//                    return;
//                }
//                if (mEtContact.getText().length() == 0){
//                    final MaterialDialog materialDialog = new MaterialDialog(SuggestionActivity.this);
//                    materialDialog.setTitle(App.sContext.getString(R.string.title_sugg_submit));
//                    materialDialog.setContent(App.sContext.getString(R.string.content_sugg_submit));
//                    materialDialog.setNegativeButton(App.sContext.getString(R.string.btn_negative), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            materialDialog.dismiss();
//                        }
//                    });
//                    materialDialog.setNegativeButtonColor(ContextCompat.getColor(context,R.color.colorPrimary));
//                    materialDialog.setPositiveButtonColor(ContextCompat.getColor(context,R.color.colorPrimary));
//                    materialDialog.setPositiveButton(App.sContext.getString(R.string.btn_positive), new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            sendSuggestion(mEtSuggestion.getText().toString() + "联系方式:" + mEtContact.getText().toString());
//                            materialDialog.dismiss();
//                        }
//                    });
//                    materialDialog.show();
//                }else {
//                    sendSuggestion(mEtSuggestion.getText().toString() + "联系方式:" + mEtContact.getText().toString());
//                }
//            }
//        });
//    }
//
//    public void sendSuggestion(String str){
//        if (NetStatus.isConnected()){
//            ZhugeUtils.sendEvent("意见提交",str);
//            final MaterialDialog materialDialog = new MaterialDialog(SuggestionActivity.this);
//            materialDialog.setTitle("匣爸温馨提示");
//            materialDialog.setContent("反馈成功!感谢您对华师匣子的支持!");
//            materialDialog.setNegativeButtonColor(ContextCompat.getColor(context,R.color.colorWhite));
//            materialDialog.setPositiveButtonColor(ContextCompat.getColor(context,R.color.colorPrimary));
//            materialDialog.setPositiveButton("确定", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    SuggestionActivity.this.finish();
//                }
//            });
//            materialDialog.setCanceledOnTouchOutside(false);
//            materialDialog.show();
////            ToastUtil.showShort("提交成功");
//
//        }else {
//            ToastUtil.showShort(App.sContext.getString(R.string.tip_check_net));
//        }
//    }


    //显示当前的字数
    public void setWordNum(TextView tv, String s) {
        if (s.length() <= MAX_WORD_NUM) {
            tv.setText(s.length() + "/" + MAX_WORD_NUM);
        } else {
            tv.setText(MAX_WORD_NUM + "/" + MAX_WORD_NUM);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
