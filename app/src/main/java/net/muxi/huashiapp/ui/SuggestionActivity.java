package net.muxi.huashiapp.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.FeedBack;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.common.util.NetUtil;
import com.umeng.analytics.MobclickAgent;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.more.FeedbackDialog;
import net.muxi.huashiapp.ui.more.LogoutDialog;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 16/8/1.
 */
public class SuggestionActivity extends ToolbarActivity {

    private static final int MAX_WORD_NUM = 400;
    private EditText mEtSuggestion;
    private TextView mTvWordLength;
    private ImageView mIvEdit;
    private EditText mEtContact;
    private View mDivider2;
    private TextView mGroupCcnubox;
    private TextView mTvCopy;
    private View mDivider3;
    private Button mBtnSubmit;

    public static void start(Context context) {
        Intent starter = new Intent(context, SuggestionActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        initView();
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

        mBtnSubmit.setOnClickListener(v -> {
            if (mEtSuggestion.getText().length() == 0) {
                showSnackbarShort(getString(R.string.tip_write_suggestion_first));
            } else {
                sendSuggestion(mEtContact.getText().toString(),mEtSuggestion.getText().toString());
            }
        });
    }

    public void sendSuggestion(String contact,String suggesion) {
        if (NetUtil.isConnected()) {
            MobclickAgent.onEvent(this, "suggestion_hand_in");
            FeedbackDialog feedbackDialog = new FeedbackDialog();
            feedbackDialog.show(getSupportFragmentManager(), "feedback_dialog");
            feedbackDialog.setOnClickListener(() -> {

              CampusFactory.getRetrofitService().
                  feedback(new FeedBack(contact,suggesion))
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(o ->{
                  },Throwable::printStackTrace,()->{});
              SuggestionActivity.this.finish();
            });
        } else {
            showSnackbarShort(App.sContext.getString(R.string.tip_check_net));
        }
    }


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

    public void onClick() {
        ClipboardManager manager = (ClipboardManager) this
                .getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText(null, mGroupCcnubox.getText()));
        if (manager.hasPrimaryClip()) {
            manager.getPrimaryClip().getItemAt(0).getText();
        }
        showSnackbarShort("成功复制到粘贴板");
    }

    private void initView() {
        mEtSuggestion = findViewById(R.id.et_suggestion);
        mTvWordLength = findViewById(R.id.tv_word_length);
        mIvEdit = findViewById(R.id.iv_edit);
        mEtContact = findViewById(R.id.et_contact);
        mDivider2 = findViewById(R.id.divider2);
        mGroupCcnubox = findViewById(R.id.group_ccnubox);
        mTvCopy = findViewById(R.id.tv_copy);
        mDivider3 = findViewById(R.id.divider3);
        mBtnSubmit = findViewById(R.id.btn_submit);
        mTvCopy.setOnClickListener(v -> onClick());
    }
}
