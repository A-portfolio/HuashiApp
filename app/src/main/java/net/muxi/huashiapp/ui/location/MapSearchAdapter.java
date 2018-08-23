package net.muxi.huashiapp.ui.location;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.muxi.huashiapp.R;

import java.util.List;

/**
 * Created by yue on 2018/8/21.
 */

public class MapSearchAdapter extends RecyclerView.Adapter<MapSearchAdapter.ViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<PointSearch> mList;
    private String name = "";

    public MapSearchAdapter(Context context,List<PointSearch> list){
        this.mContext = context;
        this.mList = list;

    }

    @Override
    public int getItemCount(){
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_point,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int position){
        name = mList.get(position).getName();
        viewHolder.name.setText(name);

    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String name);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v){
        if (mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v,name);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;

        public ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.map_item_point);

        }
    }
}
