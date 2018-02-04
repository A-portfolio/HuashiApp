package net.muxi.huashiapp.ui.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.event.RefreshTableEvent;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.util.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kolibreath on 18-2-2.
 */

public class CourseAuditAdapter extends RecyclerView.Adapter<CourseAuditAdapter.AuditViewHolder> {

    private List<Integer> positions = new ArrayList<>();
    private List<Course> mCourses;
    private HuaShiDao dao;
    private AuditCourse auditCourse;
    public CourseAuditAdapter(AuditCourse course){
        this.auditCourse = course;
        dao = new HuaShiDao();
        mCourses = dao.loadAllCourses();
    }

    @Override
    public AuditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_audit,parent,false);
        return new AuditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AuditViewHolder holder, int position) {
        AuditCourse.ResBean course = auditCourse.getRes().get(position);
        String courseNameTeacher = course.getName()+"("+course.getTeacher()+")";
        holder.mTvCourseNameTeacher.setText(courseNameTeacher);
        List<AuditCourse.ResBean.WwBean> wwBeanList = auditCourse.getRes().get(position).getWw();
        String where ="", whenPeriod = "",whenWeek = "";
        for(AuditCourse.ResBean.WwBean wwBean:wwBeanList){
            if(!wwBean.getWhere().equals(where)){
                where += wwBean.getWhere()+"\n";
            }
            String p[] = AuditCourse.getCourseTime(wwBean.getWhen());
            whenPeriod += p[0]+"\n";
            whenWeek  += p[1]+"\n";
        }
        //todo  replace  the holder 加载根据课程编号加载正确的图片
        //格式 是这样子的 : "no" : "48740002.0"已经取出来了编号
        String number = course.getNo();
        holder.mTvCourseWeek.setText(whenWeek);
        holder.mTVCoursePeriod.setText(whenPeriod);
        holder.mTvCourseSite.setText(where);
        holder.mBtnChooseCourse.setOnClickListener(v->{
            if(!positions.contains(position)){
                positions.add(position);
                String p[] = holder.mTVCoursePeriod.getText().toString().split("\n");
                for(int i=0;i<p.length;i++) {
                    //todo 这里有点问题! buggy!
                    if (isConflict(p[i])) {
                        ToastUtil.showShort("课程冲突");
                        holder.mBtnChooseCourse.setText("添加");
                        positions.remove((Integer) (position));
                    } else {
                        RxBus.getDefault().send(new RefreshTableEvent());
                        CampusFactory.getRetrofitService()
                                .addCourse(convertCourse(course))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(courseId -> {;
                                    ToastUtil.showShort("课程已经加入");
                                    holder.mBtnChooseCourse.setText("已添加");
                                    Course c = convertCourse(course);
                                    c.setId(String.valueOf(courseId));
                                    dao.insertCourse(convertCourse(course));
                                },throwable -> {
                                    throwable.printStackTrace();
                                },()->{});
                    }
                }
            }else{
                holder.mBtnChooseCourse.setText("添加");
                positions.remove((Integer)(position));
            }
        });
    }

    private String generateId() {
        List<Integer> lists = new ArrayList<>();
        for (Course course : dao.loadAllCourses()) {
            if (Integer.valueOf(course.id) < 1000) {
                lists.add(Integer.valueOf(course.id));
            }
        }
        if (lists.size() == 0) {
            return "1";
        }
        Collections.sort(lists);
        return String.valueOf(lists.get(lists.size() - 1) + 1);
    }

    //true 表示 冲突 false 表示不冲突 默认情况下返回冲突
    private boolean isConflict(String period){
        boolean flag = false;
        for (Course course: mCourses) {
            //格式: 星期一1-2
            int infos[]  =getDayDuring(period);
            int day = infos[0], start = infos[1], during = infos[2];
            if (getDay(course.getDay())==day){
                if (!((course.getStart() != start) && ((course.getStart() + course.getDuring()) != (start + during)))) {
                    flag = true;
                    return flag;
                }
                else
                    flag = false;
            }else{
                flag = false;
            }
        }
        return flag;
    }

    //todo 转化两种课程格式 然后 添加到数据库中!
    private Course convertCourse(AuditCourse.ResBean auditCourse){
        Course course = new Course();
        int size = mCourses.size();
        int color = (size-1)%3;
        course.setColor(2);
        //课程名称
        course.setCourse(auditCourse.getName());
        course.setTeacher(auditCourse.getTeacher());
        //todo 对于多个地点或者是多个周的有点问题
        String info[] = AuditCourse.getCourseTime(auditCourse.getWw().get(0).getWhen());
        course.setWeeks(getWeekString(info[1]));
        //auditCourse 格式 :星期一7-8节
        course.setStart(getDayDuring(info[0])[1]);
        course.setDuring(getDayDuring(info[0])[2]);
        //todo buggy 多个地点可能会错误
        course.setPlace(auditCourse.getWw().get(0).getWhere());
        course.setRemind("false");
        course.setId(generateId());
        return course;
    }

    //获取课程开始是一周开始的星期几 和 课程开始的节数和持续的时间
    private int[] getDayDuring(String period){
        String patter = "(([^0-9]*)(\\d+)-(\\d+))";
        Pattern pattern = Pattern.compile(patter);
        Matcher matcher = pattern.matcher(period);
        String day = "";
        int start = 0, end = 0;
        while(matcher.find()){
            day = matcher.group(2).substring(0,3);
            start   = Integer.parseInt(matcher.group(3));
            end   =   Integer.parseInt(matcher.group(4));
        }
        int dayN = getDay(day);
        int during = end - start + 1;
        return new int[]{dayN,start,during};
    }
    //通过星期几输出相应的值
    private int getDay(String day){
        if(day.equals("星期一")){
            return 1;
        }
        if(day.equals("星期二")){
            return 2;
        }
        if(day.equals("星期三")){
            return 3;
        }
        if(day.equals("星期四")){
            return 4;
        }
        if(day.equals("星期五")){
            return 5;
        }
        if(day.equals("星期六")){
            return 6;
        }
        if(day.equals("星期日")||day.equals("星期天")){
            return 7;
        }
        return  0;
    }

    //通过相应的数字输出星期几
    private String getDay(int day){
        switch (day){
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
        return  "";
    }
    //eg : audit class 的课程周数格式为 1-7 周 要改成课表的格式 1,2,3,4,5,6,7
    private String getWeekString(String week){
        String patter = "((\\d+)-(\\d+))";
        Pattern pattern = Pattern.compile(patter);
        Matcher matcher = pattern.matcher(week);
        int start  = 0  ,end = 0;
        while (matcher.find()){
            start = Integer.parseInt(matcher.group(2));
            end   = Integer.parseInt(matcher.group(3));
        }
        String weekString  = "";
        for(int i = start;i<=end;i++){
            if(i!=end){
                weekString += i + ",";
            }else{
                weekString += i+ "";
            }
        }
        return weekString;
    }
    @Override
    public int getItemCount(){
        return auditCourse.getRes().size();
    }

    static class AuditViewHolder extends RecyclerView.ViewHolder{
        //何种类型的课程 通核通选 公共课 等等
        @BindView(R.id.iv_course_marker)
        ImageView mIvCourseMarker;
        //同时包含老师和课程名称
        @BindView(R.id.tv_course_name_teacher)
        TextView mTvCourseNameTeacher;
        TextView mTvCourseWeek;
        @BindView(R.id.tv_course_period)
        TextView mTVCoursePeriod;
        @BindView(R.id.tv_course_site)
        TextView mTvCourseSite;
        @BindView(R.id.btn_choose_course)
        Button mBtnChooseCourse;
        public AuditViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
