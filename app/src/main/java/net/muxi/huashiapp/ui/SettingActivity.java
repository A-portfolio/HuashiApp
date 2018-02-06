package net.muxi.huashiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.util.PreferenceUtil;

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
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private PreferenceUtil sp;
    private AppBarLayout mAppBarLayout;

    private String preSchedule;
    private String preLibrary;
    private String preCard;
    private String preScore;
    private String preAll;


    public static void start(Context context){
        Intent starter = new Intent(context,SettingActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setTitle("通知栏提醒");
        sp = new PreferenceUtil();

        preSchedule = getResources().getString(R.string.pre_schedule);
        preLibrary = getResources().getString(R.string.pre_library);
        preCard = getResources().getString(R.string.pre_card);
        preScore = getResources().getString(R.string.pre_score);
        preAll = getResources().getString(R.string.pre_all);

        loadAllValue();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        saveAllValue();
        super.onBackPressed();
    }

    private void setAllValue(boolean value) {
        mSwitchCourseRemind.setChecked(value);
        mSwitchLibraryRemind.setChecked(value);
        mSwitchCardRemind.setChecked(value);
        mSwitchScoreRemind.setChecked(value);
    }

    private void saveAllValue() {
        sp.saveBoolean(preSchedule, mSwitchCourseRemind.isChecked());
        sp.saveBoolean(preLibrary, mSwitchLibraryRemind.isChecked());
        sp.saveBoolean(preCard, mSwitchCardRemind.isChecked());
        sp.saveBoolean(preScore, mSwitchScoreRemind.isChecked());
    }

    private void loadAllValue() {
        mSwitchCourseRemind.setChecked(sp.getBoolean(preSchedule, true));
        mSwitchLibraryRemind.setChecked(sp.getBoolean(preLibrary, true));
        mSwitchCardRemind.setChecked(sp.getBoolean(preCard, true));
        mSwitchScoreRemind.setChecked(sp.getBoolean(preScore, true));
    }

}
