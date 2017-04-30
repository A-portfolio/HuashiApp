package net.muxi.huashiapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.Constants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.AttentionBook;
import net.muxi.huashiapp.common.data.CardData;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.data.BorrowedBook;
import net.muxi.huashiapp.common.data.Scores;
import net.muxi.huashiapp.common.data.User;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.ui.card.CardActivity;
import net.muxi.huashiapp.ui.main.MainActivity;
import net.muxi.huashiapp.ui.score.ScoreActivity;
import net.muxi.huashiapp.util.Base64Util;
import net.muxi.huashiapp.util.DateUtil;
import net.muxi.huashiapp.util.Logger;
import net.muxi.huashiapp.util.NotifyUtil;
import net.muxi.huashiapp.util.PreferenceUtil;
import net.muxi.huashiapp.util.TimeTableUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ybao on 16/5/16.
 * todo test alarmreceiver
 */
public class AlarmReceiver extends BroadcastReceiver {

    private static final String TAG = "alarm";

    private User mUser;
    private User mLibUser;
    private PreferenceUtil sp;
    private Context mContext;
    //设置学生卡提醒的额度
    private static final int CARD_LEAVE_MONEY = 20;
    private int mCurYear;
    private int mCurTerm;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        mUser = new User();
        mLibUser = new User();
        sp = new PreferenceUtil();
        mUser.setSid(sp.getString(PreferenceUtil.STUDENT_ID));
        mUser.setPassword(sp.getString(PreferenceUtil.STUDENT_PWD));
        mLibUser.setSid(sp.getString(PreferenceUtil.LIBRARY_ID));
        mLibUser.setPassword(sp.getString(PreferenceUtil.LIBRARY_PWD));

        Logger.d(mUser.getSid());
        if (TextUtils.isEmpty(mUser.sid)) {
            return;
        }

        //判断对应的登录状态以及当前时间,还有用户是否设置提醒
        if (intent.getIntExtra(Constants.ALARMTIME, 2) == 2) {
            Log.d(TAG,sp.getBoolean(App.getContext().getString(R.string.pre_schedule),true) + "");
            if (sp.getBoolean(App.getContext().getString(R.string.pre_schedule), true)) {
                checkCourses();
                Log.d(TAG, "check course");
            }
            if (sp.getBoolean(App.getContext().getString(R.string.pre_score), true)) {
                checkScores();
                Log.d(TAG, "check score");
            }
        }
        if (intent.getIntExtra(Constants.ALARMTIME, 0) == 1) {
            if (sp.getBoolean(App.getContext().getString(R.string.pre_score), true)) {
                checkScores();
                Log.d(TAG, "check score");
            }
        }
        if (intent.getIntExtra(Constants.ALARMTIME, 0) == 0) {
            if (sp.getBoolean(App.getContext().getString(R.string.pre_card), true)) {
                checkCard();
                Log.d(TAG, "check card");
            }
            if (sp.getBoolean(App.getContext().getString(R.string.pre_score), true)) {
                checkScores();
            }
            if (mLibUser.getSid() != "") {
                if (sp.getBoolean(App.getContext().getString(R.string.pre_library), true)) {
                    checkLib();
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
                        try {
                            if (Integer.valueOf(cardDatas.get(0).getOutMoney())
                                    < CARD_LEAVE_MONEY) {
                                NotifyUtil.show(mContext, CardActivity.class,
                                        mContext.getResources().getString(
                                                R.string.notify_title_card),
                                        mContext.getResources().getString(
                                                R.string.notify_content_card));
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void checkLib() {
        CampusFactory.getRetrofitService().getPersonalBook(Base64Util.createBaseStr(mUser))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<List<BorrowedBook>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<BorrowedBook> borrowedBooks) {
                        List<String> books = new ArrayList<>();
                        boolean isRemind = false;
                        for (int i = 0, j = borrowedBooks.size(); i < j; i++) {
                            if (Integer.valueOf(borrowedBooks.get(i).time) < 4) {
                                isRemind = true;
                                books.add(borrowedBooks.get(i).book);
                            }
                        }
                        if (isRemind) {
                            String content = String.format(
                                    App.sContext.getString(R.string.notify_content_lib),
                                    connectBooks(books));
                            NotifyUtil.show(mContext, MainActivity.class,
                                    mContext.getString(R.string.notify_title_course),
                                    content, "lib_mine");
                        }
                    }
                });
        CampusFactory.getRetrofitService().getAttentionBooks(Base64Util.createBaseStr(mUser))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listResponse -> {
                    List<String> books = new ArrayList<String>();
                    boolean isRemind = false;
                    for (AttentionBook book : listResponse.body()) {
                        if (book.avbl.equals("y")) {
                            isRemind = true;
                            books.add(book.book);
                        }
                    }
                    if (isRemind) {
                        String content = String.format(
                                App.sContext.getString(R.string.notify_content_attention),
                                connectBooks(books));
                        NotifyUtil.show(mContext, MainActivity.class,
                                mContext.getString(R.string.notify_title_course),
                                content, "lib_mine");
                    }
                }, throwable -> throwable.printStackTrace());
    }

    private void checkCourses() {

        int day = DateUtil.getDayInWeek(new Date(System.currentTimeMillis()));
        String defalutDate = DateUtil.getTheDateInYear(new Date(System.currentTimeMillis()),
                1 - day);
        String startDate = sp.getString(PreferenceUtil.FIRST_WEEK_DATE, defalutDate);
        int curWeek = (int) DateUtil.getDistanceWeek(startDate,
                DateUtil.toDateInYear(new Date(System.currentTimeMillis()))) + 1;
        Logger.d(curWeek + "");
        //如果今天是周日则另做判断
        if (day == 7) {
            day = 1;
            curWeek++;
        } else {
            day ++;
        }

        HuaShiDao dao = new HuaShiDao();
        List<Course> allCourses = dao.loadAllCourses();
        List<String> courses = new ArrayList<>();

        for (int i = 0;i < allCourses.size();i ++){
            Logger.d(allCourses.get(i).id);
            if (Integer.parseInt(allCourses.get(i).id) < 1000
                    && allCourses.get(i).day.equals(Constants.WEEKDAYS_XQ[day - 1])
                    && !allCourses.get(i).getCourse().equals(Constants.INIT_COURSE)
                    && TimeTableUtil.isThisWeek(curWeek,allCourses.get(i).weeks)){
                courses.add(allCourses.get(i).course);
            }
        }
        Logger.d(courses.size() + "");

        if (courses.size() > 0) {
            String content = String.format(mContext.getString(R.string.notify_content_course),
                    connectStrings(courses));
            NotifyUtil.show(mContext, MainActivity.class,
                    mContext.getString(R.string.notify_title_course),
                    content, "timetable");
        }
    }

    private void checkScores() {
        judgeYearAndTerm();
        CampusFactory.getRetrofitService().getScores( mCurYear + "",
                mCurTerm + "")
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
                        Logger.d(scoresList.size() + "  " + sp.getInt(PreferenceUtil.SCORES_NUM));
                        if (scoresList.size() != sp.getInt(PreferenceUtil.SCORES_NUM)
                                && scoresList.size() != 0) {
                            sp.saveInt(PreferenceUtil.SCORES_NUM, scoresList.size());
                            NotifyUtil.show(mContext, ScoreActivity.class,
                                    mContext.getString(R.string.notify_title_score),
                                    mContext.getString(R.string.notify_content_score));
                        }
                        if (scoresList.size() == 0) {
                            sp.saveInt(PreferenceUtil.SCORES_NUM, scoresList.size());
                        }
                    }
                });
    }

    private void judgeYearAndTerm() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        if (month < 9 && month > 3) {
            mCurTerm = 12;
        } else {
            mCurTerm = 3;
        }
        if (month < 9) {
            mCurYear = year - 1;
        } else {
            mCurYear = year;
        }
    }

    public String connectStrings(List<String> stringList) {
        String s;
        s = TextUtils.join(",", stringList);
        return s;
    }

    public String connectBooks(List<String> books) {
        String s = "";
        for (int i = 0; i < books.size(); i++) {
            s = s + "《" + books.get(i) + "》";
            if (i < books.size() - 1) {
                s += "，";
            }
        }
        return s;
    }

}
