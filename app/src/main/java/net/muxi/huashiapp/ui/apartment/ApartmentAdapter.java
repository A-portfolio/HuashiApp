package net.muxi.huashiapp.ui.apartment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.data.ApartmentData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by december on 16/7/30.
 */
public class ApartmentAdapter extends RecyclerView.Adapter<ApartmentAdapter.MyApartViewHolder> {

    private List<ApartmentData> mApartmentDataList;

    public ApartmentAdapter(List<ApartmentData> dataList) {
        this.mApartmentDataList = dataList;
    }

    @Override
    public MyApartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apartment, parent, false);
        return new MyApartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyApartViewHolder holder, int position) {
        holder.mTvApartment.setText(mApartmentDataList.get(position).getApartment());
        String phone = new String();
        for (String phoneStr : mApartmentDataList.get(position).getPhone()){
            phone = phone + phoneStr + " ";
        }
        holder.mTvPhone.setText(phone);
        holder.mTvPlace.setText(mApartmentDataList.get(position).getPlace());

    }

    @Override
    public int getItemCount() {
        return mApartmentDataList.size();
    }


    static class MyApartViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_apartment)
        TextView mTvApartment;
        @BindView(R.id.tv_phone)
        TextView mTvPhone;
        @BindView(R.id.tv_place)
        TextView mTvPlace;

       public MyApartViewHolder(View view) {
           super(view);
           ButterKnife.bind(this, view);
        }
    }
}
