package net.muxi.huashiapp.ui.webview;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.tauth.Tencent;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.listener.BaseUiListener;
import net.muxi.huashiapp.util.AppUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.ToastUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/18.
 */
public class WebViewActivity extends ToolbarActivity implements IWeiboHandler.Response {

    private static final String WEB_URL = "url";
    private static final String WEB_TITLE = "title";
    private static final String WEB_INTRO = "intro";
    private static final String WEB_ICON_URL = "icon_url";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.webview)
    WebView mWebview;
    @BindView(R.id.custom_progress_bar)
    NumberProgressBar mCustomProgressBar;

    public static Tencent mTencent;
    private IWeiboShareAPI mWeiboShareAPI;
    private BaseUiListener mBaseUiListener;

    //对应网站应用的各项属性
    private String url;
    private String title;
    private String iconUrl;
    private String intro;

    private static final String APP_ID = "wxf054659decf8f748";
    private IWXAPI api;
    private String type = "webpage";



    public static void start(Context context){}


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        title = getIntent().hasExtra(WEB_TITLE) ? getIntent().getStringExtra(WEB_TITLE) : getIntent().getStringExtra(WEB_URL);
        url = getIntent().hasExtra(WEB_URL) ? getIntent().getStringExtra(WEB_URL) : "";
        iconUrl = getIntent().hasExtra(WEB_ICON_URL) ? getIntent().getStringExtra(WEB_ICON_URL) : "http://ww3.sinaimg.cn/large/65e4f1e6gw1f9h8gdxc16j204004074e.jpg";
        intro = getIntent().hasExtra(WEB_INTRO) ? getIntent().getStringExtra(WEB_INTRO) : "";
        setTitle(title);

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        mWebview.setWebChromeClient(new BrowserClient());
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                title = webView.getTitle();
                setTitle(title);
                Logger.d(title);
            }
        });
        mWebview.loadUrl(url);

        api = WXAPIFactory.createWXAPI(getApplicationContext(), APP_ID, false);
        api.registerApp(APP_ID);

        mBaseUiListener = new BaseUiListener();
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, Constants.WEIBO_KEY);
        mWeiboShareAPI.registerApp();
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }

    }




    public static Intent newIntent(Context context, String url, String title, String intro, String iconUrl) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WEB_URL, url);
        intent.putExtra(WEB_TITLE, title);
        intent.putExtra(WEB_INTRO, intro);
        intent.putExtra(WEB_ICON_URL, iconUrl);
        return intent;
    }

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WEB_URL, url);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_refresh:
                mWebview.reload();
                return true;
            case R.id.action_copy_url:
                AppUtil.clipToClipBoard(WebViewActivity.this, mWebview.getUrl());
                break;
            case R.id.action_open_browser:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mWebview.getUrl()));
                startActivity(browserIntent);
                break;
            case R.id.action_share_qq:
                shareToQzone(title, intro, url, iconUrl);
                break;
            case R.id.action_share_wechat:
                shareTOWeixin();
                break;
            case R.id.action_share_weibo:
                sendMultiMessage(true, false, false, false, false, false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebview.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebview.destroy();
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
        public void onReceivedTitle(com.tencent.smtt.sdk.WebView view, String title) {
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


    public void shareTOWeixin() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = intro;
        Logger.d(getExternalCacheDir() + "/" + title + ".jpg");
        Bitmap bmp = BitmapFactory.decodeFile(getExternalCacheDir() + "/" + title + ".jpg");
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
//        bmp.recycle();
        msg.setThumbImage(bmp);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = type + System.currentTimeMillis();
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }


    public void shareToQzone(String title, String content, String url, String picUrl) {
        mTencent = Tencent.createInstance(Constants.QQ_KEY, App.sContext);
        final Bundle params = new Bundle();
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
        if (content != null) {
            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content);//选填
        }
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
        ArrayList<String> imgUrlList = new ArrayList<>();
        imgUrlList.add(picUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrlList);// 图片地址
        Log.d("share", "start share to zone");
//        ThreadManager.getMainHandler().post(new Runnable() {
//            @Override
//
//            public void run() {
//                Logger.d("start share");
        mTencent.shareToQzone(WebViewActivity.this, params, mBaseUiListener);
//            }
//
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUiListener);
    }

    public TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = title + " " + intro + " " + url;
        return textObject;
    }

    private void sendMultiMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
                                  boolean hasMusic, boolean hasVideo, boolean hasVoice) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        mWeiboShareAPI.sendRequest(this, request); //发送请求消息到微博，唤起微博分享界面
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareAPI.handleWeiboResponse(intent, this); //当前应用唤起微博分享后，返回当前应用
        ToastUtil.showShort(getString(R.string.tip_share_success));
    }

    @Override
    public void onResponse(BaseResponse baseResp) {//接收微客户端博请求的数据。
        switch (baseResp.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                ToastUtil.showShort(getString(R.string.tip_share_success));
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                Logger.d("cancel");
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                ToastUtil.showShort(getString(R.string.tip_share_fail));
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
