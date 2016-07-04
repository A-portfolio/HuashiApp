package net.muxi.huashiapp.electricity;


import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import net.muxi.huashiapp.R;

/**
 * Created by december on 16/6/29.
 */
public class MyExpandableAdapter extends BaseExpandableListAdapter{

    public String[] groupStrings = {"区域","建筑","楼层","房间"};

    public String[][] childStrings ={
            {"西区学生宿舍","东区学生宿舍","元宝山学生宿舍","南湖学生宿舍","公共教学楼"},
            {"东11","东3","东2","东1",}
    };

    private Context mContext;

    public MyExpandableAdapter(Context context) {
        mContext = context;
    }



    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    //获取分组个数
    @Override
    public int getGroupCount() {
        return groupStrings.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childStrings[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupStrings[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childStrings[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_expand_group, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_group);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupStrings[groupPosition]);
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_expand_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_child);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder)convertView.getTag();
        }
        childViewHolder.tvTitle.setText(childStrings[groupPosition][childPosition]);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    class GroupViewHolder {
        TextView tvTitle;
    }
    class ChildViewHolder {
        TextView tvTitle;
    }

}
