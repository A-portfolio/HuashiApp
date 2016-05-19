package net.muxi.huashiapp.webview;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/18.
 */
public class WebViewActivity extends ToolbarActivity {

    private static final String WEB_URL = "url";
    private static final String WEB_TITLE = "title";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.appbar_layout)
    AppBarLayout mAppbarLayout;
    @Bind(R.id.webview)
    WebView mWebview;
    private String url;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        title = getIntent().getStringExtra(WEB_TITLE);
        url = getIntent().getStringExtra(WEB_URL);
        initToolbar(title);

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        mWebview.loadUrl(url);
    }


    public static Intent newIntent(Context context,String url,String title){
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra(WEB_URL,url);
        intent.putExtra(WEB_TITLE,title);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_refresh:
                mWebview.reload();
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
        getMenuInflater().inflate(R.menu.menu_webview,menu);
        return super.onCreateOptionsMenu(menu);
    }


    private class BrowserClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
