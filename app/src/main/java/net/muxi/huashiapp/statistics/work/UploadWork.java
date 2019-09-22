package net.muxi.huashiapp.statistics.work;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.muxistudio.appcommon.data.StatisticsData;
import com.muxistudio.appcommon.db.HuaShiDao;
import com.muxistudio.appcommon.net.CampusFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import okhttp3.ResponseBody;
import rx.Subscriber;

public class UploadWork extends Worker {
    public static final String TAG="WorkManager";
    public UploadWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        List<StatisticsData.DataBean>list;
        list= HuaShiDao.getAllStatisticsData();
        if (list.size()==0)
            return Result.success();
        StatisticsData data=new StatisticsData();
        data.setData(list);
        Boolean []isSuccess=new Boolean[1];
        CampusFactory.getRetrofitService().uploadStatisticsData(data)
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        Log.i(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        isSuccess[0]=false;
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        isSuccess[0]=true;
                        HuaShiDao.deleteAllStatistics();
                    }
                });

        if (isSuccess[0])
            return Result.success();
        else
            return Result.failure();
    }


}
