package net.muxi.huashiapp.ui.website;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.WebsiteData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 16/11/2.
 */

public class WebsiteAdapter extends RecyclerView.Adapter<WebsiteAdapter.MySiteViewHolder> {

    private List<WebsiteData> mWebsiteDataList;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void OnItemClick(View view, List<WebsiteData> websiteData, int position);
    }

    public WebsiteAdapter(List<WebsiteData> websiteData) {
        this.mWebsiteDataList = websiteData;
    }

    @Override
    public MySiteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_website, parent, false);
        return new MySiteViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MySiteViewHolder holder, final int position) {
        holder.mWebsiteSite.setText(mWebsiteDataList.get(position).getSite());
        holder.mWebsiteSite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.OnItemClick(v, mWebsiteDataList, position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mWebsiteDataList.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    static class MySiteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.website_site)
        TextView mWebsiteSite;


        public MySiteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
