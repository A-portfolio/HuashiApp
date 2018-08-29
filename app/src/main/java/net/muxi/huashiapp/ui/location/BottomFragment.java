package net.muxi.huashiapp.ui.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.muxistudio.common.base.BaseFragment;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.location.data.PointDetails;

/**
 * Created by yue on 2018/8/29.
 */

public class BottomFragment extends BaseFragment {
    private TextView mTvSite;
    private TextView mTvDetail;
    private Button mBtnMore;

    public static BottomFragment newInstance(PointDetails pointDetails,String detail, boolean more) {

        Bundle args = new Bundle();
        args.putParcelable("point",pointDetails);
        args.putString("site", pointDetails.getName());
        args.putString("detail", detail);
        args.putBoolean("more",more);
        BottomFragment fragment = new BottomFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View view = inflater.inflate(R.layout.fragment_map_bottom, null);
        mTvSite= view.findViewById(R.id.map_bottom_site);
        mTvDetail = view.findViewById(R.id.map_bottom_detail);
        mBtnMore = view.findViewById(R.id.map_bottom_more);

        mTvSite.setText(getArguments().getString("site"));
        mTvDetail.setText(getArguments().getString("detail"));
        mBtnMore.setEnabled(getArguments().getBoolean("more"));

        PointDetails pointDetails = getArguments().getParcelable("point");
        mBtnMore.setOnClickListener(v ->{
            if (getArguments().getBoolean("more")){
                PointDetailActivity.start(getActivity(),pointDetails);
            }
        });

        return view;
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}
