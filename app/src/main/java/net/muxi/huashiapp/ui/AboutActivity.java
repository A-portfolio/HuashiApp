package net.muxi.huashiapp.ui;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import net.muxi.huashiapp.BuildConfig;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.ui.webview.WebViewActivity;
import net.muxi.huashiapp.util.AppUtil;
import net.muxi.huashiapp.util.ZhugeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by december on 16/8/1.
 */
public class AboutActivity extends ToolbarActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_versionname)
    TextView mTvVersionname;
    @BindView(R.id.tv_muxi_link)
    TextView mTvMuxiLink;
    @BindView(R.id.tv_ccnubox_group)
    TextView mTvCcnuboxGroup;
    @BindView(R.id.tv_muxi_group)
    TextView mTvMuxiGroup;
    @BindView(R.id.tv_copy)
    TextView mTvCopy;
    @BindView(R.id.tv_copy2)
    TextView mTvCopy2;


    public static void start(Context context) {
        Intent starter = new Intent(context, AboutActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        setTitle(" ");
        mTvVersionname.setText("版本" + BuildConfig.VERSION_NAME);
    }



    @OnClick({R.id.tv_muxi_link, R.id.tv_copy, R.id.tv_copy2})
    public void onClick(View view) {
        ClipboardManager manager;
        switch (view.getId()) {
            case R.id.tv_muxi_link:
                ZhugeUtils.sendEvent("打开木犀官网", "打开木犀官网");
                Intent intent = WebViewActivity.newIntent(AboutActivity.this, "http://muxistudio.com");
                startActivity(intent);
                break;
            case R.id.tv_copy:
//                manager = (ClipboardManager) this
//                        .getSystemService(Context.CLIPBOARD_SERVICE);
//                manager.setPrimaryClip(ClipData.newPlainText(null,mTvCcnuboxGroup.getText()));
//                if (manager.hasPrimaryClip()){
//                    manager.getPrimaryClip().getItemAt(0).getText();
//                }
                AppUtil.clipToClipBoard(this,"576225292");
                showSnackbarShort("成功复制到粘贴板");
                break;
            case R.id.tv_copy2:
//                manager = (ClipboardManager) this
//                        .getSystemService(Context.CLIPBOARD_SERVICE);
//                manager.setPrimaryClip(ClipData.newPlainText(null,mTvMuxiGroup.getText()));
//                if (manager.hasPrimaryClip()){
//                    manager.getPrimaryClip().getItemAt(0).getText();
//                }
                AppUtil.clipToClipBoard(this,"534239958");
                showSnackbarShort("成功复制到粘贴板");
                break;
        }
    }



//    public static class NoUnderlineSpan extends UnderlineSpan {
//
//        public NoUnderlineSpan() {
//        }
//
//        public NoUnderlineSpan(Parcel src) {
//        }
//
//        @Override
//        public void updateDrawState(TextPaint ds) {
//            super.updateDrawState(ds);
//            ds.setUnderlineText(false);
//        }
//    }

}



