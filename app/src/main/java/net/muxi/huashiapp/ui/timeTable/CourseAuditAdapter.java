package net.muxi.huashiapp.ui.timeTable;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.RxBus;
import net.muxi.huashiapp.common.data.AuditCourse;
import net.muxi.huashiapp.common.data.Course;
import net.muxi.huashiapp.common.data.CourseId;
import net.muxi.huashiapp.common.db.HuaShiDao;
import net.muxi.huashiapp.event.AuditCourseEvent;
import net.muxi.huashiapp.net.CampusFactory;
import net.muxi.huashiapp.util.ToastUtil;

import java.util.ArrayList;
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
    private AuditCourse auditCourses;
    public CourseAuditAdapter(AuditCourse course){
        this.auditCourses = course;
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
        //这个course 是有id的
        AuditCourse.ResBean auditCourse = auditCourses.getRes().get(position);
        String courseNameTeacher = auditCourse.getName()+"("+auditCourse.getTeacher()+")";
        holder.mTvCourseNameTeacher.setText(courseNameTeacher);
        List<AuditCourse.ResBean.WwBean> wwBeanList = auditCourses.getRes().get(position).getWw();
        String where ="", whenPeriod = "",whenWeek = "";
        for(AuditCourse.ResBean.WwBean wwBean:wwBeanList){
            if(!wwBean.getWhere().equals(where)){
                where += wwBean.getWhere()+"\n";
            }
            String p[] = AuditCourse.getCourseTime(wwBean.getWhen());
            whenPeriod += p[0]+"\n";
            whenWeek  += p[1]+"\n";
        }
        String kind = auditCourse.getKind();
        if (kind.equals("专业课")) {
            holder.mIvCourseMarker.setImageResource(R.drawable.audit_professional);
//            ToastUtil.showLong("hh");
        } else if(kind.equals("公共课")){
            holder.mIvCourseMarker.setImageResource(R.drawable.audit_public);
        } else if(kind.equals("通核课")){
            holder.mIvCourseMarker.setImageResource(R.drawable.audit_core);
        } else if(kind.equals("通选课")){
            holder.mIvCourseMarker.setImageResource(R.drawable.audit_elective);
        }
        holder.mTvCourseWeek.setText(whenWeek);
        holder.mTVCoursePeriod.setText(whenPeriod);
        holder.mTvCourseSite.setText(where);
        holder.mBtnChooseCourse.setOnClickListener(v->{
            if(!positions.contains(position)){
                positions.add(position);
                String p[] = holder.mTVCoursePeriod.getText().toString().split("\n");
                //如果是两门课程的话都要不冲突才可以
                // 关于两门课程的解释 同一周的不同时间的同一门课程 教务处作为两门课程处理
                //只用有一门课的情况:
                if(p.length==1){
                    if(isConflict(p[0])){ ToastUtil.showShort("课程冲突");
                        holder.mBtnChooseCourse.setText("添加");
                        positions.remove((Integer) (position));
                        //上传事件
                        return;
                    } else {
                        addCourse(holder.mTVCoursePeriod.getText().toString(),auditCourse,holder);
                        return;
                    }
                }
                //如果是两门课
                boolean bothConflict = false;
                for(int i=0;i<p.length;i++){
                    if(isConflict(p[i])){
                        //只要一门冲突就都冲突
                        bothConflict = true;
                        ToastUtil.showShort("课程冲突");
                        holder.mBtnChooseCourse.setText("添加");
                        positions.remove((Integer) (position));
                        break;
                    }
                }
                if(!bothConflict){
                    addCourse(holder.mTVCoursePeriod.getText().toString(),auditCourse,holder);
                }
            }else {
                //还需要删除这门课
                holder.mBtnChooseCourse.setText("添加");
                positions.remove((Integer) (position));
            }
        });
    }

    //设置不同课程的提示
    //如果有两个时间的话需要上传两次时间和地点

    private void addCourse(String period,AuditCourse.ResBean auditCourse, AuditViewHolder holder){
        if(isTwoClassWeek(period)){
            String peroids[] = period.split("\n");
            //week 如果没有修改的格式是 1-17周"\n"1-17周 这两个是一样的 所以在下面的week参数中选取一样的
            String week = holder.mTvCourseWeek.getText().toString().split("\n")[0];
            List<AuditCourse.ResBean> list = createRequestCourse(auditCourse,peroids,week);
            rx.Observable<CourseId> observable1 = CampusFactory.getRetrofitService().addCourse(convertCourse(list.get(0)));
            rx.Observable<CourseId> observable2 = CampusFactory.getRetrofitService().addCourse(convertCourse(list.get(1)));
            rx.Observable<CourseId> mergeObservable = rx.Observable.merge(observable1,observable2);
            mergeObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(courseId -> {
                        ToastUtil.showShort("课程已经加入");
                        holder.mBtnChooseCourse.setText("已添加");
                        RxBus.getDefault().send(new AuditCourseEvent());
                        Course c = convertCourse(auditCourse);
                        //必须要在服务端发送回来的时候在本地放置id!
                        //todo 这里的策略有可能须要更改
                        c.setId(String.valueOf(courseId));
                        dao.insertCourse(convertCourse(auditCourse));
                    },throwable -> {
                        throwable.printStackTrace();
                    },()->{});
        }else{
            AuditCourse.ResBean audit = auditCourse;
            addCourseNetWork(audit,holder);
        }
    }

    //判断是不是一周两节课 是的话返回true
    private boolean isTwoClassWeek(String week){
        int counter = 0;
        for(int i=0;i<week.length();i++){
            if(week.charAt(i)=='\n')
                counter++;
        }
        if(counter==2){
            return true;
        }else{
            return false;
        }
    }
    //生成一个请求中使用的AuditCourse 在网络请求中会进行转化
    private List<AuditCourse.ResBean> createRequestCourse(AuditCourse.ResBean auditCourse,String[] period,String whenWeek){
        List<AuditCourse.ResBean>  courseList = new ArrayList<>();
        for(String p:period) {
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
    public void addCourseNetWork(AuditCourse.ResBean auditCourse,AuditViewHolder holder){
//        RxBus.getDefault().send(new RefreshTableEvent());
        CampusFactory.getRetrofitService()
                .addCourse(convertCourse(auditCourse))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(courseId -> {
                    RxBus.getDefault().send(new AuditCourseEvent());
                    holder.mBtnChooseCourse.setText("已添加");
                    ToastUtil.showShort("课程已经加入");
                    Course c = convertCourse(auditCourse);
                    //必须要在服务端发送回来的时候在本地放置id!
                    c.setId(String.valueOf(courseId));
                    dao.insertCourse(convertCourse(auditCourse));
                },throwable -> {
                    throwable.printStackTrace();
                },()->{});
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
    private Course convertCourse(AuditCourse.ResBean auditCourse){
        Course course = new Course();
        /*
    public String remind;
         */
        course.setColor(2);
        //课程名称
        course.setCourse(auditCourse.getName());
        course.setTeacher(auditCourse.getTeacher());
        String info[] = AuditCourse.getCourseTime(auditCourse.getWw().get(0).getWhen());
        course.setWeeks(getWeekString(info[1]));
        //auditCourses 格式 :星期一7-8节
        course.setDay(getDay(getDayDuring(info[0])[0]));
        course.setStart(getDayDuring(info[0])[1]);
        course.setDuring(getDayDuring(info[0])[2]);
        course.setPlace(getCorrectPlace(auditCourse.getWw().get(0).getWhere()));
        course.setRemind("false");
        return course;
    }

    //对于这样的地点信息会进行一个处理 : @9302.0 -> @9302
    private String getCorrectPlace(String site){
        String correctString = "";
        if(site.contains(".")){
            correctString += site.split("\\.")[0];
        }else{
            correctString += site;
        }
        return correctString;
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
        return auditCourses.getRes().size();
    }

    static class AuditViewHolder extends RecyclerView.ViewHolder{
        //何种类型的课程 通核通选 公共课 等等
        @BindView(R.id.iv_course_marker)
        ImageView mIvCourseMarker;
        //同时包含老师和课程名称
        @BindView(R.id.tv_course_name_teacher)
        TextView mTvCourseNameTeacher;
        @BindView(R.id.tv_course_week)
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
