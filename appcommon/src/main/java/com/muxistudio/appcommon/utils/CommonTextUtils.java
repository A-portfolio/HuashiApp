package com.muxistudio.appcommon.utils;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.muxistudio.appcommon.data.User;
import java.util.ArrayList;
import java.util.List;

/**
 * just for fun
 * author:kolibreath
 */
public class CommonTextUtils {

    public static void setText(TextView view){
        int random = (int) (System.currentTimeMillis()/10);
        switch (random){
            case 1:
                view.setText("———发现了一条彩蛋！点击我试试！———");
                view.setOnClickListener(v -> new AlertDialog.Builder(view.getContext())
                        .setTitle("一条来自木犀团队的消息")
                        .setMessage("Have a nice day!")
                        .setNegativeButton("取消", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setPositiveButton("确定", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create().show());
                default:
                    view.setText("———别笑，我也是有底线的———");
        }
    }

    /**
     *
     * @return 返回格式化之后的字符串作为查成绩或者查学分的时候的提示
     */
    public static String generateRandomScoreText(String year){
        String formats[] = new String[]{
                "嘿咻嘿咻~,正在查找%d学年-%d学年的成绩情况",
                "fufu~正在打开开往成绩档案的传送门"
        };
        int startYear = Integer.parseInt(year);
        int seed = (int) (System.currentTimeMillis()%formats.length);
        return String.format(formats[seed],startYear,startYear + 1);
    }

  /***
   *
   * @return 返回成绩查询的随机提示
   */
  public static String generateRandomLoginText(){
      String texts[]= new String[]{
          "正在前往学校服务器~~",
          "稍等稍等,正在获取你的数据",
          "匣子正在穿过网线"
      };

      return texts[(int) (System.currentTimeMillis()%texts.length)];
    }

  public static String generateRandomApartmentText(){
    String texts[]= new String[]{
        "匣子正在委托宿管查询信息..",
        "正在翻阅寝室资料"
    };

    return texts[(int) (System.currentTimeMillis()%texts.length)];
  }


  public static String generateRandomCourseText(){
    String texts[]= new String[]{
        "匣子正在请求教务处信息",
        "匣子正在查找课程资料~~"
    };

    return texts[(int) (System.currentTimeMillis()%2)];
  }


  public static String generateRandomNewsText(){
    String texts[]= new String[]{
        "匣子正在获取新闻资料",
        "西方记者匣子君正跑来给您送新闻啦",
        "匣子君的新闻是坠吼的"
    };

    return texts[(int) (System.currentTimeMillis()%2)];
  }
}

