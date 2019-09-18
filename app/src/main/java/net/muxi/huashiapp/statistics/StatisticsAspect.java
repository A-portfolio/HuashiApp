package net.muxi.huashiapp.statistics;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.muxistudio.appcommon.data.StatisticsData;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import com.muxistudio.appcommon.data.StatisticsData.DataBean;
import com.muxistudio.appcommon.db.HuaShiDao;

import net.muxi.huashiapp.App;

import retrofit2.HttpException;

@Aspect
public class StatisticsAspect {



    /**
     * pid : String
     * deviceId : String
     * type : String
     * mainCat : String
     * subCat : String
     * value : String
     * timestamp : Int
     * extra : String
     */
    public static final String TAG="statistics";

    //pageView
    //activity打开统计

    @Pointcut("execution(* onCreate(..))&&within(com.muxistudio.appcommon.appbase.BaseAppActivity)")
    public void activityCreate(){}
    @After("activityCreate()")
    public void afterActivityCreate(JoinPoint point){
        Log.i(TAG, "afterActivityCreate: "+ App.getDevicesId());
        Log.i(TAG, "afterActivityCreate: "+point.getThis().getClass().getSimpleName());
        long time=System.currentTimeMillis();
        App.getMainPool().execute(()->{
            Log.i(TAG, "afterActivityCreate: execute");
            DataBean dataBean=new DataBean.Builder()
                    .deviceId(App.getDevicesId())
                    .mainCat("pageView")
                    .subCat(point.getThis().getClass().getSimpleName())
                    .timestamp(String.valueOf(time))
                    .Build();
            HuaShiDao.insertStatisticsData(dataBean);


        });


    }


    //fragment统计
    //1.统计fragment创建
    @Pointcut("execution(void onCreate(..)) && within(android.support.v4.app.Fragment) && target(fragment)")
    public void fragmentResume(Fragment fragment) {}
    @After("fragmentResume(fragment)")
    public void afterFragment(Fragment fragment){
        if (fragment.getClass().getSimpleName().equals("CardFragment"))return;
        Log.i(TAG, "afterFragment: "+fragment.getClass().getSimpleName());
        long time=System.currentTimeMillis();
        App.getMainPool().execute(()->{
            Log.i(TAG, "afterFragment: execute");
            DataBean dataBean=new DataBean.Builder()
                    .deviceId(App.getDevicesId())
                    .mainCat("pageView")
                    .subCat(fragment.getClass().getSimpleName())
                    .timestamp(String.valueOf(time))
                    .Build();
            HuaShiDao.insertStatisticsData(dataBean);


        });
    }

    //2.统记隐藏打开：
    @Pointcut("execution(void onHiddenChanged(boolean)) && within(android.support.v4.app.Fragment) && target(fragment) && args(hidden)")
    public void onHiddenChanged(Fragment fragment, boolean hidden) {}
    @After("onHiddenChanged(fragment,hidden)")
    public void afterFragmentHidden(Fragment fragment,boolean hidden){
        if (hidden)return;
        if (fragment.getClass().getSimpleName().equals("CardFragment"))return;
        Log.i(TAG, "afterFragmentHidden: " + fragment.getClass().getSimpleName());
        long time=System.currentTimeMillis();
        App.getMainPool().execute(()->{
            Log.i(TAG, "afterFragmentHidden: execute");
            DataBean dataBean=new DataBean.Builder()
                    .deviceId(App.getDevicesId())
                    .mainCat("pageView")
                    .subCat(fragment.getClass().getSimpleName())
                    .timestamp(String.valueOf(time))
                    .Build();
            HuaShiDao.insertStatisticsData(dataBean);


        });
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
        long time=System.currentTimeMillis();
        App.getMainPool().execute(()->{
            Log.i(TAG, "afterFragmentVisible: execute");
            DataBean dataBean=new DataBean.Builder()
                    .deviceId(App.getDevicesId())
                    .mainCat("pageView")
                    .subCat(fragment.getClass().getSimpleName())
                    .timestamp(String.valueOf(time))
                    .Build();
            HuaShiDao.insertStatisticsData(dataBean);


        });

    }




    //4.onclick()事件统计：userEvent
    //因为onclick方法实现的方式不同（匿名内部类,实现接口...所以干脆加两个call和execution）
    @After("call(void android.view.View.OnClickListener+.onClick(..))  && args(view)")
    public void afterOnClickAll(View view,JoinPoint joinPoint) {
        Log.i(TAG, "afterOnClickAll: "+joinPoint.getThis().getClass().getSimpleName());
        Log.i(TAG, "afterOnClickAll: "+view.getClass().getSimpleName());

        long time=System.currentTimeMillis();
        App.getMainPool().execute(()->{
            Log.i(TAG, "afterOnClickAll: execute");
            DataBean dataBean=new DataBean.Builder()
                    .deviceId(App.getDevicesId())
                    .mainCat("userEvent")
                    .subCat(joinPoint.getThis().getClass().getSimpleName())
                    .value(view.getClass().getSimpleName())
                    .type("click")
                    .timestamp(String.valueOf(time))
                    .Build();
            HuaShiDao.insertStatisticsData(dataBean);


        });
    }

    @After("execution(void android.view.View.OnClickListener+.onClick(..))  && args(view)")
    public void afterOnClick(View view,JoinPoint joinPoint) {
        Log.i(TAG, "afterOnClickAll: "+joinPoint.getThis().getClass().getSimpleName());
        Log.i(TAG, "afterOnClickAll: "+view.getClass().getSimpleName());

        try {
            Resources res = view.getResources();
            Log.i(TAG, "afterOnClick: " + res.getResourceEntryName(view.getId()));
            long time=System.currentTimeMillis();
            App.getMainPool().execute(()->{
                Log.i(TAG, "afterOnClick: execute");
                DataBean dataBean=new DataBean.Builder()
                        .deviceId(App.getDevicesId())
                        .mainCat("userEvent")
                        .subCat(joinPoint.getThis().getClass().getSimpleName())
                        .value(view.getClass().getSimpleName())
                        .extra(res.getResourceEntryName(view.getId()))
                        .type("click")
                        .timestamp(String.valueOf(time))
                        .Build();
                HuaShiDao.insertStatisticsData(dataBean);


            });

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
        long time=System.currentTimeMillis();
        App.getMainPool().execute(()-> {
            Log.i(TAG, "afterOpenWebPage: execute");
            DataBean dataBean = new DataBean.Builder()
                    .deviceId(App.getDevicesId())
                    .mainCat("pageView")
                    .subCat("WebViewActivity")
                    .value((String) joinPoint.getArgs()[1])
                    .type("WebView")
                    .timestamp(String.valueOf(time))
                    .Build();
            HuaShiDao.insertStatisticsData(dataBean);
        });

    }

    public static long lastOnErrorTime=0;
    //apiEvent
    @After("execution(* rx.Observer+.onError(..))")
    public void afterOnerror(JoinPoint joinPoint){
        long time=System.currentTimeMillis();
        long lastTime=net.muxi.huashiapp.statistics.StatisticsAspect.lastOnErrorTime;
        net.muxi.huashiapp.statistics.StatisticsAspect.lastOnErrorTime=time;
        if (time-lastTime<500)return;
        //处理重复
        Log.i(TAG, "afterOnerror: "+joinPoint.getArgs()[0].getClass().getSimpleName());
        if (joinPoint.getArgs()[0] instanceof HttpException){
            HttpException e=(HttpException) joinPoint.getArgs()[0];
            Log.i(TAG, "afterOnerror: "+e.message());
            Log.i(TAG, "afterOnerror: "+e.response().raw().request().url().toString());


            long cTime=System.currentTimeMillis();
            App.getMainPool().execute(()-> {
                Log.i(TAG, "afterOnerror: execute");
                DataBean dataBean = new DataBean.Builder()
                        .deviceId(App.getDevicesId())
                        .mainCat("apiEvent")
                        .subCat(e.response().raw().request().url().toString())
                        .value(e.message())
                        .timestamp(String.valueOf(cTime))
                        .Build();
                HuaShiDao.insertStatisticsData(dataBean);
            });
        }


    }



}
