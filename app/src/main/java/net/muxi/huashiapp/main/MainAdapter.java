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
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>  {



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
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main,parent,false);
        MainViewHolder holder = new MainViewHolder(itemview);
        return holder;
    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    /**
     * 这里是给item中的子view绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        holder.mImageView.setImageResource(mpics[position]);
        holder.mTextView.setText(mdesc[position]);
        holder.itemView.setTag(position);

}


    /**
     * 这里返回item数量
     * @return
     */
    @Override
    public int getItemCount() {
        return mdesc.length;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * ViewHolder类，继承RecyclerView.ViewHolder
     */

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTextView;
        private ImageView mImageView;
        public MainViewHolder(View itemview) {
            super(itemview);
            mTextView = (TextView) itemview.findViewById(R.id.main_text_view);
            mImageView = (ImageView) itemview.findViewById(R.id.main_pic);
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null ){
                mItemClickListener.OnItemClick(itemView,getPosition());
            }
        }
    }
}


