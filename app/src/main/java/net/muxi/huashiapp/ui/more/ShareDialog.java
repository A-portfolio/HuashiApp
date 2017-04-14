package net.muxi.huashiapp.ui.more;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

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
import com.tencent.tauth.Tencent;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.common.listener.BaseUiListener;
import net.muxi.huashiapp.ui.main.OnRecyclerItemClickListener;
import net.muxi.huashiapp.ui.webview.ShareAdapter;
import net.muxi.huashiapp.util.AppUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.ToastUtil;
import net.muxi.huashiapp.widget.BottomDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 17/2/26.
 */

public class ShareDialog extends BottomDialogFragment implements IWeiboHandler.Response {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private static final String WEB_URL = "url";
    private static final String WEB_TITLE = "title";
    private static final String WEB_INTRO = "intro";
    private static final String WEB_ICON_URL = "icon_url";


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

    private Integer[] pics = {R.drawable.ic_share_toqq, R.drawable.ic_share_towx, R.drawable.ic_share_toweibo,
            R.drawable.ic_share_toqzone, R.drawable.ic_share_tomoments, R.drawable.ic_share_copy_link};

    private String[] desc = {"QQ好友", "微信好友", "微博", "QQ空间", "朋友圈", "复制下载链接"};

    private List<Integer> mpic;
    private List<String> mdesc;

    private ShareAdapter mShareAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_shareto, null);
        ButterKnife.bind(this, view);

        Dialog dialog = createBottomDialog(view);


        title = getActivity().getIntent().hasExtra(WEB_TITLE) ? getActivity().getIntent().getStringExtra(WEB_TITLE) : getActivity().getIntent().getStringExtra(WEB_URL);
        url = getActivity().getIntent().hasExtra(WEB_URL) ? getActivity().getIntent().getStringExtra(WEB_URL) : "";
        iconUrl = getActivity().getIntent().hasExtra(WEB_ICON_URL) ? getActivity().getIntent().getStringExtra(WEB_ICON_URL) : "http://ww3.sinaimg.cn/large/65e4f1e6gw1f9h8gdxc16j204004074e.jpg";
        intro = getActivity().getIntent().hasExtra(WEB_INTRO) ? getActivity().getIntent().getStringExtra(WEB_INTRO) : "";


        api = WXAPIFactory.createWXAPI(getContext(), APP_ID, false);
        api.registerApp(APP_ID);

        mBaseUiListener = new BaseUiListener();
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(getContext(), Constants.WEIBO_KEY);
        mWeiboShareAPI.registerApp();
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getActivity().getIntent(), this);
        }

        mpic = new ArrayList<>();
        mdesc = new ArrayList<>();

        mpic.addAll(Arrays.asList(pics));
        mdesc.addAll(Arrays.asList(desc));
        mShareAdapter = new ShareAdapter(mpic, mdesc);
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mShareAdapter);

        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                switch (vh.getLayoutPosition()) {
                    case 0:
                        break;
                    case 1:
                        break;

                    case 5:
                        AppUtil.clipToClipBoard(getContext(), "http://ccnubox.muxixyz.com");
                        ((BaseActivity) getActivity()).showSnackbarShort(getResources().getString(R.string.tip_copy_success));
                        dialog.dismiss();
                        break;


                }
            }
        });


        return dialog;


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
        mTencent.shareToQzone(getActivity(), params, mBaseUiListener);
//            }
//
//        });
    }

    public void shareTOWeixin() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = intro;
        Logger.d(getActivity().getExternalCacheDir() + "/" + title + ".jpg");
        Bitmap bmp = BitmapFactory.decodeFile(getActivity().getExternalCacheDir() + "/" + title + ".jpg");
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
//        bmp.recycle();
        msg.setThumbImage(bmp);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = type + System.currentTimeMillis();
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    public void shareTOMements(){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = intro;
        Logger.d(getActivity().getExternalCacheDir() + "/" + title + ".jpg");
        Bitmap bmp = BitmapFactory.decodeFile(getActivity().getExternalCacheDir() + "/" + title + ".jpg");
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
//        bmp.recycle();
        msg.setThumbImage(bmp);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = type + System.currentTimeMillis();
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);

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
        mWeiboShareAPI.sendRequest(getActivity(), request); //发送请求消息到微博，唤起微博分享界面
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


}



