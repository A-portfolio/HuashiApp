package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/24
 */

//只含有课程id
public class CourseView extends TextView{

    private String cid;

    public CourseView(Context context) {
        this(context,null);
    }

    public CourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCourseId(String id){
        cid = id;
    }

    public String getCourseId(){
        return cid;
    }


}
