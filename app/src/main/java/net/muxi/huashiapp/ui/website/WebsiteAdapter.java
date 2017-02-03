package net.muxi.huashiapp.ui.website;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.WebsiteData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

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
        if (position % 5 == 0) {
            holder.mWebsiteIcon.setBackgroundResource(R.drawable.ic_add_black_24dp);
            holder.mWebsiteLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_random_blue));
        } else if (position % 5 == 1) {
            holder.mWebsiteIcon.setBackgroundResource(R.drawable.ic_add_black_24dp);
            holder.mWebsiteLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_random_cyan));
        } else if (position % 5 == 2) {
            holder.mWebsiteIcon.setBackgroundResource(R.drawable.ic_add_black_24dp);
            holder.mWebsiteLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_random_red));
        } else if (position % 5 == 3) {
            holder.mWebsiteIcon.setBackgroundResource(R.drawable.ic_add_black_24dp);
            holder.mWebsiteLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_random_purple));
        } else if (position % 5 == 4) {
            holder.mWebsiteIcon.setBackgroundResource(R.drawable.ic_add_black_24dp);
            holder.mWebsiteLayout.setBackground(ContextCompat.getDrawable(context,R.drawable.shape_random_yellow));
        }
        holder.mWebsiteLayout.setOnClickListener(new View.OnClickListener() {
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
        @BindView(R.id.website_icon)
        TextView mWebsiteIcon;
        @BindView(R.id.website_site)
        TextView mWebsiteSite;
        @BindView(R.id.website_layout)
        RelativeLayout mWebsiteLayout;

        public MySiteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
