package net.muxi.huashiapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by december on 16/8/1.
 */
public class AboutActivity extends ToolbarActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_info)
    Button mBtnInfo;
    @BindView(R.id.btn_suggestion)
    Button mBtnSuggestion;
    @BindView(R.id.view)
    CardView mView;
    @BindView(R.id.tv_versionname)
    TextView mTvVersionname;
    @BindView(R.id.tv_muxi_link)
    TextView mTvMuxiLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        init();

    }

    public void init() {
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("关于我们");
        //设置超链接
        String MUXILink = "WWW.MUXI.COM";
        mTvMuxiLink.setText(MUXILink);
        NoUnderlineSpan mNoUnderlineSpan = new NoUnderlineSpan();
        if (mTvMuxiLink.getText() instanceof Spannable) {
            Spannable s = (Spannable) mTvMuxiLink.getText();
            s.setSpan(mNoUnderlineSpan, 0, s.length(), Spanned.SPAN_MARK_MARK);


        }
    }


    @OnClick({R.id.btn_info, R.id.btn_suggestion})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_info:
                break;
            case R.id.btn_suggestion:
                intent = new Intent(AboutActivity.this, SuggestionActivity.class);
                startActivity(intent);
                break;
        }
    }


    public static class NoUnderlineSpan extends UnderlineSpan {
        public NoUnderlineSpan() {
        }

        public NoUnderlineSpan(Parcel src) {
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
