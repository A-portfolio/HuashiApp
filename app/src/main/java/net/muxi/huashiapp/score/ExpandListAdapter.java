package net.muxi.huashiapp.score;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by ybao on 16/6/18.
 */
public class ExpandListAdapter extends BaseExpandableListAdapter{

    private String[] selectGroups = {"请选择学年","请选择学期"};
    private String[][] selectItems = {
            {"2016","2015","2014","2013"},
            {"1","2","3"}
    };
    private Context mContext;

    public ExpandListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return selectGroups.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return selectItems[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return selectGroups[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return selectItems[groupPosition][childPosition];
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = getTextView();
        textView.setText(selectItems[groupPosition][childPosition]);
        return textView;
    }

    private TextView getTextView(){
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.LEFT);
        textView.setLayoutParams(params);
        return textView;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView = getTextView();
        textView.setText(selectGroups[groupPosition]);
        return textView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
}
