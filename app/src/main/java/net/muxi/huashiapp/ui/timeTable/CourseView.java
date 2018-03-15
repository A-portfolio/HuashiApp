package net.muxi.huashiapp.ui.timeTable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import net.muxi.huashiapp.R;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/24
 */

//只含有课程id
public class CourseView extends TextView{

    private String cid;
    private Context context;
    private CourseView courseView;
    private boolean isTipOn;

    public CourseView(Context context) {
        this(context,null);
        this.context = context;
    }
    public CourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isTipOn){
            //默认的颜色是绿色
            int color = context.getResources().getColor(R.color.green);
            setTip(color,canvas);
        }
    }

    public void setCourseId(String id){
        cid = id;
    }
    public String getCourseId(){
        return cid;
    }


    public void setTip(int color,Canvas canvas){
        Dot dot = new Dot(color);
        float cx  = getWidth() - dot.marginRight - dot.radius;
        float cy = dot.marginTop  + dot.radius;

        Drawable drawableTop = getCompoundDrawables()[1];
        if(drawableTop!=null){
            int drawableTopWidth = drawableTop.getIntrinsicWidth();
            if(drawableTopWidth>0){
                int dotLeft = getWidth() / 2 + drawableTopWidth / 2;
                cx = dotLeft + dot.radius;
            }
        }

        Paint paint = getPaint();
        //避免影响原来画笔的颜色
        int tempColor = paint.getColor();

        paint.setColor(dot.color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx,cy,dot.radius,paint);

        paint.setColor(tempColor);
    }

    public void setIsTipOn(boolean isTipOn){
        this.isTipOn = isTipOn;
    }
    class Dot{
        int color;
        int radius;
        int marginTop;
        int marginRight;

        public Dot( int color){
            float density = context.getResources().getDisplayMetrics().density;
            radius = (int) (5 * density);
            marginTop = (int) (3 * density);
            marginRight = (int) (3 * density);
            this.color = color;
        }
    }



}
