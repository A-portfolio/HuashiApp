package net.muxi.huashiapp.common.adapter;

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
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {



    /**
     * 这里创建数组,接收传过来的数据
     *
     */

    private int[] mpics;
    private String[] mdesc;
    private ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        void OnItemClick(View view,int position);
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public MainAdapter(String[] mdesc, int[] mpics){
        this.mdesc = mdesc;
        this.mpics = mpics;


    }
    /**
     * 这里是加载item，并且创建ViewHolder对象，把加载的item(view)传给ViewHolder
     * 第二个参数就是View的类型，根据这个类型去创建不同item的ViewHolder
     * @param parent
     * @param viewType
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main,parent,false);
        MainViewHolder holder = new MainViewHolder(v);
        holder.mImageView = (ImageView) v.findViewById(R.id.main_pic);
        holder.mTextView = (TextView) v.findViewById(R.id.main_text_view);
        return holder;
    }


    /**
     * 这里是给item中的子view绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MainViewHolder) holder).mImageView.setImageResource(mpics[position]);
        ((MainViewHolder) holder).mTextView.setText(mdesc[position]);
        if (mItemClickListener != null) {
            ((MainViewHolder) holder).mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.OnItemClick(v,position);
                }
        });
        }




}

    /**
     * 这里返回item数量
     * @return
     */
    @Override
    public int getItemCount() {
        return mdesc.length;
    }


//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//    }

    /**
     * ViewHolder类，继承RecyclerView.ViewHolder
     */

    public class MainViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        ImageView mImageView;

        MainViewHolder(final View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.main_text_view);
            mImageView = (ImageView) view.findViewById(R.id.main_pic);
        }

    }


}
