package net.muxi.huashiapp.ui.score.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.appcommon.data.Score;

import net.muxi.huashiapp.R;

import java.util.List;

/**
 * author:kolibreath
 */

public class CreditAdapter implements ExpandableListAdapter {

    //List<List<Object>> 是一个二维数组
    private List<List<Score>> mChildCredits;
    private List<String> mCreditType;
    private Context mCtx;
    private List<Double> mGroupCredits;

    public CreditAdapter(Context context,
                         List<List<Score>> mCredits,
                         List<String> mCreditType,
                         List<Double> mGroupCredit){
        this.mGroupCredits = mGroupCredit;
        this.mChildCredits = mCredits;
        this.mCtx = context;
        this.mCreditType = mCreditType;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return mCreditType.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<Score> mCredit = mChildCredits.get(groupPosition);
        return mCredit.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCreditType.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<Score> mCredit = mChildCredits.get(groupPosition);
        return mCredit.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //加载父布局
        View groupView ;
        if(convertView == null)
            groupView = LayoutInflater.from(mCtx).inflate(R.layout.view_credit_group,parent,false);
        else
            groupView = convertView;

        double groupCredit = mGroupCredits.get(groupPosition);
        String groupName = mCreditType.get(groupPosition);

        ImageView mIvGroupView = groupView.findViewById(R.id.iv_group);
        TextView mGroupTextView = groupView.findViewById(R.id.tv_group);
        TextView mTvGroupCredit = groupView.findViewById(R.id.tv_group_credit);

        mTvGroupCredit.setText(String.valueOf(groupCredit));
        mGroupTextView.setText(groupName);

        mIvGroupView.setImageResource(isExpanded? R.drawable.ic_group_selected: R.drawable.ic_group_unselected);

        return groupView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View childView;
        if(convertView == null)
            childView = LayoutInflater.from(mCtx).inflate(R.layout.view_credit_child,parent,false);
        else
            childView = convertView;

        Score credits = (Score) getChild(groupPosition,childPosition);
        TextView mCreditName = childView.findViewById(R.id.tv_course_name);
        TextView mCreditValue = childView.findViewById(R.id.tv_course_credit);

        mCreditName.setText(credits.course);
        mCreditValue.setText(credits.credit);

        return childView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
