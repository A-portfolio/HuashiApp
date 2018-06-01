package net.muxi.huashiapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.muxistudio.appcommon.data.BannerData;
import com.muxistudio.appcommon.data.Hint;
import com.muxistudio.appcommon.data.ItemData;
import com.muxistudio.appcommon.utils.FrescoUtil;
import com.muxistudio.cardbanner.CardBanner;
import com.muxistudio.cardbanner.ViewHolder;
import com.muxistudio.common.util.DimensUtil;
import com.muxistudio.common.util.Logger;
import com.muxistudio.common.util.PreferenceUtil;

import net.muxi.huashiapp.BuildConfig;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.webview.WebViewActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by december on 16/4/19.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements MyItemTouchCallback.ItemTouchAdapter {

    private static final int ITEM_TYPE_HINT = 0;
    private static final int ITEM_TYPE_BANNER = 1;
    private static final int ITEM_TYPE_COMMON = 2;
    private static final int ITEM_TYPE_FOOTER = 3;
    private static final long TURNING_TIME = 4000;
    private static final int UPDATE_TIME = PreferenceUtil.getInt(PreferenceUtil.HINT_UPDATE_TIME);
    private Hint hint;
    private List<ItemData> mItemDatas;
    private List<BannerData> mBannerDatas;
    private OnBannerItemClickListener mOnBannerItemClickListener;
    private Context mContext;
    private List<ViewHolder<BannerData>> mViewHolders = new ArrayList<>();
    private List<String> resUrls;


    //banner 和 error（hint） 一共所占的 item 数量
    public static int ITEM = 1;



    public interface OnBannerItemClickListener {
        void onBannerItemClick(BannerData bannerData);
    }


    public MainAdapter(List<ItemData> items, List<BannerData> bannerDatas, Hint hint) {
        this.mItemDatas = items;
        this.hint = hint;
        mBannerDatas = bannerDatas;
        Comparator<BannerData> comparator = (o1, o2) -> {
            return Integer.parseInt(o1.getNum()) - Integer.parseInt(o2.getNum());
        };
        Logger.d("bannder data is null? " + (mBannerDatas == null) + " comparator is null" + (comparator == null));
        Collections.sort(mBannerDatas, comparator);
        resUrls = new ArrayList<>();
        for (int i = 0; i < bannerDatas.size(); i++) {
            resUrls.add(mBannerDatas.get(i).getImg());
        }
    }

    public void swapBannerData(List<BannerData> bannerDatas) {
        mBannerDatas.clear();
        mBannerDatas.addAll(bannerDatas);
        resUrls.clear();
        for (int i = 0; i < mBannerDatas.size(); i++) {
            resUrls.add(mBannerDatas.get(i).getImg());
        }
    }

    public void swapItems(List<ItemData> items) {
        this.mItemDatas = items;
        notifyDataSetChanged();
    }

    public boolean isBannerPosition(int position) {
        if (isHintShown()) {
            return position == 1;
        } else {
            return position == 0;
        }
    }

    public boolean isHintPosition(int position) {
        if (isHintShown()) {
            return position == 0;
        } else {
            return false;
        }
    }

    public boolean isFooterPosition(int position) {
        if (isHintShown()) {
            return position == mItemDatas.size() + 2;
        } else {
            return position == mItemDatas.size() + 1;
        }
    }

    public void setHint(Hint hint) {
        this.hint = hint;
    }

    //原来mItemData有十一个元素 加上banner和footer 一共 13个元素 position的范围是0-12
    // 现在加上了一个hint 隐藏的提示元素 ： 一共 9+2+1=13
    //如果出现则是13 不出现则是12
    @Override
    public int getItemViewType(int position) {
        if (isHintShown()) {
            if (position == 0) {
                ITEM = 2;
                return ITEM_TYPE_HINT;
            } else if (position == 1) {
                return ITEM_TYPE_BANNER;
            } else if (position == mItemDatas.size() + 2) {
                return ITEM_TYPE_FOOTER;
            } else {
                return ITEM_TYPE_COMMON;
            }
        } else {
            if (position == 0) {
                return ITEM_TYPE_BANNER;
            } else if (position == mItemDatas.size() + 1) {
                return ITEM_TYPE_FOOTER;
            } else {
                return ITEM_TYPE_COMMON;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        if (viewType == ITEM_TYPE_HINT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_hint, parent, false);
            return new HintViewHolder(view);
        } else if (viewType == ITEM_TYPE_BANNER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false);
            return new BannerViewHolder(view);
        } else if (viewType == ITEM_TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_footer, parent, false);
            return new FooterViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
            return new CommonViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HintViewHolder) {
            ((HintViewHolder) holder).mRlHintContent.setOnClickListener(v -> {
                mContext.startActivity(new Intent(mContext, DetailActivity.class)
                        .putExtra("detail", hint.getDetail()));
            });
            ((HintViewHolder) holder).mTvHintMsg.setText(hint.getMsg());
            ((HintViewHolder) holder).mIvHintClose.setOnClickListener(v -> {
                ((HintViewHolder) holder).mRlHintContent.setVisibility(View.GONE);
                PreferenceUtil.saveInt(PreferenceUtil.HINT_UPDATE_TIME, hint.getUpdate());
                ITEM = 1;
            });
            if (hint.getType().equals("err")) {
                ((HintViewHolder) holder).mRlHintContent
                        .setBackgroundColor(mContext.getResources().getColor(R.color.red));
            } else {
                ((HintViewHolder) holder).mRlHintContent
                        .setBackgroundColor(mContext.getResources().getColor(R.color.hint_yellow));
            }
        } else if (holder instanceof BannerViewHolder) {
            for (int i = 0; i < mBannerDatas.size(); i++) {
                ViewHolder<BannerData> viewHolder = (context, bannerDatas) -> {
                    SimpleDraweeView simpleDraweeView = new SimpleDraweeView(mContext);
                    simpleDraweeView.setImageURI(Uri.parse(bannerDatas.getImg()));
                    simpleDraweeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    simpleDraweeView.setOnClickListener(v -> {
                        Intent intent = WebViewActivity.newIntent(mContext, bannerDatas.getUrl());
                        mContext.startActivity(intent);
                    });
                    return simpleDraweeView;
                };
                mViewHolders.add(viewHolder);
            }
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            ViewGroup.LayoutParams lp = bannerViewHolder.mCardBanner.getLayoutParams();
            //banner 按宽高比布局
            lp.height = (DimensUtil.getScreenWidth() - DimensUtil.dp2px(32)) * 300 / 750;
            ((BannerViewHolder) holder).mCardBanner.setViewHolders(mViewHolders, mBannerDatas);
            ((BannerViewHolder) holder).mCardBanner.setAutoScroll(true);
            ((BannerViewHolder) holder).mCardBanner.setScrollDuration(3000);
            ((BannerViewHolder) holder).mCardBanner.setScrollTime(500);
        } else if (holder instanceof CommonViewHolder) {
            //如果是动态获取的图片 就需要网络加载资源
            if (mItemDatas.get(position - ITEM).isDynamic()) {
                ((CommonViewHolder) holder).mDraweeView.setImageURI(Uri.parse(mItemDatas.get(position - ITEM).getIcon()));
                FrescoUtil.savePicture(mItemDatas.get(position - ITEM).getIcon(), mContext, mItemDatas.get(position - ITEM).getName());
            } else {
                if (position > 0 && position != mItemDatas.size() + ITEM) {
                    ((CommonViewHolder) holder).mDraweeView.setImageURI(Uri.parse("res:/" +
                            mItemDatas.get(position - ITEM).getIcon()));
                    ((CommonViewHolder) holder).mTextView.setText(mItemDatas.get(position - ITEM).getName());
                    ((CommonViewHolder) holder).itemView.setTag(position - ITEM);
                }
            }
            ((CommonViewHolder) holder).mTextView.setText(mItemDatas.get(position - ITEM).getName());
            ((CommonViewHolder) holder).itemView.setTag(position - ITEM);
        }
    }

    @Override
    public int getItemCount() {
        return mItemDatas.size() + 2;
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition == mItemDatas.size() || toPosition == mItemDatas.size()) {
            return;
        } else if (fromPosition == mItemDatas.size() + 1 || toPosition == mItemDatas.size() + 1) {
            return;
        } else if (fromPosition == 0 || toPosition == 0) {
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mItemDatas, i - 1, i);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mItemDatas, i - 1, i - 2);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        mItemDatas.remove(position);
        notifyItemRemoved(position);

    }

    //判断有没有hint信息的包装函数
    public boolean isHintShown() {
        boolean b = UPDATE_TIME != hint.getUpdate();
        boolean a = BuildConfig.VERSION_NAME.equals(hint.getVersion());
        return !TextUtils.isEmpty(hint.getMsg()) && a && b;
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
            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) mDraweeView.getLayoutParams();
            layoutParams.width = DimensUtil.getScreenWidth() / 3;
            mDraweeView.setLayoutParams(layoutParams);
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        public CardBanner mCardBanner;

        public BannerViewHolder(View itemView) {
            super(itemView);
            mCardBanner = (CardBanner) itemView.findViewById(R.id.card_banner);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_footer);
        }
    }

    public class HintViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mRlHintContent;
        private TextView mTvHintMsg;
        private ImageView mIvHintClose;

        public HintViewHolder(View itemView) {
            super(itemView);
            mRlHintContent = itemView.findViewById(R.id.rl_hint_content);
            mTvHintMsg = itemView.findViewById(R.id.tv_hint_msg);
            mIvHintClose = itemView.findViewById(R.id.iv_hint_close);
        }
    }

}


