package net.muxi.huashiapp.common.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.card.CardActivity;
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
import net.muxi.huashiapp.common.util.TimeTableUtil;
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
public class AlarmReceiver extends BroadcastReceiver {

    private User mUser;
    private User mLibUser;
    private PreferenceUtil sp;
    private Context mContext;
    //设置学生卡提醒的额度
    private static final int CARD_LEAVE_MONEY = 20;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: 16/5/29 enable after
        mContext = context;
        mUser = new User();
        mLibUser = new User();
        sp = new PreferenceUtil();
        mUser.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
        mUser.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
        mLibUser.setSid(sp.getString(PreferenceUtil.LIBRARY_ID));
        mLibUser.setPassword(sp.getString(PreferenceUtil.LIBRARY_PWD));

        Logger.d(mUser.getSid());
        //判断对应的登录状态以及当前时间,还有用户是否设置提醒
        if (!mUser.getSid().equals("")) {
            Logger.d("check sid");
            if (intent.getIntExtra(AppConstants.ALARMTIME, 0) == 2) {
                if (sp.getBoolean(App.getContext().getString(R.string.pre_schedule),true)) {
                    checkCourses();
                    Logger.d("check course");
                }
            }
            if (intent.getIntExtra(AppConstants.ALARMTIME, 0) == 1) {
                if (sp.getBoolean(App.getContext().getString(R.string.pre_score),true)) {
                    checkScores();
                    Logger.d("check course");
                }
            }
            if (intent.getIntExtra(AppConstants.ALARMTIME, 0) == 0) {
                if (sp.getBoolean(App.getContext().getString(R.string.pre_card),true)) {
                    checkCard();
                    Logger.d("check course");
                }
                if (sp.getBoolean(App.getContext().getString(R.string.pre_score),true)) {
                    checkScores();
                }
                if (mLibUser.getSid() != "") {
                    if (sp.getBoolean(App.getContext().getString(R.string.pre_library),true)) {
                        checkLib();
                    }
                }
            }
        }


    }

    private void checkCard() {
        CampusFactory.getRetrofitService().getCardBalance(mUser.getSid(), "60", "0", "10")
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
                    public void onNext(List<CardData> cardDatas) {
                        if (Integer.valueOf(cardDatas.get(0).getOutMoney()) < CARD_LEAVE_MONEY) {
                            NotifyUtil.show(mContext, CardActivity.class,
                                    mContext.getResources().getString(R.string.notify_title_card),
                                    mContext.getResources().getString(R.string.notify_content_card));
                        }
                    }
                });
    }

    private void checkLib() {
        CampusFactory.getRetrofitService().getPersonalBook(Base64Util.createBaseStr(mUser))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<PersonalBook>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<PersonalBook> personalBooks) {
                        boolean isRemind = false;
                        for (int i = 0, j = personalBooks.size(); i < j; i++) {
                            if (Integer.valueOf(personalBooks.get(i).getTime()) < 4) {
                                isRemind = true;
                                break;
                            }
                        }
                        if (isRemind) {
                            NotifyUtil.show(mContext, MineActivity.class,
                                    mContext.getResources().getString(R.string.notify_title_lib),
                                    mContext.getResources().getString(R.string.notify_content_lib));
                        }
                    }
                });
    }

    private void checkCourses() {
        HuaShiDao dao = new HuaShiDao();
        List<Course> courses = dao.loadCustomCourse();
        String startDate = sp.getString(PreferenceUtil.FIRST_WEEK_DATE);
        int curWeek = (int) DateUtil.getDistanceWeek(startDate, DateUtil.toDateInYear(new Date(System.currentTimeMillis())));
        int today = DateUtil.getDayInWeek(new Date(System.currentTimeMillis()));
        for (int i = 0, j = courses.size(); i < j; i++) {
            Course course = courses.get(i);
            if (course.getRemind().equals("true") &&
                    (AppConstants.WEEKDAYS[today]).equals(course.getDay()) &&
                    TimeTableUtil.isThisWeek(curWeek, course.getWeeks())) {
                NotifyUtil.show(mContext, ScheduleActivity.class,
                        mContext.getString(R.string.notify_title_course),
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
                            NotifyUtil.show(mContext, ScoreActivity.class,
                                    mContext.getString(R.string.notify_title_score),
                                    mContext.getString(R.string.notify_content_score));
                        }
                    }
                });
    }
}
