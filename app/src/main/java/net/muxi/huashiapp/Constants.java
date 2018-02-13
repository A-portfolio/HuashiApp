package net.muxi.huashiapp;

import java.io.File;

/**
 * Created by ybao on 16/6/23.
 */
public class Constants {

    //数字后面是成功登录的网站的名称
    //3: 信息门户 教务系统 图书馆  2:信息门户 教务系统 1:信息门户 图书馆 0:信息门户 -1:全部登录失败
    public static final String LOGIN_CODE[] = {"3","2","1","0","-1"};
    public static final String LIBRARY_QUERY_TEXT = "queryText";
    public static final String TIP_CHECK_NET = "当前网络不可用,请检查网络连接";
    public static final String ALARMTIME = "alarmTime";
    public static final String[] WEEKDAYS = {"一",
            "二",
            "三",
            "四",
            "五",
            "六",
            "日"};
    public static final String[] WEEKDAYS_XQ = {
            "星期一",
            "星期二",
            "星期三",
            "星期四",
            "星期五",
            "星期六",
            "星期日"
    };
    public static final String[] WEEKS = {"第一周",
            "第二周",
            "第三周",
            "第四周",
            "第五周",
            "第六周",
            "第七周",
            "第八周",
            "第九周",
            "第十周",
            "第十一周",
            "第十二周",
            "第十三周",
            "第十四周",
            "第十五周",
            "第十六周",
            "第十七周",
            "第十八周",
            "第十九周",
            "第二十周",
            "第二十一周"
    };

    public static final String[] CREDIT_CATEGORY = {
            "专业必修课",
            "专业选修课",
            "通识必修课",
            "通识选修课",
            "通识核心课",
    };

    public static final String[] TERMS = {
            "3",
            "12",
            "16"
    };

    public static final int WEEKS_LENGTH = 21;

    public static final int ANIMATION_DURATION = 100;

    //服务器端mongdb数据库要求初始必须有门课程
    public static final String INIT_COURSE = "re:从零开始的异世界生活";

    public static final File CACHE_DIR = App.getContext().getExternalCacheDir();
    public static final String APATCH_NAME = "out.apatch";
    public static final String APATCH_URL = "http://7xtask.com2.z0.glb.clouddn.com/out.apatch";

    public static final String QQ_KEY = "1105548375";
    public static final String WEIBO_KEY = "4167359117";
    public static final String WEIBO_INIT_TEXT = "(分享自华师匣子)";
    public static final String APP_DOWNLOAD_URL = "http://fir.im/3lbx";

    public static final String SPLASH_URL = "splashUrl";
    public static final String SPLASH_IMG = "splashImgUrl";
    public static final String SPLASH_UPDATE = "splashUpdate";

    public static final String PRODUCT_JSON = "{\"update\": 1497603170.0, \"_product\": [{\"url\": \"https://xueer.muxixyz.com\", \"intro\": \"huashi xuanke\", \"name\": \"\\u5b66\\u800c\", \"icon\": \"http://static.muxixyz.com/ic_xueer.png\"}]}";

}
