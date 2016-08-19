//package net.muxi.huashiapp.common.util;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import com.tencent.connect.share.QzoneShare;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;
//import com.tencent.tauth.UiError;
//
//import net.muxi.huashiapp.App;
//import net.muxi.huashiapp.R;
//import net.muxi.huashiapp.common.base.BaseActivity;
//
///**
// * Created by ybao on 16/8/17.
// */
//public class ShareUtils {
//
//    public static Tencent mTencent;
//
//    public static void shareToQzone(Context context,String title,String content,String url) {
//        final Bundle params = new Bundle();
//        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
//        if (content != null) {
//            params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, content);//选填
//        }
//        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);//必填
//        mTencent = Tencent.createInstance("1105548375",App.getContext());
//        mTencent.shareToQzone((BaseActivity)context, params, new BaseUiListener());
//    }
//
//    private static class BaseUiListener implements IUiListener {
//
//        @Override
//        public void onError(UiError e) {
//            Logger.d(e.errorDetail);
//            ToastUtil.showShort(App.getContext().getString(R.string.tip_share_fail));
//        }
//
//        @Override
//        public void onCancel() {
//        }
//
//        @Override
//        public void onComplete(Object o) {
//            ToastUtil.showShort(App.getContext().getString(R.string.tip_share_success));
//        }
//    }
//
//
//}
