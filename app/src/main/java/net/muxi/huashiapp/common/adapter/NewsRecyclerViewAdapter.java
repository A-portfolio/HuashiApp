package net.muxi.huashiapp.common.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.ui.DetailActivity;
import net.muxi.huashiapp.common.util.News;

import java.util.List;

/**
 * Created by december on 16/4/27.
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder> {
    private List<News> mNewses;
    private Context mContext;

    public NewsRecyclerViewAdapter(List<News> mNewses,Context mContext)
    {

        this.mNewses = mNewses;
        this.mContext = mContext;
    }

    //自定义ViewHolder
    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView ;
        ImageView mnews_photo;
        TextView mnews_title;
        TextView mnews_desc;
        Button mButton;

        public NewsViewHolder(final View itemView){
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.news_card_view);
            mnews_title= (TextView) itemView.findViewById(R.id.news_title);
            mnews_photo = (ImageView) itemView.findViewById(R.id.news_photo);
            mnews_desc = (TextView) itemView.findViewById(R.id.news_desc);
            mButton = (Button) itemView.findViewById(R.id.btn_more);
            //设置TextView背景为透明
            mnews_title.setBackgroundColor(Color.argb(20, 0, 0, 0));
        }
    }

    @Override
    public NewsRecyclerViewAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_news,parent,false);
        NewsViewHolder nvh = new NewsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(NewsRecyclerViewAdapter.NewsViewHolder holder, int position) {
        final int j = position;
        holder.mnews_title.setText(mNewses.get(position).getTitle());
        holder.mnews_photo.setImageResource(mNewses.get(position).getPhotoId());
        holder.mnews_desc.setText(mNewses.get(position).getDesc());

        //为CardView,Button设置跳转事件
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(mContext, DetailActivity.class);
                intent.putExtra("News",mNewses.get(j));
                mContext.startActivity(intent);

            }
        });

        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(mContext,DetailActivity.class);
                intent.putExtra("News",mNewses.get(j));
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mNewses.size();
    }
}
