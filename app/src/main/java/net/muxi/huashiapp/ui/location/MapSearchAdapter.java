package net.muxi.huashiapp.ui.location;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.muxistudio.appcommon.data.MapDetailList;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.location.data.PointSearch;

import java.util.List;

/**
 * Created by yue on 2018/8/21.
 */

public class MapSearchAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private List<MapDetailList.PointBean> mList;
    private OnClickTextList onClickTextList;

    public MapSearchAdapter(Context context,List<MapDetailList.PointBean> list,OnClickTextList onClickTextList){
        this.mContext = context;
        this.mList = list;
        this.onClickTextList = onClickTextList;
    }

    @Override
    public int getItemCount(){
        Log.d("COME FROM ADAPTER",mList.size()+"");
        return mList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_point,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MapDetailList.PointBean p = mList.get(position);
        LatLonPoint l = new LatLonPoint(p.getPoints().get(1),p.getPoints().get(0));
        ((ViewHolder)holder).bind(p.getName(),l);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        public ViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.map_item_point);

        }

        public void bind(String s, LatLonPoint l){
            name.setText(s);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickTextList.changeEditText(s,l);
                }
            });
        }
    }
}
