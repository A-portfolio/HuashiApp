package net.muxi.huashiapp.statistics;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import retrofit2.HttpException;

@Aspect
public class StatisticsAspect {


    public static final String TAG="statistics";

    //pageView
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
        if (hidden)return;
        Log.i(TAG, "afterFragmentHidden: " + fragment.getClass().getSimpleName());
        Log.i(TAG, "afterFragmentHidden: hidden"+hidden);

    }

    //3.viewpage+fragment转换调用这个方法：
    @Pointcut("execution(void setUserVisibleHint(..)) && within(android.support.v4.app.Fragment) && target(fragment) && args(visible)")
    public void setUserVisibleHint(Fragment fragment, boolean visible) {}
    @After("setUserVisibleHint(fragment,visible)")
    public void afterFragmentVisible(Fragment fragment,boolean visible){
        //主页面的banner一直在显示隐藏fragment，之后要改进，所以这里为了不重复统计先不记录他
        if (!visible)return;
        if (fragment.getClass().getSimpleName().equals("CardFragment"))return;
        Log.i(TAG, "afterFragmentVisible: "+fragment.getClass().getSimpleName());

    }




    //4.onclick()事件统计：userEvent
    //因为onclick方法实现的方式不同（匿名内部类,实现接口...所以干脆加两个call和execution）
    @After("call(void android.view.View.OnClickListener+.onClick(..))  && args(view)")
    public void afterOnClickAll(View view,JoinPoint joinPoint) {
        Log.i(TAG, "afterOnClickAll: "+joinPoint.getThis().getClass().getSimpleName());
        Log.i(TAG, "afterOnClickAll: "+view.getClass().getSimpleName());
    }

    @After("execution(void android.view.View.OnClickListener+.onClick(..))  && args(view)")
    public void afterOnClick(View view,JoinPoint joinPoint) {
        Log.i(TAG, "afterOnClickAll: "+joinPoint.getThis().getClass().getSimpleName());
        Log.i(TAG, "afterOnClickAll: "+view.getClass().getSimpleName());

        try {
            Resources res = view.getResources();
            Log.i(TAG, "afterOnClick: " + res.getResourceEntryName(view.getId()));
        }catch (Resources.NotFoundException e){
            Log.e(TAG, "afterOnClick: ",e );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //5.网页统计
    @After("execution(* net.muxi.huashiapp.ui.webview.WebViewActivity.newIntent(..))")
    public void afterOpenWebPage(JoinPoint joinPoint) {
        Log.i(TAG, "webPage: "+joinPoint.getArgs()[1]);
    }

    //apiEvent
    @After("execution(* rx.Observer+.onError(..))")
    public void afterOnerror(JoinPoint joinPoint){
        //处理重复
        Log.i(TAG, "afterOnerror: "+joinPoint.getArgs()[0].getClass().getSimpleName());
        if (joinPoint.getArgs()[0] instanceof HttpException){
            HttpException e=(HttpException) joinPoint.getArgs()[0];
            Log.i(TAG, "afterOnerror: "+e.message());
            Log.i(TAG, "afterOnerror: "+e.response().raw().request().url().toString());
        }


    }



}
