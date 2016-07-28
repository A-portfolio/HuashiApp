package net.muxi.huashiapp.common.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.CardData;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.data.PersonalBook;
import net.muxi.huashiapp.common.data.Scores;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.common.net.CampusFactory;
import net.muxi.huashiapp.common.util.Base64Util;
import net.muxi.huashiapp.common.util.DateUtil;
import net.muxi.huashiapp.common.util.Logger;
import net.muxi.huashiapp.common.util.NotifyUtil;
import net.muxi.huashiapp.common.util.PreferenceUtil;
import net.muxi.huashiapp.library.MineActivity;
import net.muxi.huashiapp.schedule.ScheduleActivity;
import net.muxi.huashiapp.score.ScoreActivity;

import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/5/16.
 */
public class AlarmReceiveer extends BroadcastReceiver {

    private User mUser;
    private User mLibUser;
    private PreferenceUtil sp;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: 16/5/29 enable after
        Logger.d("search score");
        mContext = context;
        mUser = new User();
        mLibUser = new User();
        sp = new PreferenceUtil();
        mUser.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
        mUser.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
        mLibUser.setSid(sp.getString(PreferenceUtil.LIBRARY_ID));
        mLibUser.setPassword(sp.getString(PreferenceUtil.LIBRARY_PWD));

        //判断对应的登录状态以及当前时间,还有用户是否设置提醒
        if (mUser.getSid() != "") {
            if (intent.getStringExtra(AppConstants.ALARMTIME).equals("thirdTime")) {
                if (sp.getBoolean(App.getContext().getString(R.string.pre_schedule))) {
                    checkCourses();
                }
            }
            if (intent.getStringExtra(AppConstants.ALARMTIME).equals("secondTime")) {
                if (sp.getBoolean(App.getContext().getString(R.string.pre_score))) {
                    checkScores();
                }
            }
            if (intent.getStringExtra(AppConstants.ALARMTIME).equals("firstTime")) {
                if (sp.getBoolean(App.getContext().getString(R.string.pre_score))) {
                    checkScores();
                }
                if (sp.getBoolean(App.getContext().getString(R.string.pre_card))) {
                    checkCard();
                }
                if (mLibUser.getSid() != "") {
                    if (sp.getBoolean(App.getContext().getString(R.string.pre_library))) {
                        checkLib();
                    }
                }
            }
        }

    }

    private void checkCard() {
        CampusFactory.getRetrofitService().getCardBalance(mUser.getSid(), "10", "0", "1")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CardData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<CardData> cardData) {
                        // TODO: 16/7/4  待添加检测是否已经充值,不然每天都会提醒
                        if (Integer.valueOf(cardData.get(0).getOutMoney()) < 10) {
                            if (sp.getBoolean(PreferenceUtil.IS_STOP_REMIND_CARD,false)) {
//                                NotifyUtil.show(mContext,);
                            }
                        }
                    }
                });
    }

    private void checkLib() {
        CampusFactory.getRetrofitService().getPersonalBook(Base64Util.createBaseStr(mUser))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<PersonalBook>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(PersonalBook personalBook) {
                        if (Integer.valueOf(personalBook.getTime()) <= 3) {
                            NotifyUtil.show(mContext, MineActivity.class,
                                    mContext.getString(R.string.notify_title_lib),
                                    mContext.getString(R.string.notify_content_lib));
                        }
                    }
                });
    }

    private void checkCourses() {
        HuaShiDao dao = new HuaShiDao();
        String startDate = sp.getString(PreferenceUtil.FIRST_WEEK_DATE);
        int curWeek = (int) DateUtil.getDistanceWeek(startDate,DateUtil.toDateInYear(new Date(System.currentTimeMillis())));
        List<Course> courses = dao.loadCourse((curWeek + 1) + "");
        Course course;
        int today = DateUtil.getDayInWeek(new Date(System.currentTimeMillis()));
        for (int i = 0,j = courses.size();i < j;i ++){
            course = courses.get(i);
            if (course.getRemind().equals("true") && (AppConstants.WEEKDAYS[today + 1]).equals(course.getDay())){
                NotifyUtil.show(mContext, ScheduleActivity.class,mContext.getString(R.string.notify_title_course),
                        mContext.getString(R.string.notify_content_course));
                break;
            }
        }
    }

    private void checkScores() {
        CampusFactory.getRetrofitService().getScores(Base64Util.createBaseStr(mUser), "2015", "12")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Scores>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Scores> scoresList) {
                        if (scoresList.size() > sp.getInt(PreferenceUtil.SCORES_NUM)) {
                            NotifyUtil.show(mContext, ScoreActivity.class, mContext.getString(R.string.notify_title_score),
                                    mContext.getString(R.string.notify_content_score));
                        }
                    }
                });
    }
}
