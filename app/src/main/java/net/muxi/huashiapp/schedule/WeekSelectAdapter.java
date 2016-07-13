package net.muxi.huashiapp.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.muxi.huashiapp.AppConstants;
import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.util.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ybao on 16/7/11.
 */
public class WeekSelectAdapter extends RecyclerView.Adapter<WeekSelectAdapter.ViewHolder> {


    //此处为实际的当前周,不以 0 开始
    private int curWeek;
    private OnItemClickListener mOnItemClickListener;

    public WeekSelectAdapter(int curWeek) {
        super();
        this.curWeek = curWeek;
        Logger.d(curWeek + "");
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if ((position + 1) / 10 < 0) {
            holder.mTvNumber.setText("0" + (position + 1));
        } else {
            holder.mTvNumber.setText("" + (position + 1));
        }
        setTvNumberBg(position,holder.mTvNumber);

        holder.mTvWeek.setText(AppConstants.WEEKS[position]);
        if ((position + 1) == curWeek) {
            holder.mTvCurweek.setVisibility(View.VISIBLE);
            holder.mTvCurweek.setText("(当前周)");
        }
        holder.mRootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(position);
            }
        });
    }

    private void setTvNumberBg(int position,TextView tv) {
        switch (position % 4){
            case 0:
                tv.setBackgroundResource(R.drawable.shape_round_orange);
                break;
            case 1:
                tv.setBackgroundResource(R.drawable.shape_round_pink);
                break;
            case 2:
                tv.setBackgroundResource(R.drawable.shape_round_green);
                break;
            case 3:
                tv.setBackgroundResource(R.drawable.shape_round_purple);
                break;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weeks, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return 18;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_number)
        TextView mTvNumber;
        @BindView(R.id.tv_week)
        TextView mTvWeek;
        @BindView(R.id.tv_curweek)
        TextView mTvCurweek;
        @BindView(R.id.root_layout)
        LinearLayout mRootLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
