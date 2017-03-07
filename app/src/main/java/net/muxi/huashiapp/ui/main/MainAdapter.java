package net.muxi.huashiapp.ui.main;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.muxistudio.cardbanner.CardBanner;
import com.muxistudio.cardbanner.ViewHolder;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.BannerData;
import net.muxi.huashiapp.common.data.ItemData;
import net.muxi.huashiapp.util.FrescoUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by december on 16/4/19.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MyItemTouchCallback.ItemTouchAdapter {

    private static final int ITEM_TYPE_BANNER = 0;
    private static final int ITEM_TYPE_COMMON = 1;

    private static final long TURNING_TIME = 4000;

    private List<ItemData> mItems;
    private List<BannerData> mBannerDatas;


    private OnBannerItemClickListener mOnBannerItemClickListener;

    private Context mContext;


    private List<ViewHolder<BannerData>> mViewHolders = new ArrayList<>();


    private List<String> resUrls;


    //banner 所占的 item 数量
    private static final int ITEM_BANNER = 1;

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition == mItems.size() || toPosition == mItems.size()) {
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mItems, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mItems, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

    }

    @Override
    public void onSwiped(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);

    }


    public interface OnBannerItemClickListener {
        void onBannerItemClick(BannerData bannerData);
    }


    public MainAdapter(List<ItemData> items, List<BannerData> bannerDatas) {
        this.mItems = items;
        mBannerDatas = bannerDatas;
        resUrls = new ArrayList<>();
        for (int i = 0; i < bannerDatas.size(); i++) {
            resUrls.add(mBannerDatas.get(i).getImg());
        }
    }

    public MainAdapter(List<ItemData> items) {
        this.mItems = items;
    }

    public void swapBannerData(List<BannerData> bannerDatas) {
        mBannerDatas.clear();
        mBannerDatas.addAll(bannerDatas);
        resUrls.clear();
        for (int i = 0; i < mBannerDatas.size(); i++) {
            resUrls.add(mBannerDatas.get(i).getImg());
        }
    }

    public void swapProduct(List<ItemData> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    public boolean isBannerPosition(int position) {
        return position == 0 ? true : false;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? ITEM_TYPE_BANNER : ITEM_TYPE_COMMON;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BannerViewHolder) {
            for (int i = 0; i < mBannerDatas.size(); i++) {
                ViewHolder<BannerData> viewHolder = new ViewHolder<BannerData>() {
                    @Override
                    public View getView(Context context, BannerData bannerDatas) {
                        SimpleDraweeView simpleDraweeView = new SimpleDraweeView(mContext);
                        simpleDraweeView.setImageURI(Uri.parse(bannerDatas.getImg()));
                        simpleDraweeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return simpleDraweeView;
                    }
                };

                mViewHolders.add(viewHolder);
            }

            ((BannerViewHolder) holder).mCardBanner.setViewHolders(mViewHolders, mBannerDatas);
            ((BannerViewHolder) holder).mCardBanner.setAutoScroll(true);
            ((BannerViewHolder) holder).mCardBanner.setScrollDuration(3000);
            ((BannerViewHolder) holder).mCardBanner.setScrollTime(500);

        } else if (holder instanceof CommonViewHolder) {
            if (position > 0) {
                ((CommonViewHolder) holder).mDraweeView.setImageURI(Uri.parse("res:/" + mItems.get(position).getIcon()));
                ((CommonViewHolder) holder).mTextView.setText(mItems.get(position).getName());
                ((CommonViewHolder) holder).itemView.setTag(position);
            } else {
                if (mItems.get(position).getName().equals("学而")) {
                    ((CommonViewHolder) holder).mDraweeView.setImageURI(Uri.parse(mItems.get(position - ITEM_BANNER).getIcon()));
                    FrescoUtil.savePicture(mItems.get(position - ITEM_BANNER).getIcon(), mContext, mItems.get(position - ITEM_BANNER).getName());
                } else {
                    ((CommonViewHolder) holder).mDraweeView.setImageURI(Uri.parse("res:/" + mItems.get(position - ITEM_BANNER).getIcon()));
                }
                ((CommonViewHolder) holder).mTextView.setText(mItems.get(position - ITEM_BANNER).getName());
                ((CommonViewHolder) holder).itemView.setTag(position - ITEM_BANNER);
            }
        }
//            ((CommonViewHolder) holder).mDraweeView.setImageURI(Uri.parse("res:/" + mItems.get(position).getIcon()));
//            ((CommonViewHolder) holder).mTextView.setText(mItems.get(position).getName());
//            ((CommonViewHolder) holder).itemView.setTag(position);
    }


    public void setOnBannerItemClickListener(OnBannerItemClickListener bannerItemClickListener) {
        mOnBannerItemClickListener = bannerItemClickListener;
    }


    @Override
    public int getItemCount() {
        return mItems.size() + 1;
    }


    public class CommonViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private SimpleDraweeView mDraweeView;
        private RelativeLayout mItemLayout;

        public CommonViewHolder(View itemview) {
            super(itemview);
            mTextView = (TextView) itemview.findViewById(R.id.main_text_view);
            mDraweeView = (SimpleDraweeView) itemview.findViewById(R.id.main_pic);
            mItemLayout = (RelativeLayout) itemview.findViewById(R.id.item_layout);
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        private CardBanner mCardBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            mCardBanner = (CardBanner) itemView.findViewById(R.id.card_banner);
        }
    }

}


