package net.muxi.huashiapp.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.muxi.huashiapp.R;

/**
 * Created by december on 16/4/19.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private enum ITEM_TYPE {
        ITEM_TYPE_BANNER,
        ITEM_TYPE_COMMON
    }

    /**
     * 这里创建数组,接收传过来的数据
     */

    private int[] mpics;
    private String[] mdesc;
    private ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        void OnItemClick(View view, int position);
    }


    public MainAdapter(String[] mdesc, int[] mpics) {
        this.mdesc = mdesc;
        this.mpics = mpics;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_BANNER.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner,parent,false);
            return new BannerViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main,parent,false);
            return new CommonViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    /**
     * 这里返回item数量
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mdesc.length;
    }


    @Override
    public int getItemViewType(int position) {
        //第7个 item 为 banner
        return position == 6 ? ITEM_TYPE.ITEM_TYPE_BANNER.ordinal() : ITEM_TYPE.ITEM_TYPE_COMMON.ordinal();
    }

    /**
     * ViewHolder类，继承RecyclerView.ViewHolder
     */

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public HeaderViewHolder(View itemview) {
            super(itemview);
        }

    }


    public class CommonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextView;
        private ImageView mImageView;

        public CommonViewHolder(View itemview) {
            super(itemview);
            mTextView = (TextView) itemview.findViewById(R.id.main_text_view);
            mImageView = (ImageView) itemview.findViewById(R.id.main_pic);
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.OnItemClick(itemView, getPosition());
            }
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        public BannerViewHolder(View itemView) {
            super(itemView);
        }
    }
}


