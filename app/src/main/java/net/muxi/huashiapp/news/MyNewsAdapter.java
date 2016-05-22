package net.muxi.huashiapp.news;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.muxi.huashiapp.R;

import java.util.List;

/**
 * Created by december on 16/5/18.
 */
public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.MyNewsViewHolder> {

    private List<String> mDatas;

    public MyNewsAdapter(List<String> mDatas){
        this.mDatas = mDatas;
    }

    @Override
    public MyNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        MyNewsViewHolder holder = new MyNewsViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyNewsViewHolder holder, int position) {
        holder.mTextView.setText(mDatas.get(position));


    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class MyNewsViewHolder extends RecyclerView.ViewHolder{

        private TextView mTextView;

        public MyNewsViewHolder(View itemView){
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.news_text_view);
        }



    }
}
