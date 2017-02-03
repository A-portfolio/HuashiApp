package net.muxi.huashiapp.ui.news;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.News;
import net.muxi.huashiapp.util.NoDoubleClickUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 16/5/18.
 */
public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.MyNewsViewHolder> {



    private List<News> mNewsList;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void OnItemClick(View view, List<News> newsList,int position);
    }

//    List<News> newsList

    public MyNewsAdapter(List<News> newsList) {
        super();
        mNewsList = newsList;
    }

    @Override
    public MyNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new MyNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyNewsViewHolder holder, final int position) {
        holder.mNewsInfoDate.setText(mNewsList.get(position).getDate());
        holder.mNewsInfoTitle.setText(mNewsList.get(position).getTitle());
        holder.mNewsInfoIcon.setText(mNewsList.get(position).getTitle().substring(0, 1));
        holder.mNewsInfoIcon.setTextColor(Color.WHITE);
        if (position % 4 == 0){
            holder.mNewsInfoIcon.setBackgroundResource(R.drawable.shape_round_green);
        }else if (position % 4 == 1){
            holder.mNewsInfoIcon.setBackgroundResource(R.drawable.shape_round_orange);
        }else if (position % 4 == 2){
            holder.mNewsInfoIcon.setBackgroundResource(R.drawable.shape_round_pink);
        }else {
            holder.mNewsInfoIcon.setBackgroundResource(R.drawable.shape_round_purple);
        }
        holder.mNewsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NoDoubleClickUtil.isDoubleClick()) {
                        mOnItemClickListener.OnItemClick(v,mNewsList,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    static class MyNewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_layout)
        RelativeLayout mNewsLayout;
        @BindView(R.id.news_info_icon)
        TextView mNewsInfoIcon;
        @BindView(R.id.news_info_title)
        TextView mNewsInfoTitle;
        @BindView(R.id.news_info_date)
        TextView mNewsInfoDate;

        public MyNewsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


    }
}
