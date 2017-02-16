package net.muxi.huashiapp.ui.studyroom;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseActivity;
import net.muxi.huashiapp.ui.SuggestionActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by december on 17/2/10.
 */

public class StudyRoomCorrectView extends RelativeLayout {


    @BindView(R.id.btn_feedback)
    Button mBtnFeedback;
    @BindView(R.id.view_close_btn)
    ImageView mViewCloseBtn;

    private Context mContext;

    private StudyRoomCorrectView mView;

    public StudyRoomCorrectView(Context context) {
        super(context);
        mContext = context;

        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_study_room_correct, this, true);
        ButterKnife.bind(this, view);

        mViewCloseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) mContext).onBackPressed();
            }
        });
    }

    @OnClick(R.id.btn_feedback)
    public void onClick() {
        Intent intent = new Intent();
        intent.setClass(mContext, SuggestionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        removeView(mView);
    }


}
