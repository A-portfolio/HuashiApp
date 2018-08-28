package net.muxi.huashiapp.ui.studyroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxistudio.appcommon.appbase.ToolbarActivity;
import com.muxistudio.appcommon.data.ClassRoom;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.common.util.DimensUtil;
import com.muxistudio.common.util.Logger;
import com.muxistudio.multistatusview.MultiStatusView;

import net.muxi.huashiapp.R;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by december on 17/2/1.
 */

public class StudyRoomDetailActivity extends ToolbarActivity {

    private ClassRoom mClassRoom;
    private RelativeLayout mStudyDetailLayout;
    private MultiStatusView mMultiStatusView;
    private TextView mTitleStudyRoomEight;
    private GridLayout mGridClassroomEight;
    private TextView mTitleStudyRoomTen;
    private GridLayout mGridClassroomTen;
    private TextView mTitleStudyRoomTwelve;
    private GridLayout mGridClassroomTwelve;
    private TextView mTitleStudyRoomFourteen;
    private GridLayout mGridClassroomFourteen;
    private TextView mTitleStudyRoomSixteen;
    private GridLayout mGridClassroomSixteen;
    private TextView mTitleStudyRoomEighteen;
    private GridLayout mGridClassroomEighteen;
    private TextView mTitleStudyRoomTwenty;
    private GridLayout mGridClassroomTwenty;

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
        initView();
        setTitle("空闲教室列表");

        mQuery = getIntent().getStringExtra("query");
        Logger.d(mQuery + " ");
        mMultiStatusView.setOnRetryListener(v -> loadData());
        loadData();
    }

    private void loadData() {
        showLoading("正在请求空闲教室数据ing~");
        CampusFactory.getRetrofitService()
                .getClassRoom(getWeek(mQuery), getDayValue(mQuery)
                        , getBuidingValue(mQuery))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(classRoom -> {
                    mMultiStatusView.showContent();
                    mClassRoom = classRoom;
                    setNull();
                    setData();
                }, throwable -> {
                    throwable.printStackTrace();
                    mMultiStatusView.showNetError();
                    hideLoading();
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
                params.width = 2 * mGridClassroomEight.getWidth() / 7;
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
                params.width = 2 * mGridClassroomEight.getWidth() / 7;
//                params.rightMargin = mGridClassroomEight.getWidth() / 5;
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
                params.width = 2 * mGridClassroomEight.getWidth() / 7;
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
                params.width = 2 * mGridClassroomEight.getWidth() / 7;
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
//                params.width = DimensUtil.dp2px(42f);
                params.width = 2 * mGridClassroomEight.getWidth() / 7;
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
//                params.width = DimensUtil.dp2px(42f);
                params.width = 2 * mGridClassroomEight.getWidth() / 7;
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
//                params.width = DimensUtil.dp2px(42f);
                params.width = 2 * mGridClassroomEight.getWidth() / 7;
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

    private void initView() {
        mStudyDetailLayout = findViewById(R.id.study_detail_layout);
        mMultiStatusView = findViewById(R.id.multi_status_view);
        mTitleStudyRoomEight = findViewById(R.id.title_study_room_eight);
        mGridClassroomEight = findViewById(R.id.grid_classroom_eight);
        mTitleStudyRoomTen = findViewById(R.id.title_study_room_ten);
        mGridClassroomTen = findViewById(R.id.grid_classroom_ten);
        mTitleStudyRoomTwelve = findViewById(R.id.title_study_room_twelve);
        mGridClassroomTwelve = findViewById(R.id.grid_classroom_twelve);
        mTitleStudyRoomFourteen = findViewById(R.id.title_study_room_fourteen);
        mGridClassroomFourteen = findViewById(R.id.grid_classroom_fourteen);
        mTitleStudyRoomSixteen = findViewById(R.id.title_study_room_sixteen);
        mGridClassroomSixteen = findViewById(R.id.grid_classroom_sixteen);
        mTitleStudyRoomEighteen = findViewById(R.id.title_study_room_eighteen);
        mGridClassroomEighteen = findViewById(R.id.grid_classroom_eighteen);
        mTitleStudyRoomTwenty = findViewById(R.id.title_study_room_twenty);
        mGridClassroomTwenty = findViewById(R.id.grid_classroom_twenty);
    }
}
