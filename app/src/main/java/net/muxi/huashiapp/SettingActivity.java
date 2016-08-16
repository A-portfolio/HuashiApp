package net.muxi.huashiapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.common.util.ToastUtil;
import net.muxi.huashiapp.common.util.ZhugeUtils;
import net.muxi.huashiapp.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/5/17.
 */
public class SettingActivity extends ToolbarActivity {

    @BindView(R.id.switch_course_remind)
    SwitchCompat mSwitchCourseRemind;
    @BindView(R.id.switch_library_remind)
    SwitchCompat mSwitchLibraryRemind;
    @BindView(R.id.switch_card_remind)
    SwitchCompat mSwitchCardRemind;
    @BindView(R.id.switch_score_remind)
    SwitchCompat mSwitchScoreRemind;
    @BindView(R.id.switch_all)
    SwitchCompat mSwitchAll;
    @BindView(R.id.btn_logout)
    Button mBtnLogout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private PreferenceUtil sp;
    private AppBarLayout mAppBarLayout;

    private String preSchedule;
    private String preLibrary;
    private String preCard;
    private String preScore;
    private String preAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setTitle("设置");
        sp = new PreferenceUtil();

        preSchedule = getResources().getString(R.string.pre_schedule);
        preLibrary = getResources().getString(R.string.pre_library);
        preCard = getResources().getString(R.string.pre_card);
        preScore = getResources().getString(R.string.pre_score);
        preAll = getResources().getString(R.string.pre_all);

        loadAllValue();

        mSwitchAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    setAllValue(true);
                }else {
                    setAllValue(false);
                }
            }
        });

        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                App.clearLibUser();
                App.clearUser();
                sp.clearAllData();
                startActivity(intent);
                ToastUtil.showShort("注销成功");
                SettingActivity.this.finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        saveAllValue();
        ZhugeUtils.sendEvent("各消息提醒状态","课程提醒" + mSwitchCourseRemind.isChecked() +
        "图书馆消息提醒" + mSwitchLibraryRemind.isChecked() +
        "学生卡消息提醒" + mSwitchCardRemind.isChecked() +
        "成绩消息提醒" + mSwitchScoreRemind.isChecked());
        super.onBackPressed();
    }

    private void setAllValue(boolean value) {
        mSwitchCourseRemind.setChecked(value);
        mSwitchLibraryRemind.setChecked(value);
        mSwitchCardRemind.setChecked(value);
        mSwitchScoreRemind.setChecked(value);
        mSwitchAll.setChecked(value);
    }

    private void saveAllValue() {
        sp.saveBoolean(preSchedule, mSwitchCourseRemind.isChecked());
        sp.saveBoolean(preLibrary, mSwitchLibraryRemind.isChecked());
        sp.saveBoolean(preCard, mSwitchCardRemind.isChecked());
        sp.saveBoolean(preScore, mSwitchScoreRemind.isChecked());
        sp.saveBoolean(preAll, mSwitchAll.isChecked());
    }

    private void loadAllValue() {
        mSwitchCourseRemind.setChecked(sp.getBoolean(preSchedule, true));
        mSwitchLibraryRemind.setChecked(sp.getBoolean(preLibrary,true));
        mSwitchCardRemind.setChecked(sp.getBoolean(preCard,true));
        mSwitchScoreRemind.setChecked(sp.getBoolean(preScore,true));
        mSwitchAll.setChecked(sp.getBoolean(preAll,true));
    }

}
