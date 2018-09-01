package net.muxi.huashiapp.ui.location;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.muxistudio.appcommon.data.MapDetailList;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.SuggestionActivity;

import java.util.List;

/**
 * Created by yue on 2018/8/21.
 */

public class MapSearchAdapter extends RecyclerView.Adapter{

    private static final int TYPE_CONTENT = 0;
    private static final int TYPE_BOTTOM = 1;
    private Context mContext;
    private List<MapDetailList.PointsBean> mList;
    private OnClickTextList onClickTextList;
    private LayoutInflater mInflater;
    private int mFooterCount = 1;

    public MapSearchAdapter(Context context,List<MapDetailList.PointsBean> list,OnClickTextList onClickTextList){
        this.mContext = context;
        this.mList = list;
        this.onClickTextList = onClickTextList;
        mInflater = LayoutInflater.from(context);
    }

    public boolean isBottom(int position){
        return mFooterCount != 0 && position >= getContentItemCount();
    }

    private int getContentItemCount(){
        return mList.size();
    }

    @Override
    public int getItemCount(){
        Log.d("COME FROM ADAPTER",mList.size()+"");
        return mList.size()+mFooterCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (isBottom(position)) {
            return TYPE_BOTTOM;
        } else {
            return TYPE_CONTENT;
        }
    }

    //fixme return null !!!!!????
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if (viewType == TYPE_CONTENT) {
            return new ContentViewHolder(mInflater.inflate(R.layout.item_map_point, parent, false));
        }else if (viewType == TYPE_BOTTOM){
            return new FooterViewHolder(mInflater.inflate(R.layout.view_map_footer, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            MapDetailList.PointsBean p = mList.get(position);
            LatLonPoint l = new LatLonPoint(p.getPoints().get(0), p.getPoints().get(1));
            ((ContentViewHolder) holder).bind(p.getName(), l);
        }else if (holder instanceof FooterViewHolder){

        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        public ContentViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.map_item_point);

        }

        public void bind(String s, LatLonPoint l){
            name.setText(s);
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickTextList.onClickText(s,l);
                }
            });
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder{
        private TextView footer;
        public FooterViewHolder(View itemView){
            super(itemView);
            footer = itemView.findViewById(R.id.map_footer);
            footer.setOnClickListener( v -> {
                SuggestionActivity.start(itemView.getContext());
            });
        }
    }
}
