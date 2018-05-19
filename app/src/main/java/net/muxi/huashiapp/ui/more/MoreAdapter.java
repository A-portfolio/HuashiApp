package net.muxi.huashiapp.ui.more;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import net.muxi.huashiapp.App;
import net.muxi.huashiapp.R;

/**
 * Created by december on 17/2/18.
 */

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.MoreViewHolder> {

    private List<Integer> mColors;
    private List<Integer> mIcons;
    private List<String> mContents;
    private Context mContext;
    public ItemClickListener mItemClickListener;


    public interface ItemClickListener {
        void OnItemClick(View view, int position);
    }

    public MoreAdapter(List<String> content, List<Integer> icons,List<Integer> colors) {
        this.mIcons = icons;
        this.mColors = colors;
        this.mContents = content;
    }


    @Override
    public MoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more, parent, false);
        return new MoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoreViewHolder holder, int position) {
        holder.mItemImg.setImageResource(mIcons.get(position));
        holder.mItemImg.setColorFilter(ContextCompat.getColor(mContext,mColors.get(position))
        , PorterDuff.Mode.MULTIPLY);
        if (position == 6) {
            holder.mItemText.setTextColor(App.sContext.getResources().getColor(R.color.red));
        }
        holder.mItemText.setText(mContents.get(position));
        holder.mItemLayout.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.OnItemClick(v, position);
                if (!TextUtils.isEmpty(App.sUser.getSid())&&position==6) {
                    holder.mItemImg.setVisibility(View.INVISIBLE);
                    holder.mItemText.setVisibility(View.INVISIBLE);
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
