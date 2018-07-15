package net.muxi.huashiapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.SwitchCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.common.util.PreferenceUtil;

import net.muxi.huashiapp.R;


/**
 * Created by ybao on 16/5/17.
 */
public class SettingActivity extends ToolbarActivity {

    private PreferenceUtil sp;
    private AppBarLayout mAppBarLayout;

    private String preSchedule;
    private String preLibrary;
    private String preCard;
    private String preScore;
    private String preAll;
    private ImageView mImg;
    private SwitchCompat mSwitchCourseRemind;
    private ImageView mImg1;
    private TextView mLibraryNotice;
    private SwitchCompat mSwitchLibraryRemind;
    private ImageView mImg2;
    private TextView mCardNotice;
    private SwitchCompat mSwitchCardRemind;
    private ImageView mImg3;
    private SwitchCompat mSwitchScoreRemind;


    public static void start(Context context) {
        Intent starter = new Intent(context, SettingActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
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

    private void initView() {
        mImg = findViewById(R.id.img);
        mSwitchCourseRemind = findViewById(R.id.switch_course_remind);
        mImg1 = findViewById(R.id.img1);
        mLibraryNotice = findViewById(R.id.library_notice);
        mSwitchLibraryRemind = findViewById(R.id.switch_library_remind);
        mImg2 = findViewById(R.id.img2);
        mCardNotice = findViewById(R.id.card_notice);
        mSwitchCardRemind = findViewById(R.id.switch_card_remind);
        mImg3 = findViewById(R.id.img3);
        mSwitchScoreRemind = findViewById(R.id.switch_score_remind);
    }
}
