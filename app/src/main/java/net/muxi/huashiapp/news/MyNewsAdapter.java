package net.muxi.huashiapp.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.News;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 16/5/18.
 */
public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.MyNewsViewHolder> {


    private List<News> mNewsList;

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
    public void onBindViewHolder(MyNewsViewHolder holder, int position) {
        holder.mNewsInfoDate.setText(mNewsList.get(position).getDate());
        holder.mNewsInfoTitle.setText(mNewsList.get(position).getTitle());
        holder.mNewsInfoIcon.setText(mNewsList.get(position).getTitle());


    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }


    static class MyNewsViewHolder extends RecyclerView.ViewHolder{
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
