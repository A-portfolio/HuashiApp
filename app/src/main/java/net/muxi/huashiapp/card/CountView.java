package net.muxi.huashiapp.card;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by december on 16/11/1.
 */

public class CountView extends View {
    private Paint mLinePaint;
    private Paint mCirclePaint;
    private Paint mTextPaint;

    private String circleColor = "#512DA8";
    private String baseColor = "#FFFFFF";

    private int textSize = 20;
    private int r = 5;

    int width;
    int height;

    private int y_max = 200;
    private int y_min = 0;

    private int xOffset = 40;

    //七天日期
    private List<String> mDateList;
    //数据
    private List<DailyData> mDailyDatas;
    private List<PointEntity> mPointentities;



    public CountView(Context context) {
        super(context, null);
    }

    public CountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CountView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     */
    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.parseColor(baseColor));
        mLinePaint.setAntiAlias(true);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.parseColor(circleColor));
        mCirclePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor(baseColor));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);

        mDateList = new ArrayList<>();
        DateTime dateTime = new DateTime(new Date());
        for (int i = 6; i >= 0; i--) {
            DateTime time = dateTime.minusDays(i);
            String day = time.toString("yyyy-MM-dd HH:mm:ss");
            mDateList.add(day);
        }
    }


    //测量View的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(320, 380);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(320, heightSpecSize);

        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 380);

        }
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawData(canvas);

    }

    /**
     * 数据展示
     *
     * @param canvas
     */
    private void drawData(Canvas canvas) {
        try {
            if (mDateList != null && mDailyDatas != null ) {
                long endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mDateList.get(0).split(" ")[0] + "23:59:59").getTime();
                long startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mDateList.get(0).split(" ")[0] + " 00:00:00").getTime();
                float l = (float) ((width - (float)xOffset / (endTime - startTime)));


                mPointentities = new ArrayList<>();
                for (int i = 0; i < mDailyDatas.size(); i++) {
                    DailyData data  = mDailyDatas.get(i);
                    float x = (startTime);
                    float y = (0);
                    PointEntity pointentity = new PointEntity();
                    pointentity.setX(x);
                    pointentity.setY(y);
                    mPointentities.add(pointentity);
                    canvas.drawLine(pointentity.getX(), pointentity.getY(), pointentity.getX(), pointentity.getY(), mLinePaint);
                }

                for (int i = 0; i < mPointentities.size() - 1; i++) {
                    PointEntity pointEntity = mPointentities.get(i);
                    PointEntity pointEntityNext = mPointentities.get(i + 1);
                    canvas.drawLine(pointEntity.getX(), pointEntity.getY(), pointEntityNext.getX(), pointEntityNext.getY(), mLinePaint);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void drawX(Canvas canvas){
        Paint axisPaint = new Paint();
        axisPaint.setTextSize(textSize);
        axisPaint.setColor(Color.parseColor("#3F51B5"));

        int xInterval = (int) (width-xOffset)/(mDateList.size());
        int xItemY = 0;

        for (int i=0;i<mDateList.size();i++){
            canvas.drawText(mDateList.get(i).substring(8,10),i*xInterval+xOffset,xItemY,axisPaint);
        }
    }


    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }


    public void setR(int r) {
        this.r = r;
    }


    public int getY_max() {
        return y_max;
    }

    public void setY_max(int y_max) {
        this.y_max = y_max;
    }

    public int getY_min() {
        return y_min;
    }

    public void setY_min(int y_min) {
        this.y_min = y_min;
    }





    static class PointEntity {
        private Float x;
        private Float y;

        public Float getX() {
            return x;
        }

        public void setX(Float x) {
            this.x = x;
        }

        public Float getY() {
            return y;
        }

        public void setY(Float y) {
            this.y = y;
        }
    }
}