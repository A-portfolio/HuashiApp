package net.muxi.huashiapp.ui.studyroom;

//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import net.muxi.huashiapp.R;


/**
 * Created by december on 17/2/6.
 */

public class AreaOptionAdapter extends RecyclerView.Adapter<AreaOptionAdapter.MyOptionViewHolder> {


    private String[] mbuildings;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void OnItemClick(View view, String[] buildings,int position);
    }

    public AreaOptionAdapter(String[] buildings) {
        this.mbuildings = buildings;
    }

    @Override
    public MyOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_study_area, parent, false);
        return new MyOptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyOptionViewHolder holder, int position) {
        holder.mStudyAreaOption.setText(mbuildings[position]);
        holder.mStudyAreaOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null){
                    mOnItemClickListener.OnItemClick(v,mbuildings,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mbuildings.length;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    static class MyOptionViewHolder extends RecyclerView.ViewHolder {

        private TextView mStudyAreaOption;

        public MyOptionViewHolder(View itemView) {
            super(itemView);
            mStudyAreaOption = itemView.findViewById(R.id.study_area_option);
        }
    }
}
