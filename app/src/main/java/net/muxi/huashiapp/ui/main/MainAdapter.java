package net.muxi.huashiapp.ui.main;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.BannerData;
import net.muxi.huashiapp.ui.credit.SelectCreditActivity;
import net.muxi.huashiapp.ui.library.LibrarySearchResultActivity;
import net.muxi.huashiapp.ui.score.ScoreSelectActivity;
import net.muxi.huashiapp.util.FrescoUtil;
import net.muxi.huashiapp.util.Logger;

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

    private List<String> mpics;
    private List<String> mdesc;
    private List<Integer> mIcons;
    private List<BannerData> mBannerDatas;
    //图片的地址
    private List<String> imageUrls;

    private ItemClickListener mItemClickListener;
    private OnBannerItemClickListener mOnBannerItemClickListener;

    private Context mContext;
    private ConvenientBanner mConvenientBanner;

    //banner 所占的 item 数量
    private static final int ITEM_BANNER = 1;
    //webview item 的位置
    private static final int WEB_POSITON = 9;

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnBannerItemClickListener {
        void onBannerItemClick(BannerData bannerData);
    }

    public MainAdapter(List<String> pics, List<String> desc, List<BannerData> bannerDatas) {
        this.mdesc = desc;
        this.mpics = pics;
        mBannerDatas = bannerDatas;
        imageUrls = new ArrayList<>();
        for (int i = 0; i < bannerDatas.size(); i++) {
            imageUrls.add(mBannerDatas.get(i).getImg());
        }
    }

    public MainAdapter(List<String> title, List<Integer> pic) {
        this.mdesc = title;
        this.mIcons = pic;
    }

    public void swapBannerData(List<BannerData> bannerDatas) {
        mBannerDatas.clear();
        mBannerDatas.addAll(bannerDatas);
        imageUrls.clear();
        for (int i = 0; i < mBannerDatas.size(); i++) {
            imageUrls.add(mBannerDatas.get(i).getImg());
        }
        mConvenientBanner.notifyDataSetChanged();
        Logger.d(mConvenientBanner.isTurning() + "");
    }

    public void swapProduct(List<String> pics, List<String> desc) {
        mpics = pics;
        mdesc = desc;
        notifyDataSetChanged();
    }

    public boolean isBannerPosition(int position) {
        return position == 6 ? true : false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
//        if (viewType == ITEM_TYPE_BANNER) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
//            return new BannerViewHolder(view);
//        } else {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
//            return new CommonViewHolder(view);
//        }

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main,parent,false);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        if (holder instanceof BannerViewHolder) {
//            setupBanner(holder);
//        } else if (holder instanceof CommonViewHolder) {
//            if (position < 6) {
//                ((CommonViewHolder) holder).mDraweeView.setImageURI(Uri.parse("res:/" + mpics.get(position)));
//                ((CommonViewHolder) holder).mTextView.setText(mdesc.get(position));
//                ((CommonViewHolder) holder).itemView.setTag(position);
//            } else {
//                if (position >= WEB_POSITON) {
//                    ((CommonViewHolder) holder).mDraweeView.setImageURI(Uri.parse(mpics.get(position - ITEM_BANNER)));
//                    FrescoUtil.savePicture(mpics.get(position - ITEM_BANNER),mContext,mdesc.get(position - ITEM_BANNER));
//                } else {
//                    ((CommonViewHolder) holder).mDraweeView.setImageURI(Uri.parse("res:/" + mpics.get(position - ITEM_BANNER)));
//                }
//                ((CommonViewHolder) holder).mTextView.setText(mdesc.get(position - ITEM_BANNER));
//                ((CommonViewHolder) holder).itemView.setTag(position - ITEM_BANNER);
//            }
//        }
        ((CommonViewHolder)holder).mDraweeView.setImageURI(Uri.parse("res:/" + mIcons.get(position)));
        ((CommonViewHolder)holder).mTextView.setText(mdesc.get(position));
        ((CommonViewHolder)holder).itemView.setTag(position);

        ((CommonViewHolder)holder).mItemLayout.setOnClickListener(v -> {
            Logger.d(position + "");
            switch (position){
                case 4:
                    SelectCreditActivity.start(mContext);
                    break;
                case 0:
                    ScoreSelectActivity.start(mContext);
                    break;
//                case 1:
//                    LibrarySearchResultActivity.start(mContext);
//                    break;
            }
        });
    }

    private void setupBanner(RecyclerView.ViewHolder holder) {
        ((BannerViewHolder) holder).mBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new FrescoBannerHolder();
            }
        }, imageUrls)
                .setPageIndicator(new int[]{R.drawable.ic_add_black_24dp, R.drawable.ic_add_black_24dp})
                .setOnItemClickListener(this);

        ((BannerViewHolder) holder).mBanner.startTurning(TURNING_TIME);
        ((BannerViewHolder) holder).mBanner.setManualPageable(true);
        mConvenientBanner = ((BannerViewHolder) holder).mBanner;
        for (int i = 0; i < mBannerDatas.size(); i++) {
            FrescoUtil.savePicture(mBannerDatas.get(i).getImg(), mContext, mBannerDatas.get(i).getFilename());
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
        return mdesc.size();
    }

    @Override
    public void onItemClick(int position) {
        if (mOnBannerItemClickListener != null) {
            mOnBannerItemClickListener.onBannerItemClick(mBannerDatas.get(position));
        }
    }

    public class CommonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextView;
        private SimpleDraweeView mDraweeView;
        private RelativeLayout mItemLayout;

        public CommonViewHolder(View itemview) {
            super(itemview);
            mTextView = (TextView) itemview.findViewById(R.id.main_text_view);
            mDraweeView = (SimpleDraweeView) itemview.findViewById(R.id.main_pic);
            mItemLayout = (RelativeLayout) itemview.findViewById(R.id.item_layout);
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
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


