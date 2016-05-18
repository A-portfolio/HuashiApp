package net.muxi.huashiapp.common.ui;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.PreferenceUtil;

/**
 * Created by ybao on 16/5/17.
 */
public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private PreferenceUtil sp;
    private Toolbar mToolbar;
    private AppBarLayout mAppBarLayout;

    private CheckBoxPreference mSchedulePreference;
    private CheckBoxPreference mLibraryPreference;
    private CheckBoxPreference mCardPreference;
    private CheckBoxPreference mScorePreference;
    private CheckBoxPreference mAllPreference;
    private String preSchedule;
    private String preLibrary;
    private String preCard;
    private String preScore;
    private String preAll;

    private Button mBtnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_preference_btn);
        addPreferencesFromResource(R.xml.preference);
        initActionBar();
        sp = new PreferenceUtil();


        preSchedule = getResources().getString(R.string.pre_schedule);
        preLibrary = getResources().getString(R.string.pre_library);
        preCard = getResources().getString(R.string.pre_card);
        preScore = getResources().getString(R.string.pre_score);
        preAll = getResources().getString(R.string.pre_all);

        initView();
        loadAllValue();

        mSchedulePreference.setOnPreferenceChangeListener(this);
        mLibraryPreference.setOnPreferenceChangeListener(this);
        mCardPreference.setOnPreferenceChangeListener(this);
        mScorePreference.setOnPreferenceChangeListener(this);
        mAllPreference.setOnPreferenceChangeListener(this);

    }

    private void initView() {
//        mBtnLogout = new Button(this);
//        mBtnLogout.setText("注销");
//        ListView v = getListView();
//        v.addFooterView(mBtnLogout);
        mSchedulePreference = (CheckBoxPreference) findPreference(preSchedule);
        mLibraryPreference = (CheckBoxPreference) findPreference(preLibrary);
        mCardPreference = (CheckBoxPreference) findPreference(preCard);
        mScorePreference = (CheckBoxPreference) findPreference(preScore);
        mAllPreference = (CheckBoxPreference) findPreference(preAll);

    }

    private void initActionBar() {
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        mAppBarLayout = (AppBarLayout) LayoutInflater.from(this).inflate(R.layout.view_toolbar, root, false);
        mToolbar = (Toolbar) mAppBarLayout.findViewById(R.id.toolbar);
        mToolbar.setTitle("设置");
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAllValue();
                onBackPressed();
            }
        });
        root.addView(mAppBarLayout, 0);

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveAllValue();
                onBackPressed();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(preAll)) {
            setAllValue((boolean) newValue);
        }
        return true;
    }

    private void setAllValue(boolean value) {
        mSchedulePreference.setChecked(value);
        mLibraryPreference.setChecked(value);
        mCardPreference.setChecked(value);
        mScorePreference.setChecked(value);
        mAllPreference.setChecked(value);
    }

    private void saveAllValue() {
        sp.saveBoolean(preSchedule, mSchedulePreference.isChecked());
        sp.saveBoolean(preLibrary, mLibraryPreference.isChecked());
        sp.saveBoolean(preCard, mCardPreference.isChecked());
        sp.saveBoolean(preScore, mScorePreference.isChecked());
        sp.saveBoolean(preAll, mAllPreference.isChecked());
    }

    private void loadAllValue() {
        mSchedulePreference.setDefaultValue(sp.getBoolean(preSchedule, true));
        mLibraryPreference.setDefaultValue(sp.getBoolean(preLibrary));
        mCardPreference.setDefaultValue(sp.getBoolean(preCard));
        mScorePreference.setDefaultValue(sp.getBoolean(preScore));
        mAllPreference.setDefaultValue(sp.getBoolean(preAll));
    }
}
