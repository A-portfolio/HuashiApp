package net.muxi.huashiapp.ui.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muxistudio.appcommon.data.News;
import com.muxistudio.common.util.NoDoubleClickUtil;

import net.muxi.huashiapp.R;

import java.util.List;


/**
 * Created by december on 16/5/18.
 */

public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.MyNewsViewHolder> {


    private List<News> mNewsList;

    private OnItemClickListener mOnItemClickListener;


    private void initView(View view) {

    }

    public interface OnItemClickListener {
        void OnItemClick(View view, List<News> newsList, int position);
    }


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
        holder.mNewsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NoDoubleClickUtil.isDoubleClick()) {
                    mOnItemClickListener.OnItemClick(v, mNewsList, position);
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
        private RelativeLayout mNewsLayout;
        private ImageView mNewsInfoIcon;
        private TextView mNewsInfoTitle;
        private TextView mNewsInfoDate;

        public MyNewsViewHolder(View view) {
            super(view);
            mNewsLayout = view.findViewById(R.id.news_layout);
            mNewsInfoIcon = view.findViewById(R.id.news_info_icon);
            mNewsInfoTitle = view.findViewById(R.id.news_info_title);
            mNewsInfoDate = view.findViewById(R.id.news_info_date);
        }


    }
}
