package net.muxi.huashiapp.ui.main;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by december on 17/2/27.
 */

public class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    private GestureDetectorCompat mGestureDetectorCompat;
    private RecyclerView mRecyclerView;

    public OnRecyclerItemClickListener(RecyclerView recyclerView){
        this.mRecyclerView = recyclerView;
        mGestureDetectorCompat = new
                GestureDetectorCompat(recyclerView.getContext(),new ItemTouchHelperGestureListener());
    }


    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetectorCompat.onTouchEvent(e);

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child!=null) {
                RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(child);
                onItemClick(vh);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            View child = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child!=null) {
                RecyclerView.ViewHolder vh = mRecyclerView.getChildViewHolder(child);
                onLongClick(vh);
            }
        }
    }

    public void onLongClick(RecyclerView.ViewHolder vh){}
    public void onItemClick(RecyclerView.ViewHolder vh){}
}
