package net.muxi.huashiapp.ui.score;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import net.muxi.huashiapp.R;


/**
 * Created by ybao on 16/6/26.
 */
public class ScoreSearchAdapter extends BaseAdapter{

    private String[] times;
    private Context mContext;

    public ScoreSearchAdapter(Context context,String[] times) {
        this.times = times;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_score_search,parent,false);
        TextView yearTv = (TextView)view.findViewById(R.id.tv_time);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        yearTv.setText(times[position]);
        return view;
    }

    @Override
    public int getCount() {
        return times.length;
    }

    @Override
    public Object getItem(int position) {
        return times[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
