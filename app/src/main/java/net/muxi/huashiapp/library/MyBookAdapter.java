package net.muxi.huashiapp.library;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ybao on 16/7/30.
 * 已借图书的 adapter
 */
public class MyBookAdapter extends RecyclerView.Adapter<MyBookAdapter.ViewHolder>{

    public MyBookAdapter() {
        super();
    }

    @Override
    public void onBindViewHolder(MyBookAdapter.ViewHolder holder, int position) {

    }

    @Override
    public MyBookAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
