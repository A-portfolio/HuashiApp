package net.muxi.huashiapp.util;

import android.content.ComponentName;
import android.content.res.Resources;

import com.zhuge.analysis.stat.ZhugeSDK;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ybao on 16/8/15.
 */
public class ZhugeUtils {

    public static void sendEvent (String title,String detail){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(title,detail);
            ZhugeSDK.getInstance().track(App.sContext,title,jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendEvent(String title){
        sendEvent(title,UserUtil.getStudentGrade());
    }

    public static void sendEvent(ComponentName componentName){
        sendEvent(getComponentEventName(componentName));
    }

    private static String getComponentEventName(ComponentName componentName) {
        Resources resources = App.sContext.getResources();
        switch (componentName.getClassName()){
            case "net.muxi.huashiapp.ui.score.ScoreActivity":
                return resources.getString(R.string.event_score);
            case "net.muxi.huashiapp.ui.score.electricity.ElectricityActivity":
                return resources.getString(R.string.event_ele);
            case "net.muxi.huashiapp.ui.CalendarActivity":
                return resources.getString(R.string.event_calendar);
            case "net.muxi.huashiapp.ui.apartment.ApartmentActivity":
                return resources.getString(R.string.event_apartment);
            case "net.muxi.huashiapp.ui.InfoActivity":
                return resources.getString(R.string.event_notification);
            case "net.muxi.huashiapp.ui.website.WebsiteActivity":
                return resources.getString(R.string.event_net);
            case "net.muxi.huashiapp.ui.studyroom.StudyRoomActivity":
                return resources.getString(R.string.event_room);
            case "net.muxi.huashiapp.ui.credit.SelectCreditActivity":
                return resources.getString(R.string.event_credit);
            case "net.muxi.huashiapp.ui.card.CardActivity":
                return resources.getString(R.string.event_card);
        }
        return null;
    }
}
