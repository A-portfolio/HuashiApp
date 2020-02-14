package net.muxi.huashiapp.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.utils.AppUtil;

import net.muxi.huashiapp.BuildConfig;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.webview.WebViewActivity;


/**
 * Created by december on 16/8/1.
 */
public class AboutActivity extends ToolbarActivity {

    private RelativeLayout mRootLayout;
    private ImageView mImgCcnubox;
    private TextView mTvName;
    private TextView mTvVersionname;
    private TextView mTvMuxiLink;
    private View mDivider;
    private TextView mTvCcnuboxGroup;
    private TextView mTvCopy;
    private View mDivider2;
    private TextView mTvMuxiGroup;
    private TextView mTvCopy2;

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        setTitle(" ");
        mTvVersionname.setText("版本" + BuildConfig.VERSION_NAME);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_muxi_link){
            Intent intent = WebViewActivity.newIntent(AboutActivity.this,"http://www.muxixyz.com");
            startActivity(intent);
        }else if (id == R.id.tv_copy){
            AppUtil.clipToClipBoard(this, "576225292");
            showSnackbarShort("成功复制到粘贴板");
        }else if (id == R.id.tv_copy2){
            AppUtil.clipToClipBoard(this, "534239958");
            showSnackbarShort("成功复制到粘贴板");
        }
    }

    private void initView() {
        mRootLayout = findViewById(R.id.root_layout);
        mImgCcnubox = findViewById(R.id.img_ccnubox);
        mTvName = findViewById(R.id.tv_name);
        mTvVersionname = findViewById(R.id.tv_versionname);
        mTvMuxiLink = findViewById(R.id.tv_muxi_link);
        mDivider = findViewById(R.id.divider);
        mTvCcnuboxGroup = findViewById(R.id.tv_ccnubox_group);
        mTvCopy = findViewById(R.id.tv_copy);
        mDivider2 = findViewById(R.id.divider2);
        mTvMuxiGroup = findViewById(R.id.tv_muxi_group);
        mTvCopy2 = findViewById(R.id.tv_copy2);
        mTvMuxiLink.setOnClickListener(v -> onClick(v));
        mTvCopy.setOnClickListener(v -> onClick(v));
        mTvCopy2.setOnClickListener(v -> onClick(v));
    }
}




