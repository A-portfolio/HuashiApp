package com.muxistudio.appcommon;

import com.muxistudio.common.base.Global;

import java.io.File;

/**
 * Created by ybao on 16/6/23.
 */
public class Constants {

    public static final String UMENG_APP_KEY = "58b55d3d8f4a9d21ce0013ed";
    public static final String MI_PUSH_APPID = "2882303761517505217";
    public static final String MI_PUSH_APPKEY = "5981750541217";
    public static final String MI_PUSH_APP_SECRET = "SAa6GxYItJjLPNEW/LC5Vg==";

    public static final String QQ_GROUP_NUMBER = "576225292";
    public static final String LIBRARY_QUERY_TEXT = "queryText";
    public static final String TIP_CHECK_NET = "当前网络不可用,请检查网络连接";
    public static final String ALARMTIME = "alarmTime";

    public static final String ALL_CREDIT = "sAllCredit";

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

    /**
     * 这个分类中 对应的字段是{@link com.muxistudio.appcommon.data.Score} 中 kcxzmc字段
     */
    public static final String[] CLASS_TYPE = {
            "专业主干课程",
            "个性发展课程",
            "通识核心课",
            "通识必修课",
            "通识选修课",
            "其他"
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

    public static final File CACHE_DIR = Global.getApplication().getExternalCacheDir();
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
