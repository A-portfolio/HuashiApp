package net.muxi.huashiapp.webview;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.util.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/18.
 */
public class WebViewActivity extends ToolbarActivity {

    private static final String WEB_URL = "url";
    private static final String WEB_TITLE = "title";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.custom_progress_bar)
    NumberProgressBar mCustomProgressBar;


    private String url;
    private String title;

    private static final String APP_ID = "wxf054659decf8f748";
    private IWXAPI api;

    private String type = "webpage";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        title = getIntent().getStringExtra(WEB_TITLE);
        url = getIntent().getStringExtra(WEB_URL);
        setTitle(title);

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        mWebview.setWebChromeClient(new BrowserClient());
        mWebview.loadUrl(url);

        api = WXAPIFactory.createWXAPI(getApplicationContext(), APP_ID, false);
        api.registerApp(APP_ID);
            }







    public static Intent newIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WEB_URL, url);
        intent.putExtra(WEB_TITLE, title);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_refresh:
                mWebview.reload();
                return true;

            case R.id.action_share_wechat:
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = "https://xueer.muxixyz.com/";
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = "学而";
                msg.description = "就决定是你了";
                Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp,150,150, true);
                bmp.recycle();
                msg.setThumbImage(thumbBmp);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = type + System.currentTimeMillis();
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                api.sendReq(req);
                break;



            case R.id.action_copy_url:
                AppUtil.clipToClipBoard(WebViewActivity.this, mWebview.getUrl());
                break;
            case R.id.action_open_browser:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebview.getUrl()));
                startActivity(browserIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_webview, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private class BrowserClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

            mCustomProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mCustomProgressBar.setVisibility(View.GONE);
            } else {
                mCustomProgressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebview.canGoBack()) {
                mWebview.goBack();
                return true;
            }
            onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
