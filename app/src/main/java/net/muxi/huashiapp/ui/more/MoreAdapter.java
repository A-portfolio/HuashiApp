package net.muxi.huashiapp.ui.more;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;

import java.util.List;


/**
 * Created by december on 17/2/18.
 */

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.MoreViewHolder> {

    private List<Integer> mIcons;
    private List<String> mContents;


    public ItemClickListener mItemClickListener;


    public interface ItemClickListener {
        void OnItemClick(View view, int position);
    }

    public MoreAdapter(List<String> content, List<Integer> icons) {
        this.mIcons = icons;
        this.mContents = content;
    }


    @Override
    public MoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more, parent, false);
        return new MoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoreViewHolder holder, int position) {
        holder.mItemImg.setImageResource(mIcons.get(position));
        if (position == 6) {
            holder.mItemText.setTextColor(App.sContext.getResources().getColor(R.color.red));
        }
        holder.mItemText.setText(mContents.get(position));
        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.OnItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mContents.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }


    static class MoreViewHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImg;
        private TextView mItemText;
        private RelativeLayout mItemLayout;

        public MoreViewHolder(View itemView) {
            super(itemView);
            mItemImg = (ImageView) itemView.findViewById(R.id.item_img);
            mItemText = (TextView) itemView.findViewById(R.id.item_text);
            mItemLayout = (RelativeLayout) itemView.findViewById(R.id.item_layout);
        }

    }

}
