package net.muxi.huashiapp.common.adapter;

import android.content.Context;
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
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    public static enum ITEM_TYPE {
        ITEM_TYPE_IMAGE,
        ITEM_TYPE_TEXT;
    }


    /**
     * 这里创建数组,接收传过来的数据
     *
     */
    private String[] mTitles;

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;


    public RecyclerViewAdapter(Context context){
        mTitles = context.getResources().getStringArray(R.array.titles);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 这里是加载item，并且创建ViewHolder对象，把加载的item(view)传给ViewHolder
     * 第二个参数就是View的类型，根据这个类型去创建不同item的ViewHolder
     * @param parent
     * @param viewType
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal()) {
            return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_image, parent, false));
        } else {
            return new TextViewHolder(mLayoutInflater.inflate(R.layout.item_text, parent, false));
        }
    }


    /**
     * 这里是给item中的子view绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextViewHolder){
            ((TextViewHolder) holder).mTextView.setText(mTitles[position]);
        }else if (holder instanceof ImageViewHolder){
            ((ImageViewHolder) holder).mTextView.setText(mTitles[position]);
        }
    }


    /**
     * 这里返回item数量
     * @return
     */
    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.length;
    }

    @Override
    public int getItemViewType(int position) {
        return  position % 1 == 0 ? ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal() : ITEM_TYPE.ITEM_TYPE_TEXT.ordinal();

    }

    /**
     * ViewHolder类，继承RecyclerView.ViewHolder
     */
    public static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        TextViewHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text_view);
        }

    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        ImageView mImageView;

        ImageViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text_view);
            mImageView = (ImageView) view.findViewById(R.id.image_view);

        }

    }


}
