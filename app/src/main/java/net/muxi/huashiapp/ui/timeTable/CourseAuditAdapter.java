package net.muxi.huashiapp.ui.timeTable;

//import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.muxistudio.appcommon.RxBus;
import com.muxistudio.appcommon.data.AuditCourse;
import com.muxistudio.appcommon.data.Course;
import com.muxistudio.appcommon.data.CourseAdded;
import com.muxistudio.appcommon.data.CourseId;
import com.muxistudio.appcommon.db.HuaShiDao;
import com.muxistudio.appcommon.event.AuditCourseEvent;
import com.muxistudio.appcommon.net.CampusFactory;
import com.muxistudio.common.util.ToastUtil;

import net.muxi.huashiapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kolibreath on 18-2-2.
 */

public class CourseAuditAdapter extends RecyclerView.Adapter<CourseAuditAdapter.AuditViewHolder> {

    private List<Integer> positions = new ArrayList<>();
    private List<Course> mCourses;
    private HuaShiDao dao;
    private AuditCourse auditCourses;


    public CourseAuditAdapter(AuditCourse course) {
        this.auditCourses = course;
        dao = new HuaShiDao();
        mCourses = dao.loadAllCourses();
    }

    @Override
    public AuditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_audit, parent, false);
        return new AuditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AuditViewHolder holder, int position) {
        //这个course 是有id的
        AuditCourse.ResBean auditCourse = auditCourses.getRes().get(position);
        String courseNameTeacher = auditCourse.getName() + "(" + auditCourse.getTeacher() + ")";
        holder.mTvCourseNameTeacher.setText(courseNameTeacher);
        List<AuditCourse.ResBean.WwBean> wwBeanList = auditCourses.getRes().get(position).getWw();
        String where = "", whenPeriod = "", whenWeek = "";
        for (AuditCourse.ResBean.WwBean wwBean : wwBeanList) {
            if (!wwBean.getWhere().equals(where)) {
                where += getProperSite(wwBean.getWhere()) + "\n";
            }
            String p[] = AuditCourse.getCourseTime(wwBean.getWhen());
            whenPeriod += p[0] + "\n";
            whenWeek += p[1] + "\n";
        }
        String kind = auditCourse.getKind();
        if (kind.equals("专业课")) {
            holder.mIvCourseMarker.setImageResource(R.drawable.audit_professional);
        } else if (kind.equals("公共课")) {
            holder.mIvCourseMarker.setImageResource(R.drawable.audit_public);
        } else if (kind.equals("通核课")) {
            holder.mIvCourseMarker.setImageResource(R.drawable.audit_core);
        } else if (kind.equals("通选课")) {
            holder.mIvCourseMarker.setImageResource(R.drawable.audit_elective);
        }
        holder.mTvCourseWeek.setText(whenWeek);
        holder.mTvCoursePeriod.setText(whenPeriod);
        holder.mTvCourseSite.setText(where);
        holder.mBtnChooseCourse.setOnClickListener(v -> {
            if (mCourses.size() == 0 || mCourses == null) {
                ToastUtil.showShort("课程表为空,请先添加课程哟~");
                return;
            }
            if (!positions.contains(position)) {
                positions.add(position);
                String p[] = holder.mTvCoursePeriod.getText().toString().split("\n");
                //如果是两门课程的话都要不冲突才可以
                // 关于两门课程的解释 同一周的不同时间的同一门课程 教务处作为两门课程处理
                //只用有一门课的情况:
                // FIXME: 19-8-31 
                String formerWeeks[] = getWeek(holder.mTvCourseWeek.getText().toString());
                if (p.length == 1) {
                    if (isConflict(p[0], formerWeeks)) {
                        ToastUtil.showShort("课程冲突");
                        holder.mBtnChooseCourse.setText("添加");
                        positions.remove((Integer) (position));
                        //上传事件
                        return;
                    } else {
                        addCourse(holder.mTvCoursePeriod.getText().toString(), auditCourse, holder);
                        return;
                    }
                }
                //如果是两门课
                boolean bothConflict = false;
                for (int i = 0; i < p.length; i++) {
                    if (isConflict(p[i], formerWeeks)) {
                        //只要一门冲突就都冲突
                        bothConflict = true;
                        ToastUtil.showShort("课程冲突");
                        holder.mBtnChooseCourse.setText("添加");
                        positions.remove((Integer) (position));
                        break;
                    }
                }
                if (!bothConflict) {
                    addCourse(holder.mTvCoursePeriod.getText().toString(), auditCourse, holder);
                }
            } else {
                //还需要删除这门课
                holder.mBtnChooseCourse.setText("添加");
                positions.remove((Integer) (position));
            }
        });
    }

    //转换格式1-17周 -- 1,2,3,4,5,6,7 String数组
    //有些课程如果两周的话格式是: 1-17周\n1-17周
    private String[] getWeek(String string) {
        String stringCopy = string.split("\n")[0];
        String array[]=null;
        if (string.contains("双")||string.contains("单")) {
            String num[] = stringCopy.substring(0, stringCopy.indexOf("周")).split("-");
            //2,4,6,8,10,12,14,16,18  9
            int start = Integer.parseInt(num[0]);
            int end = Integer.parseInt(num[1]);
            array = new String[(end-start+ 2) / 2];
            int index = 0;
            for (int i = start; i <= end; i += 2) {
                array[index++] = i + "";
            }
        }
        else  {
            String pieces[] = stringCopy.substring(0, stringCopy.length() - 1).split("-");
            String stater = pieces[0], end = pieces[1];
            // FIXME: 19-8-31
             array = new String[Integer.parseInt(end) - Integer.parseInt(stater) + 1];
            for (int i = Integer.parseInt(stater), index = 0; i <= Integer.parseInt(end); i++, index++) {
                array[index] = i + "";
            }
        }
        return array;
    }

    //学校返回的数据格式会在教学楼后面加点 比如0910.0
    private String getProperSite(String site) {
        if (site.contains(".")) {
            int index = site.indexOf(".");
            return site.substring(0, index);
        } else {
            return site;
        }
    }

    //设置不同课程的提示
    //如果有两个时间的话需要上传两次时间和地点
    private void addCourse(String period, AuditCourse.ResBean auditCourse, AuditViewHolder holder) {
        if (isTwoClassWeek(period)) {
            String peroids[] = period.split("\n");
            //week 如果没有修改的格式是 1-17周"\n"1-17周 这两个是一样的 所以在下面的week参数中选取一样的
            String week = holder.mTvCourseWeek.getText().toString().split("\n")[0];
            List<AuditCourse.ResBean> list = createRequestCourse(auditCourse, peroids, week);
            CampusFactory.getRetrofitService().addCourse(convertCourseAdded(list.get(0)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(courseAddedResponse -> {
                        ToastUtil.showShort("课程已经加入");
                        holder.mBtnChooseCourse.setText("已添加");
                        Course c = convertCourse(list.get(0));
                        //必须要在服务端发送回来的时候在本地放置id!
                        c.setId(String.valueOf(courseAddedResponse.getData().getId()));
                        dao.insertCourse(c);
                        RxBus.getDefault().send(new AuditCourseEvent());
                    }, throwable -> {
                        throwable.printStackTrace();
                    }, () -> {
                    });
            CampusFactory.getRetrofitService().addCourse(convertCourseAdded(list.get(1)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(courseAddedResponse -> {
                        ToastUtil.showShort("课程已经加入");
                        holder.mBtnChooseCourse.setText("已添加");
                        Course c = convertCourse(list.get(1));
                        //必须要在服务端发送回来的时候在本地放置id!
                        c.setId(String.valueOf(courseAddedResponse.getData().getId()));
                        dao.insertCourse(c);
                        RxBus.getDefault().send(new AuditCourseEvent(true));
                    }, throwable -> {
                        throwable.printStackTrace();
                    }, () -> {
                    });

        } else {
            AuditCourse.ResBean audit = auditCourse;
            addCourseNetWork(audit, holder);
        }
    }

    //判断是不是一周两节课 是的话返回true
    private boolean isTwoClassWeek(String week) {
        int counter = 0;
        for (int i = 0; i < week.length(); i++) {
            if (week.charAt(i) == '\n')
                counter++;
        }
      return counter == 2;
    }

    //生成一个请求中使用的AuditCourse 在网络请求中会进行转化
    private List<AuditCourse.ResBean> createRequestCourse(AuditCourse.ResBean auditCourse, String[] period, String whenWeek) {
        List<AuditCourse.ResBean> courseList = new ArrayList<>();
        for (String p : period) {
            AuditCourse.ResBean audit = new AuditCourse.ResBean();
            audit.setName(auditCourse.getName());
            audit.setForwho(auditCourse.getForwho());
            audit.setKind(auditCourse.getKind());
            audit.setNo(auditCourse.getNo());
            audit.setRank(auditCourse.getRank());
            audit.setTeacher(auditCourse.getTeacher());
            List<AuditCourse.ResBean.WwBean> list = new ArrayList<>();
            String week = whenWeek;
            AuditCourse.ResBean.WwBean wwBean = new AuditCourse.ResBean.WwBean();
            wwBean.setWhen(p + "{" + week + "}");
            wwBean.setWhere(auditCourse.getWw().get(0).getWhere());
            list.add(wwBean);
            audit.setWw(list);
            courseList.add(audit);
        }
        return courseList;
    }

    public void addCourseNetWork(AuditCourse.ResBean auditCourse, AuditViewHolder holder) {
        CampusFactory.getRetrofitService()
                .addCourse(convertCourseAdded(auditCourse))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(courseAddedResponse -> {
                    holder.mBtnChooseCourse.setText("已添加");
                    ToastUtil.showShort("课程已经加入");
                    Course c = convertCourse(auditCourse);
                    //必须要在服务端发送回来的时候在本地放置id!
                    c.setId(String.valueOf(courseAddedResponse.getData().getId()));
                    dao.insertCourse(c);
                    RxBus.getDefault().send(new AuditCourseEvent(true));

                }, throwable -> {
                    throwable.printStackTrace();
                }, () -> {
                });
    }

    //true 表示 冲突 false 表示不冲突 默认情况下返回冲突
    private boolean isConflict(String period, String[] courseArray) {
        boolean flag = false;
        //之所以要这样表示是因为mcourses 不能很快的根据星期几查询到那天的课表内容
        for (Course course : mCourses) {
            //格式: 星期一1-2
            int infos[] = getDayDuring(period);
            int day = infos[0], start = infos[1], during = infos[2];
            //原来在课表中的周数表示
            String array[] = TextUtils.split(Course.listToString(course.getWeeks()), ",");
            List<String> formerWeek = Arrays.asList(array), auditWeek = Arrays.asList(courseArray);
            //false 没有交集: true 有交集
            if (isIntersection(formerWeek, auditWeek)) {
                if (getDay(course.getDay()) == day) {
                    if (!((course.getStart() != start)
                            && ((course.getStart() + course.getDuring()) != (start + during)))) {
                        flag = true;
                        return flag;
                    } else
                        flag = false;
                } else {
                    flag = false;
                }
            }
        }
        return flag;
    }

    //转成一个请求添加课程的对象
    private CourseAdded convertCourseAdded(AuditCourse.ResBean auditCourse) {
        CourseAdded courseAdded = new CourseAdded();
        courseAdded.setCourse(auditCourse.getName());
        courseAdded.setTeacher(auditCourse.getTeacher());

        courseAdded.setCourse(auditCourse.getName());
        courseAdded.setTeacher(auditCourse.getTeacher());
        String info[] = AuditCourse.getCourseTime(auditCourse.getWw().get(0).getWhen());
        courseAdded.setWeeks(Course.convertWeeks(getWeekString(info[1])));
        courseAdded.setDay(getDay(getDayDuring(info[0])[0]));
        courseAdded.setStart(getDayDuring(info[0])[1]+"");
        courseAdded.setDuring(getDayDuring(info[0])[2]+"");
        courseAdded.setPlace(getCorrectPlace(auditCourse.getWw().get(0).getWhere()));
        return courseAdded;
    }

    private Course convertCourse(AuditCourse.ResBean auditCourse) {
        Course course = new Course();
        /*
    public String remind;
         */
        course.setColor(2);
        //课程名称
        course.setCourse(auditCourse.getName());
        course.setTeacher(auditCourse.getTeacher());
        String info[] = AuditCourse.getCourseTime(auditCourse.getWw().get(0).getWhen());
        course.setWeeks(Course.convertWeeks(getWeekString(info[1])));
        //auditCourses 格式 :星期一7-8节
        course.setDay(getDay(getDayDuring(info[0])[0]));
        course.setStart(getDayDuring(info[0])[1]);
        course.setDuring(getDayDuring(info[0])[2]);
        course.setPlace(getCorrectPlace(auditCourse.getWw().get(0).getWhere()));
        course.setRemind("false");
        return course;
    }

    //判断两个集合是否有交集
    private <T> boolean isIntersection(List<T> list1, List<T> list2) {
        int size1 = list1.size(), size2 = list2.size();
        int size;
        if (size1 > size2) {
            size = size2;
        } else {
            size = size1;
        }
        for (int i = 0; i < size; i++) {
            if (list1.contains(list2.get(i))) {
                return true;
            }
        }
        return false;
    }

    //对于这样的地点信息会进行一个处理 : @9302.0 -> @9302
    private String getCorrectPlace(String site) {
        String correctString = "";
        if (site.contains(".")) {
            correctString += site.split("\\.")[0];
        } else {
            correctString += site;
        }
        return correctString;
    }

    //获取课程开始是一周开始的星期几 和 课程开始的节数和持续的时间
    private int[] getDayDuring(String period) {
        String patter = "(([^0-9]*)(\\d+)-(\\d+))";
        Pattern pattern = Pattern.compile(patter);
        Matcher matcher = pattern.matcher(period);
        String day = "";
        int start = 0, end = 0;
        while (matcher.find()) {
            day = matcher.group(2).substring(0, 3);
            start = Integer.parseInt(matcher.group(3));
            end = Integer.parseInt(matcher.group(4));
        }
        int dayN = getDay(day);
        int during = end - start + 1;
        return new int[]{dayN, start, during};
    }

    //通过星期几输出相应的值
    private int getDay(String day) {
        if (day.equals("星期一")) {
            return 1;
        }
        if (day.equals("星期二")) {
            return 2;
        }
        if (day.equals("星期三")) {
            return 3;
        }
        if (day.equals("星期四")) {
            return 4;
        }
        if (day.equals("星期五")) {
            return 5;
        }
        if (day.equals("星期六")) {
            return 6;
        }
        if (day.equals("星期日") || day.equals("星期天")) {
            return 7;
        }
        return 0;
    }

    //通过相应的数字输出星期几
    private String getDay(int day) {
        switch (day) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期日";
        }
        return "";
    }

    //eg : audit class 的课程周数格式为 1-7 周 要改成课表的格式 1,2,3,4,5,6,7
    private String getWeekString(String week) {
        String [] weeks=getWeek(week);
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < weeks.length; i++) {
            sb.append(weeks[i]).append(",");
        }

        return sb.substring(0,sb.length()-1).toString();
    }

    @Override
    public int getItemCount() {
        return auditCourses.getRes().size();
    }

    static class AuditViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvCourseMarker;
        private TextView mTvCourseNameTeacher;
        private TextView mTvCourseWeek;
        private TextView mTvCoursePeriod;
        private TextView mTvCourseSite;
        private Button mBtnChooseCourse;

        public AuditViewHolder(View itemView) {
            super(itemView);
            mIvCourseMarker = itemView.findViewById(R.id.iv_course_marker);
            mTvCourseNameTeacher = itemView.findViewById(R.id.tv_course_name_teacher);
            mTvCourseWeek = itemView.findViewById(R.id.tv_course_week);
            mTvCoursePeriod = itemView.findViewById(R.id.tv_course_period);
            mTvCourseSite = itemView.findViewById(R.id.tv_course_site);
            mBtnChooseCourse = itemView.findViewById(R.id.btn_choose_course);
        }
    }
}
