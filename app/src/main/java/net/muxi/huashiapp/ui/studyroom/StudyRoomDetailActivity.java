package net.muxi.huashiapp.ui.studyroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.ToolbarActivity;
import net.muxi.huashiapp.common.data.ClassRoom;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.util.DimensUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.NetStatus;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 17/2/1.
 */

public class StudyRoomDetailActivity extends ToolbarActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.grid_classroom_eight)
    GridLayout mGridClassroomEight;
    @BindView(R.id.grid_classroom_ten)
    GridLayout mGridClassroomTen;
    @BindView(R.id.grid_classroom_twelve)
    GridLayout mGridClassroomTwelve;
    @BindView(R.id.grid_classroom_fourteen)
    GridLayout mGridClassroomFourteen;
    @BindView(R.id.grid_classroom_sixteen)
    GridLayout mGridClassroomSixteen;
    @BindView(R.id.grid_classroom_eighteen)
    GridLayout mGridClassroomEighteen;
    @BindView(R.id.grid_classroom_twenty)
    GridLayout mGridClassroomTwenty;
    @BindView(R.id.study_detail_layout)
    RelativeLayout mStudyDetailLayout;


    private ClassRoom mClassRoom;


    public static void start(Context context, String query) {
        Intent starter = new Intent(context, StudyRoomDetailActivity.class);
        starter.putExtra("query", query);
        context.startActivity(starter);
    }

    //查询参数
    private String mQuery;

    private ImageView img;


    //测试用直接查询
    private String mTest = "第5周周三7号楼";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyroom_detail);
        ButterKnife.bind(this);

        setTitle("空闲教室列表");

        mQuery = getIntent().getStringExtra("query");
        Logger.d(mQuery + " ");

        if (!NetStatus.isConnected()) {
            showErrorSnackbarShort(R.string.tip_check_net);
            return;
        }
        showLoading();
        CampusFactory.getRetrofitService()
                .getClassRoom(getWeek(mQuery), getDayValue(mQuery), getBuidingValue(mQuery))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(classRoom -> {
                    mClassRoom = classRoom;
                    setNull();
                    setData();
                }, throwable -> {
                    throwable.printStackTrace();
                }, () -> {
                    hideLoading();
                });

    }


    //返回数据为空时显示图片
    private void setNull() {
        img = new ImageView(this);
        img.setImageResource(R.drawable.img_empty_classroom);
        if (mClassRoom.getValue1() == null) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.leftMargin = DimensUtil.dp2px(94f);
            mGridClassroomEight.addView(img, params);
        }
        if (mClassRoom.getValue3() == null) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.leftMargin = DimensUtil.dp2px(94f);
            mGridClassroomTen.addView(img, params);
        }
        if (mClassRoom.getValue5() == null) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.leftMargin = DimensUtil.dp2px(94f);
            mGridClassroomTwelve.addView(img, params);
        }
        if (mClassRoom.getValue7() == null) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.leftMargin = DimensUtil.dp2px(94f);
            mGridClassroomFourteen.addView(img, params);
        }
        if (mClassRoom.getValue9() == null) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.leftMargin = DimensUtil.dp2px(94f);
            mGridClassroomSixteen.addView(img, params);
        }
        if (mClassRoom.getValue11() == null) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.leftMargin = DimensUtil.dp2px(94f);
            mGridClassroomEighteen.addView(img, params);
        }
        if (mClassRoom.getValue13() == null) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.leftMargin = DimensUtil.dp2px(94f);
            mGridClassroomTwenty.addView(img, params);
        }
    }


    private void setData() {
        if (mClassRoom.getValue1() != null) {
            mGridClassroomEight.setRowCount(30);
            mGridClassroomEight.setColumnCount(4);
            for (int i = 0; i < mClassRoom.getValue1().size(); i++) {
                TextView context1 = new TextView(this);
                context1.setTextColor(getResources().getColor(R.color.colorBlack));
                context1.setText(mClassRoom.getValue1().get(i));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                params.rightMargin = DimensUtil.dp2px(68f);
//                params.width = DimensUtil.getScreenWidth() / 4;
                params.rightMargin = mGridClassroomEight.getWidth() / 5;
                params.topMargin = DimensUtil.dp2px(16f);
                mGridClassroomEight.addView(context1, params);
            }


        }

        if (mClassRoom.getValue3() != null) {
            mGridClassroomTen.setRowCount(30);
            mGridClassroomTen.setColumnCount(4);
            for (int i = 0; i < mClassRoom.getValue3().size(); i++) {
                TextView context2 = new TextView(this);
                context2.setTextColor(getResources().getColor(R.color.colorBlack));
                context2.setText(mClassRoom.getValue3().get(i));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                params.rightMargin = DimensUtil.dp2px(42f);
                params.width = DimensUtil.getScreenWidth() / 4;
                params.topMargin = DimensUtil.dp2px(16f);
                mGridClassroomTen.addView(context2, params);
            }
        }

        if (mClassRoom.getValue5() != null) {
            mGridClassroomTwelve.setRowCount(30);
            mGridClassroomTwelve.setColumnCount(4);
            for (int i = 0; i < mClassRoom.getValue5().size(); i++) {
                TextView context3 = new TextView(this);
                context3.setTextColor(getResources().getColor(R.color.colorBlack));
                context3.setText(mClassRoom.getValue5().get(i));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                params.rightMargin = DimensUtil.dp2px(42f);
                params.width = DimensUtil.getScreenWidth() / 4;
                params.topMargin = DimensUtil.dp2px(16f);
                mGridClassroomTwelve.addView(context3, params);
            }
        }


        if (mClassRoom.getValue7() != null) {
            mGridClassroomFourteen.setRowCount(30);
            mGridClassroomFourteen.setColumnCount(4);
            for (int i = 0; i < mClassRoom.getValue7().size(); i++) {
                TextView context4 = new TextView(this);
                context4.setTextColor(getResources().getColor(R.color.colorBlack));
                context4.setText(mClassRoom.getValue7().get(i));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                params.rightMargin = DimensUtil.dp2px(42f);
                params.width = DimensUtil.getScreenWidth() / 4;
                params.topMargin = DimensUtil.dp2px(16f);
                mGridClassroomFourteen.addView(context4, params);
            }
        }

        if (mClassRoom.getValue9() != null) {
            mGridClassroomSixteen.setRowCount(30);
            mGridClassroomSixteen.setColumnCount(4);
            for (int i = 0; i < mClassRoom.getValue9().size(); i++) {
                TextView context5 = new TextView(this);
                context5.setTextColor(getResources().getColor(R.color.colorBlack));
                context5.setText(mClassRoom.getValue9().get(i));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                params.rightMargin = DimensUtil.dp2px(42f);
                params.width = DimensUtil.getScreenWidth() / 4;
                params.topMargin = DimensUtil.dp2px(16f);
                mGridClassroomSixteen.addView(context5, params);
            }
        }

        if (mClassRoom.getValue11() != null) {
            mGridClassroomEighteen.setRowCount(30);
            mGridClassroomEighteen.setColumnCount(4);
            for (int i = 0; i < mClassRoom.getValue11().size(); i++) {
                TextView context6 = new TextView(this);
                context6.setTextColor(getResources().getColor(R.color.colorBlack));
                context6.setText(mClassRoom.getValue11().get(i));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                params.rightMargin = DimensUtil.dp2px(42f);
                params.width = DimensUtil.getScreenWidth() / 4;
                params.topMargin = DimensUtil.dp2px(16f);
                mGridClassroomEighteen.addView(context6, params);
            }
        }

        if (mClassRoom.getValue13() != null) {
            mGridClassroomTwenty.setRowCount(30);
            mGridClassroomTwenty.setColumnCount(4);
            for (int i = 0; i < mClassRoom.getValue13().size(); i++) {
                TextView context7 = new TextView(this);
                context7.setTextColor(getResources().getColor(R.color.colorBlack));
                context7.setText(mClassRoom.getValue13().get(i));
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
//                params.rightMargin = DimensUtil.dp2px(42f);
                params.width = DimensUtil.getScreenWidth() / 4;
                params.topMargin = DimensUtil.dp2px(16f);
                mGridClassroomTwenty.addView(context7, params);
            }
        }
    }


    /**
     * 转换星期格式为相应的查询参数
     *
     * @param str
     * @return
     */
    private String getDayValue(String str) {
        int index = str.indexOf("周");
        String s = str.substring(index + 1, index + 3);
        switch (s) {
            case "周一":
                s = "mon";
                break;
            case "周二":
                s = "tue";
                break;
            case "周三":
                s = "wed";
                break;
            case "周四":
                s = "thu";
                break;
            case "周五":
                s = "fri";
                break;
        }
        return s;
    }

    private String getWeek(String str) {
        int index = str.indexOf("周");
        String s = str.substring(1, index);
        return s;
    }

    private String getBuidingValue(String str) {
        int index = str.indexOf("号");
        String s = str.substring(index - 1, index);
        return s;
    }


    @Override
    public void onBackPressed() {
        StudyRoomCorrectView view = new StudyRoomCorrectView(StudyRoomDetailActivity.this);
        if (getWindow().getDecorView().equals(view)) {
            mStudyDetailLayout.removeView(view);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_studyroom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_correct) {
            StudyRoomCorrectView studyRoomCorrectView = new StudyRoomCorrectView(StudyRoomDetailActivity.this);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.view_show);
            studyRoomCorrectView.startAnimation(animation);
            mStudyDetailLayout.addView(studyRoomCorrectView);
        }
        return super.onOptionsItemSelected(item);
    }
}
