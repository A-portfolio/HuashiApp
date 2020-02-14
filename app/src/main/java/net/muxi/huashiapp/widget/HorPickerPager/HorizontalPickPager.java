package net.muxi.huashiapp.widget.HorPickerPager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
//import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import androidx.viewpager.widget.ViewPager;

import com.muxistudio.common.util.DimensUtil;

import net.muxi.huashiapp.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ybao (ybaovv@gmail.com)
 * Date: 17/2/28
 */

public class HorizontalPickPager extends ViewPager {

    private List<String> displayStrings;
    private HorPickerPagerAdapter mPagerAdapter;

    private int padding = DimensUtil.getScreenWidth() / 2 - DimensUtil.dp2px(57);

    public HorizontalPickPager(Context context) {
        this(context, null);
    }

    public HorizontalPickPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.argb(44, 255, 255, 255));
        setClipToPadding(true);
        setPadding(padding, 0, padding, 0);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.argb(255, 245, 245, 245));
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(getWidth() / 2 - DimensUtil.dp2px(41), 0,
                getWidth() / 2 + DimensUtil.dp2px(41), getHeight(), p);

        p.setColor(getResources().getColor(R.color.color_selected));
        canvas.drawRect(getWidth() / 2 - DimensUtil.dp2px(41), getHeight() - DimensUtil.dp2px(2),
                getWidth() / 2 + DimensUtil.dp2px(41), getHeight(), p);

    }

    public void setDisplayValues(String[] string){
        displayStrings.clear();
        displayStrings.addAll(Arrays.asList(string));
        if (mPagerAdapter == null){
            mPagerAdapter = new HorPickerPagerAdapter(displayStrings);
            setAdapter(mPagerAdapter);
            addOnPageChangeListener(new TransformerListener(mPagerAdapter));
        }else {
            mPagerAdapter.swap(displayStrings);
        }
    }

    public void setCurrentItem(int item){
        setCurrentItem(item,false);
    }

}
