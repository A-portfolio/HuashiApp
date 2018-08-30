package net.muxi.huashiapp.ui.location;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.muxistudio.appcommon.widgets.BottomDialogFragment;
import com.muxistudio.common.base.BaseFragment;
import com.muxistudio.common.util.Logger;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.ui.location.data.PointDetails;

/**
 * Created by yue on 2018/8/29.
 */

public class BottomFragment extends Fragment {
    private TextView mTvSite;
    private TextView mTvDetail;
    private Button mBtnMore;
    public BottomFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        Logger.i("onCreateView");
        View view = inflater.inflate(R.layout.fragment_map_bottom, null);
        mTvSite= view.findViewById(R.id.map_bottom_site);
        mTvDetail = view.findViewById(R.id.map_bottom_detail);
        mBtnMore = view.findViewById(R.id.map_bottom_more);

        mBtnMore.setOnClickListener((View.OnClickListener)getActivity());

        return view;
    }

    public void setdetail(String site,String info,boolean enable){
        mTvSite.setText(site);
        mTvDetail.setText(info);
        mBtnMore.setEnabled(enable);
    }
    @Override
    public void onPause(){
        super.onPause();
    }
}
