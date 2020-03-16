package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.appbase.BaseAppActivity;
import com.muxistudio.appcommon.appbase.BaseAppFragment;
import com.muxistudio.appcommon.data.Course;
import com.muxistudio.appcommon.data.CourseList;
import com.muxistudio.appcommon.db.HuaShiDao;
import com.muxistudio.appcommon.event.AddCourseEvent;
import com.muxistudio.appcommon.event.AuditCourseEvent;
import com.muxistudio.appcommon.event.CurWeekChangeEvent;
import com.muxistudio.appcommon.event.DeleteCourseOkEvent;
import com.muxistudio.appcommon.event.RefreshFinishEvent;
import com.muxistudio.appcommon.event.RefreshTableEvent;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.appcommon.presenter.LoginPresenter;
import com.muxistudio.appcommon.user.UserAccountManager;
import com.muxistudio.common.util.Logger;
import com.umeng.analytics.MobclickAgent;

import java.util.Locale;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.provider.ScheduleWidgetProvider;
import net.muxi.huashiapp.utils.TimeTableUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.HttpException;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 17/1/25.
 */

public class TimetableFragment extends BaseAppFragment {

    /**
     * 本学期所有的课程
     */
    private List<Course> mCourses;
    //false表示初始状态
    private boolean selectedIvStatus = false;
    private int selectedWeek;
    private int curWeek;
    private HuaShiDao dao;
    private Context mContext;
    private boolean handlingRefresh = false;
    private TimeTable mTimetable;
    private View mShadeView;
    private WeekSelectedView mWeekSelectedView;
    private RelativeLayout mToolLayout;
    private TextView mTvSelectWeek;
    private ImageView mIvSelectWeek;
    private TextView mTvCurrentWeek;
    private ImageButton mIvMenu;
    private TableMenuView mTableMenuView;

    public static TimetableFragment newInstance() {
        Bundle args = new Bundle();
        TimetableFragment fragment = new TimetableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        mTimetable = view.findViewById(R.id.timetable);
        mShadeView = view.findViewById(R.id.shade_view);
        mWeekSelectedView = view.findViewById(R.id.week_selected_view);
        mToolLayout = view.findViewById(R.id.tool_layout);
        mTvSelectWeek = view.findViewById(R.id.tv_select_week);
        mIvSelectWeek = view.findViewById(R.id.iv_select_week);
        mTvCurrentWeek = view.findViewById(R.id.tv_current_week);
        mIvMenu = view.findViewById(R.id.iv_menu);
        mTableMenuView = view.findViewById(R.id.table_menu_view);
        mTvSelectWeek.setOnClickListener(v -> onClick(v));
        mIvSelectWeek.setOnClickListener(v -> onClick(v));
        getActivity().getWindow().getDecorView().setBackgroundColor
                (Color.argb(255, 250, 250, 250));
        dao = new HuaShiDao();
        mContext = getActivity();
        initData();
        initView();
        initListener();

        if (mCourses.size()==0&&(!TextUtils.isEmpty(UserAccountManager.getInstance().getInfoUser().sid))) {
            handlingRefresh=true;
            mTimetable.startRefreshView();
            loadTable();
        }
        return view;
    }

    public void startRefreshView(){
        handlingRefresh=true;
        mTimetable.startRefreshView();
        loadTable();
    }
    @Override
    public void onHiddenChanged(boolean hidden){
        if (!hidden){
            setCurweek(curWeek);
            setSelectedWeek(selectedWeek);
            int nowWeek=TimeTableUtil.getCurWeek();
            if (nowWeek!=curWeek){
                curWeek=nowWeek;
                setCurweek(curWeek);
                setSelectedWeek(selectedWeek);
                RxBus.getDefault().send(new CurWeekChangeEvent());
            }
        }
    }
    private void initData() {
        curWeek = TimeTableUtil.getCurWeek();
        selectedWeek = curWeek;
        mCourses = dao.loadAllCourses();
    }

    private void initView() {
        if (mCourses.size() == 0) {
        } else {
            renderCourseView(mCourses);
        }
        setCurweek(curWeek);
        setSelectedWeek(selectedWeek);

//        if (PreferenceUtil.getBoolean(PreferenceUtil.IS_FIRST_ENTER_TABLE, true)) {
//            IndicatedView indicatedView = new IndicatedView(getContext());
//            indicatedView.setTipViewText("设置当前周也可以点这里噢");
//            TipViewUtil.addToContent(getContext(), indicatedView, DIRECTION_DOWN,
//                    DimensUtil.getScreenWidth() - DimensUtil.dp2px(38), DimensUtil.dp2px(38));
//        }删除这里的设置周数功能

    }

    private void initListener() {
        mWeekSelectedView.setOnWeekSelectedListener(week -> {
            rotateSelectView();
            selectedWeek = week;
            renderCourseView(mCourses);
            setSelectedWeek(week);
            mShadeView.setVisibility(View.GONE);
        });
        mIvMenu.setOnClickListener(v -> {
            if (selectedIvStatus) {
                mWeekSelectedView.slideUp();
                rotateSelectView();
            }
            mTableMenuView.show();
          //  mTableMenuView.setCurweek(curWeek);
        });
        mTimetable.setOnCourseClickListener(v -> {
            String id = ((CourseView) v).getCourseId();
            DetailCoursesDialog dialog = DetailCoursesDialog.newInstance(getSameTimeCourses(id),
                    selectedWeek);
            dialog.show(getChildFragmentManager(), "detail_courses");
        });
        mShadeView.setOnClickListener(v -> {
            mTvSelectWeek.performClick();
            if (selectedIvStatus) {
                mWeekSelectedView.slideUp();
                mShadeView.setVisibility(View.GONE);
                rotateSelectView();
            }

        });


        Subscription subscription1
                = RxBus.getDefault().toObservable(AddCourseEvent.class)
                .subscribe(addCourseEvent -> {
                    mCourses = dao.loadAllCourses();
                    renderCourseView(mCourses);
                    Intent intent = new Intent(mContext, ScheduleWidgetProvider.class);
                    intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                    mContext.sendBroadcast(intent);
                }, Throwable::printStackTrace);
        ((BaseAppActivity) getContext()).addSubscription(subscription1);
        Subscription subscription2 = RxBus.getDefault().toObservable(DeleteCourseOkEvent.class)
                .subscribe(deleteCourseOkEvent -> {
                    mCourses = dao.loadAllCourses();
                    renderCourseView(mCourses);
                    Intent intent = new Intent(mContext, ScheduleWidgetProvider.class);
                    intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                    mContext.sendBroadcast(intent);
                }, Throwable::printStackTrace);
        ((BaseAppActivity) getContext()).addSubscription(subscription2);
        Subscription subscription3 = RxBus.getDefault().toObservable(RefreshTableEvent.class)
                .subscribe(refreshTableEvent -> {
                    Logger.d("refresh course");
                    mCourses = dao.loadAllCourses();
                    renderCourseView(mCourses);
                    Intent intent = new Intent(mContext, ScheduleWidgetProvider.class);
                    intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                    mContext.sendBroadcast(intent);
                }, Throwable::printStackTrace);
        ((BaseAppActivity) getContext()).addSubscription(subscription3);
        Subscription subscription4 = RxBus.getDefault().toObservable(CurWeekChangeEvent.class)
                .subscribe(curWeekChangeEvent -> {
                  //在回调之前更新过的课程都会储存在SharedPerference中
                    int curWeek = TimeTableUtil.getCurWeek();
                    selectedWeek = curWeek;
                    setCurweek(curWeek);
                    setSelectedWeek(curWeek);
                    renderCourseView(mCourses);
                }, Throwable::printStackTrace);
        ((BaseAppActivity) getContext()).addSubscription(subscription4);
        Subscription subscription5  = RxBus.getDefault().toObservable(AuditCourseEvent.class)
                .subscribe(auditCourseEvent -> {
                    if (auditCourseEvent.isRefresh) {
                        mCourses = dao.loadAllCourses();
                        renderCourseView(mCourses);
                        loadTable();
                    }
                });
        ((BaseAppActivity) getContext()).addSubscription(subscription5);

        mTimetable.setOnRefreshListener(() -> {
            handlingRefresh = true;
            loadTable();
        });
    }

    public List<Course> getSameTimeCourses(String id) {
        List<Course> courseList = new ArrayList<>();
        int start = 1;
        int duration = 2;
        String weekday = "星期一";
        for (Course course : mCourses) {
            if (course.id.equals(id)) {
                start = course.start;
                duration = course.during;
                weekday = course.day;
                break;
            }
        }
        for (Course course : mCourses) {
            if (course.start == start && course.during == duration && course.day.equals(weekday)) {
                courseList.add(course);
            }
        }
        return courseList;
    }

    public void retryLoadTable() {
        new LoginPresenter()
                .login(UserAccountManager.getInstance().getInfoUser())
                .flatMap(booleans -> CampusFactory.getRetrofitService().
                        getTimeTable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(courses -> {
                    updateDB(courses.getData().getTable());
                    mCourses = courses.getData().getTable();
                    renderCourseView(courses.getData().getTable());
                    if (handlingRefresh) {
                        handlingRefresh = false;
                        RxBus.getDefault().send(new RefreshFinishEvent(courses.getData().getTable().size() != 0));
                    }
                });
    }

    public void loadTable() {
        getTable();
    }

    public void deferLoadTable(){
        Observable.timer(500,TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<Long, Observable<CourseList>>() {
                    @Override
                    public Observable<CourseList> call(Long aLong) {
                        return CampusFactory.getRetrofitService().getTimeTable();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(courseList -> {
                    updateDB(courseList.getData().getTable());
                    mCourses = courseList.getData().getTable();
                    renderCourseView(courseList.getData().getTable());
                    if (handlingRefresh) {
                        handlingRefresh = false;
                        RxBus.getDefault().send(new RefreshFinishEvent(courseList.getData().getTable().size() != 0));
                    }
                },Throwable::printStackTrace);

    }
    private void getTable() {

        CampusFactory.getRetrofitService().getTimeTable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(courseList -> {
                    updateDB(courseList.getData().getTable());
                    mCourses = courseList.getData().getTable();
                    renderCourseView(courseList.getData().getTable());
                    if (handlingRefresh) {
                        handlingRefresh = false;
                        RxBus.getDefault().send(new RefreshFinishEvent(courseList.getData().getTable().size() != 0));
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (handlingRefresh) {
                        handlingRefresh = false;
                        //没有联网会抛出这个异常
                        RxBus.getDefault().send(new RefreshFinishEvent(false
                                , RefreshFinishEvent.SELF_DEFINE_CODE));
                     //   retryLoadTable();

                    }
                    if (throwable instanceof HttpException) {
                        int code = ((HttpException) throwable).code();
                        if (code == 401) {
                            RxBus.getDefault().send(new RefreshFinishEvent(false
                                    , code));
                        }
                    }
                });
    }

    /**
     * 渲染显示的课程： 更改日期，显示课程
     */
    private void renderCourseView(List<Course> courseList) {
        mTimetable.getWeekLayout().setWeekDate(selectedWeek - curWeek);
        mTimetable.getTableContent().updateCourses(courseList, selectedWeek);
    }

    private void updateDB(List<Course> courseList) {
        dao.deleteAllCourse();
        for (Course course : courseList) {
            dao.insertCourse(course);
        }
    }

    //设置选择的view位置颠倒
    private void rotateSelectView() {
        if (!selectedIvStatus) {
            mIvSelectWeek.setRotation(180);
        } else {
            mIvSelectWeek.setRotation(0);
        }
        selectedIvStatus = !selectedIvStatus;
    }

    /**
     * @param selectingWeek 从1开始计数
     */
    //第n周
    public void setSelectedWeek(int selectingWeek) {
        mTvSelectWeek.setText(String.format(Locale.CHINESE,"第%d周", selectingWeek));
    }

    /**
     * @param week 从1开始计数
     */
    public void setCurweek(int week) {
        mTvCurrentWeek.setText("当前周设置为" + week);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_select_week || id == R.id.iv_select_week) {
            if (!selectedIvStatus) {
                mWeekSelectedView.slideDown();
                MobclickAgent.onEvent(getActivity(), "week_select");
                if (selectedWeek>0&&selectedWeek<=21) {
                    mWeekSelectedView.setSelectedWeek(selectedWeek);
                }
                mShadeView.setVisibility(View.VISIBLE);
            } else {
                mWeekSelectedView.slideUp();

                mShadeView.setVisibility(View.GONE);
            }
            rotateSelectView();
        }
    }


}
