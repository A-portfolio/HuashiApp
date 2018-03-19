package net.muxi.huashiapp.ui.main;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.muxistudio.appcommon.Constants;
import com.muxistudio.appcommon.appbase.BaseAppActivity;
import com.muxistudio.appcommon.utils.CopyBoardUtil;
import com.muxistudio.common.util.ToastUtil;

import net.muxi.huashiapp.R;


/**
 * Created by kolibreath on 18-2-25.
 */

public class DetailActivity extends BaseAppActivity {

    private TextView mTvDetailTitle;
    private TextView mTvDetailContent;
    private TextView mTvDetailQqGroup;
    private Button mBtnDetailConfirm;

    void submit(View view) {
        int id = view.getId();
        if (id == R.id.btn_detail_confirm){
            finish();
        }else if (id == R.id.tv_detail_qq_group){
            CopyBoardUtil.copy(Constants.QQ_GROUP_NUMBER);
            ToastUtil.showShort("已复制到剪贴板");
        }
    }

    private String mDetailString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        mDetailString = getIntent().getStringExtra("detail");
        setDetailFormat();
    }

    private void setDetailFormat() {
        mTvDetailQqGroup.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvDetailQqGroup.setText(Constants.QQ_GROUP_NUMBER);
        mTvDetailQqGroup.setTextColor(getResources().getColor(R.color.blue));
        mTvDetailContent.setText(mDetailString);
    }

    private void initView() {
        mTvDetailTitle = findViewById(R.id.tv_detail_title);
        mTvDetailContent = findViewById(R.id.tv_detail_content);
        mTvDetailQqGroup = findViewById(R.id.tv_detail_qq_group);
        mBtnDetailConfirm = findViewById(R.id.btn_detail_confirm);
        mBtnDetailConfirm.setOnClickListener(v -> submit(v));
        mTvDetailQqGroup.setOnClickListener(v -> submit(v));
    }
}
