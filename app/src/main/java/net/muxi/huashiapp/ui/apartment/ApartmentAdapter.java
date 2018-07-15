package net.muxi.huashiapp.ui.apartment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.muxistudio.appcommon.data.ApartmentData;

import net.muxi.huashiapp.R;

import java.util.List;


/**
 * Created by december on 16/7/30.
 */
public class ApartmentAdapter
        extends RecyclerView.Adapter<ApartmentAdapter.MyApartViewHolder> {

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
        for (String phoneStr : mApartmentDataList.get(position).getPhone()) {
            phone = phone + phoneStr + " ";
        }
        holder.mTvPhone.setText(phone);
        holder.mTvPlace.setText(mApartmentDataList.get(position).getPlace());

    }

    @Override
    public int getItemCount() {
        return mApartmentDataList.size();
    }

    //holder 只是用来绑定控件对象
    static class MyApartViewHolder extends RecyclerView.ViewHolder {

        private CardView mApartmentLayout;
        private TextView mTvApartment;
        private View mLine;
        private ImageView mIcPhone;
        private TextView mTvPhone;
        private ImageView mIcPosition;
        private TextView mTvPlace;

        public MyApartViewHolder(View view) {
            super(view);
            mApartmentLayout = view.findViewById(R.id.apartment_layout);
            mTvApartment = view.findViewById(R.id.tv_apartment);
            mLine = view.findViewById(R.id.line);
            mIcPhone = view.findViewById(R.id.ic_phone);
            mTvPhone = view.findViewById(R.id.tv_phone);
            mIcPosition = view.findViewById(R.id.ic_position);
            mTvPlace = view.findViewById(R.id.tv_place);
        }
    }
}
