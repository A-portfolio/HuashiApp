package net.muxi.huashiapp.ui.main;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.util.CopyBoardUtil;
import net.muxi.huashiapp.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kolibreath on 18-2-25.
 */

public class DetailActivity extends BaseActivity {


    @BindView(R.id.tv_detail_qq_group)
    TextView mTvDetailQQ;
    @BindView(R.id.tv_detail_content)
    TextView mTVDetailContent;
    @BindView(R.id.btn_detail_confirm)
    Button mBtnDetailConfirm;
    @OnClick({R.id.btn_detail_confirm,R.id.tv_detail_qq_group})
    void submit(View view){
        switch (view.getId()){
            case R.id.btn_detail_confirm:
                finish();
                break;
            case R.id.tv_detail_qq_group:
                CopyBoardUtil.copy(Constants.QQ_GROUP_NUMBER);
                ToastUtil.showShort("已复制到剪贴板");
                break;
        }
    }
    private String mDetailString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        mDetailString  = getIntent().getStringExtra("detail");
        setDetailFormat();
    }

    private void setDetailFormat(){
        mTvDetailQQ.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvDetailQQ.setText(Constants.QQ_GROUP_NUMBER);
        mTvDetailQQ.setTextColor(getResources().getColor(R.color.blue));
        mTVDetailContent.setText(mDetailString);
    }

}
