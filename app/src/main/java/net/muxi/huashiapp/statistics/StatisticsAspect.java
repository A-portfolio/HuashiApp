package net.muxi.huashiapp.statistics;

import android.support.v4.app.Fragment;
import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class StatisticsAspect {


    public static final String TAG="statistics";

    //activity打开统计

    @Pointcut("execution(* onCreate(..))&&within(com.muxistudio.appcommon.appbase.BaseAppActivity)")
    public void activityCreate(){}

    @After("activityCreate()")
    public void afterActivityCreate(JoinPoint point){
        Log.i(TAG, "afterActivityCreate: "+point.getThis().getClass().getSimpleName());
    }


    //fragment统计
    //1.统计fragment创建
    @Pointcut("execution(void onCreate(..)) && within(android.support.v4.app.Fragment) && target(fragment)")
    public void fragmentResume(Fragment fragment) {}

    @After("fragmentResume(fragment)")
    public void afterFragment(Fragment fragment){
        Log.i(TAG, "afterFragment: "+fragment.getClass().getSimpleName());

    }

    //2.统记隐藏打开：
    @Pointcut("execution(void onHiddenChanged(boolean)) && within(android.support.v4.app.Fragment) && target(fragment) && args(hidden)")
    public void onHiddenChanged(Fragment fragment, boolean hidden) {}

    @After("onHiddenChanged(fragment,hidden)")
    public void afterFragmentHidden(Fragment fragment,boolean hidden){
        Log.i(TAG, "afterFragmentHidden: " + fragment.getClass().getSimpleName());
        Log.i(TAG, "afterFragmentHidden: hidden"+hidden);

    }

    //3.viewpage+fragment转换调用这个方法：
    @Pointcut("execution(void setUserVisibleHint(..)) && within(android.support.v4.app.Fragment) && target(fragment) && args(visible)")
    public void setUserVisibleHint(Fragment fragment, boolean visible) {}

    @After("setUserVisibleHint(fragment,visible)")
    public void afterFragmentVisible(Fragment fragment,boolean visible){
        Log.i(TAG, "afterFragmentVisible: "+fragment.getClass().getSimpleName());

    }

}
