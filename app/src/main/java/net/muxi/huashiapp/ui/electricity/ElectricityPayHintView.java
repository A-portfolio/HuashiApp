package net.muxi.huashiapp.ui.electricity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by december on 17/3/2.
 */

public class ElectricityPayHintView extends RelativeLayout {

    @BindView(R.id.view_close_btn)
    ImageView mViewCloseBtn;
    @BindView(R.id.tv_copy)
    TextView mTvCopy;
    @BindView(R.id.tv_name)
    TextView mTvName;

    private Context mContext;

    public ElectricityPayHintView(Context context) {
        super(context);
        mContext = context;

        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_electricity_pay_hint, this, true);
        ButterKnife.bind(this, view);

    }

    @OnClick({R.id.view_close_btn, R.id.tv_copy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_close_btn:
                ((BaseActivity) mContext).onBackPressed();
                break;
            case R.id.tv_copy:
                ClipboardManager manager = (ClipboardManager) getContext()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                manager.setPrimaryClip(ClipData.newPlainText(null, mTvName.getText()));
                if (manager.hasPrimaryClip()) {
                    manager.getPrimaryClip().getItemAt(0).getText();
                }
                showSnackbarShort("成功复制到粘贴板");
                break;
        }
    }

    public void showSnackbarShort(String msg) {
        Snackbar.make(getRootView(), msg, Snackbar.LENGTH_SHORT)
                .show();
    }

}
