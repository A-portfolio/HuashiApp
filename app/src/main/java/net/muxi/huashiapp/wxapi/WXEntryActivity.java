package net.muxi.huashiapp.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by december on 16/8/19.
 */
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

    private static final String APP_ID = "wxf054659decf8f748";
    private IWXAPI api;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, APP_ID,false);
        api.registerApp(APP_ID);
        api.handleIntent(getIntent(),this);
    }

    @Override
    public void onReq(BaseReq baseReq) {


    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = null;
        switch (baseResp.errCode){
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
                Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
                finish();
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送失败";
                Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
                finish();
                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "发送被拒绝";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;

            default:
                result = "发送返回";
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
                finish();
                break;
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

}


