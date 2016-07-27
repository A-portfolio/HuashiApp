package net.muxi.huashiapp.electricity;


import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import net.muxi.huashiapp.R;

/**
 * Created by december on 16/6/29.
 */
public class MyExpandableAdapter extends BaseExpandableListAdapter{

    public String[] groupStrings = {"区域","建筑"};

    public String[][] childStrings ={
            {"西区","东区","元宝山","南湖"},
            {"西区1栋","西区2栋","西区3栋","西区4栋","西区5栋","西区6栋","西区7栋","西区8栋",
             "东区1栋","东区2栋","东区3栋","东区4栋","东区5栋","东区6栋","东区7栋","东区8栋","东区9栋","东区10栋","东区11栋",
                    "东区12栋","东区13栋","东区14栋","东区15栋","东区16栋",
             "元宝山1栋","元宝山2栋","元宝山3栋","元宝山4栋","元宝山5栋"
            }
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
            childViewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder)convertView.getTag();
        }
        childViewHolder.tvTitle.setText(childStrings[groupPosition][childPosition]);
//        childViewHolder.mCheckBox.setText(childStrings[groupPosition][childPosition]);
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
        CheckBox mCheckBox;
    }

}
