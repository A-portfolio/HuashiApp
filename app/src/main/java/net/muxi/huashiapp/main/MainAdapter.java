package net.muxi.huashiapp.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.BannerData;
import net.muxi.huashiapp.common.util.FrescoUtil;
import net.muxi.huashiapp.common.util.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 16/4/19.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemClickListener {

    private static final int ITEM_TYPE_BANNER = 0;
    private static final int ITEM_TYPE_COMMON = 1;

    private static final long TURNING_TIME = 4000;

    private int[] mpics;
    private String[] mdesc;
    private List<BannerData> mBannerDatas;
    //图片的地址
    private List<String> imageUrls;

    private ItemClickListener mItemClickListener;
    private OnBannerItemClickListener mOnBannerItemClickListener;

    private Context mContext;
    private ConvenientBanner mConvenientBanner;

    //banner 所占的 item 数量
    private static final int ITEM_BANNER = 1;

    public interface ItemClickListener {
        void OnItemClick(View view, int position);
    }

    public interface OnBannerItemClickListener {
        void onBannerItemClick(BannerData bannerData);
    }

    public MainAdapter(String[] mdesc, int[] mpics, List<BannerData> bannerDatas) {
        this.mdesc = mdesc;
        this.mpics = mpics;
        mBannerDatas = bannerDatas;
        imageUrls = new ArrayList<>();
        for (int i = 0; i < bannerDatas.size(); i++) {
            imageUrls.add(mBannerDatas.get(i).getImg());
        }
    }

    public void swap(List<BannerData> bannerDatas) {
        mBannerDatas.clear();
        mBannerDatas.addAll(bannerDatas);
//        notifyDataSetChanged();
        imageUrls.clear();
        for (int i = 0; i < mBannerDatas.size(); i++) {
            imageUrls.add(mBannerDatas.get(i).getImg());
        }
//        notifyItemChanged(6);
        mConvenientBanner.notifyDataSetChanged();
        Logger.d(mConvenientBanner.isTurning() + "");
        mConvenientBanner.startTurning(TURNING_TIME);
//        mConvenientBanner.startTurning(3000);
    }

    public boolean isBannerPosition(int position) {
        return position == 6 ? true : false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (viewType == ITEM_TYPE_BANNER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
            return new BannerViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
            return new CommonViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Logger.d(position + "");
        if (holder instanceof BannerViewHolder) {
            setupBanner(holder);
        } else if (holder instanceof CommonViewHolder) {
            if (position < 6) {
                ((CommonViewHolder) holder).mImageView.setImageResource(mpics[position]);
                ((CommonViewHolder) holder).mTextView.setText(mdesc[position]);
                ((CommonViewHolder) holder).itemView.setTag(position);
            }else{
                Logger.d(position + "");
                ((CommonViewHolder) holder).mImageView.setImageResource(mpics[position - ITEM_BANNER]);
                ((CommonViewHolder) holder).mTextView.setText(mdesc[position - ITEM_BANNER]);
                ((CommonViewHolder) holder).itemView.setTag(position - ITEM_BANNER);
            }
        }
    }

    private void setupBanner(RecyclerView.ViewHolder holder) {
        ((BannerViewHolder) holder).mBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new FrescoBannerHolder();
            }
        }, imageUrls)
                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .startTurning(TURNING_TIME)
                .setOnItemClickListener(this);

        ((BannerViewHolder) holder).mBanner.setManualPageable(true);
        Logger.d("setup banner");
        mConvenientBanner = ((BannerViewHolder) holder).mBanner;
        for (int i = 0; i < mBannerDatas.size(); i++) {
            FrescoUtil.savePicture(mBannerDatas.get(i).getImg(), mContext, mBannerDatas.get(i).getFilename());
            Logger.d(mBannerDatas.get(i).getImg());
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnBannerItemClickListener(OnBannerItemClickListener bannerItemClickListener) {
        mOnBannerItemClickListener = bannerItemClickListener;
    }

    @Override
    public int getItemCount() {
        Logger.d(mdesc.length + "");
        return mdesc.length + 1;
    }

    @Override
    public void onItemClick(int position) {
        if (mOnBannerItemClickListener != null) {
            mOnBannerItemClickListener.onBannerItemClick(mBannerDatas.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        //第7个 item 为 banner
        Logger.d(position + "");
        return position == 6 ? ITEM_TYPE_BANNER : ITEM_TYPE_COMMON;
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

        @BindView(R.id.banner)
        ConvenientBanner mBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


