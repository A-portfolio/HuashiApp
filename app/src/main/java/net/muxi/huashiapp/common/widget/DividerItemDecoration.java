package net.muxi.huashiapp.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ybao on 16/6/28.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    public static final int HORIZATIONAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private Drawable mDivider;
    private int mOrientation;

    public DividerItemDecoration(Context context,int orientation) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        if (orientation != VERTICAL_LIST && orientation != HORIZATIONAL_LIST){
            throw new IllegalArgumentException("invalid orientation");

        }
        mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == VERTICAL_LIST){
            outRect.set(0,0,0,mDivider.getIntrinsicHeight());
        }else if (mOrientation == HORIZATIONAL_LIST){
            outRect.set(0,0,mDivider.getIntrinsicWidth(),0);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == HORIZATIONAL_LIST){
            drawHorizationalDivider(c,parent);
        }else if (mOrientation == VERTICAL_LIST){
            drawVerticalDivider(c,parent);
        }
    }

    public void drawHorizationalDivider(Canvas c,RecyclerView parent){
        final int top = parent.getPaddingTop();
        final int bottom = parent.getBottom() - parent.getPaddingBottom();

        int childCount = parent.getChildCount();
        for (int i = 0;i < childCount;i ++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }

    public void drawVerticalDivider(Canvas c,RecyclerView parent){
        final int right = parent.getRight() - parent.getPaddingRight();
        final int left = parent.getPaddingLeft();

        int childCount = parent.getChildCount();
        for (int i = 0;i < childCount;i ++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }
}
