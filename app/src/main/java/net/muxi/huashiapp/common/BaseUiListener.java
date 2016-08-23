package net.muxi.huashiapp.common;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.ToastUtil;

/**
 * Created by ybao on 16/8/17.
 */
public class BaseUiListener implements IUiListener {

    @Override
    public void onError(UiError e) {
        Logger.d(e.errorDetail);
        Logger.d("share error");
        ToastUtil.showShort(App.sContext.getString(R.string.tip_share_fail));
    }

    @Override
    public void onCancel() {
        Logger.d("cancel");
    }

    @Override
    public void onComplete(Object o) {
        Logger.d("complete");
        ToastUtil.showShort(App.sContext.getString(R.string.tip_share_success));
    }

}
