package net.muxi.huashiapp.electricity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;

/**
 * Created by december on 16/7/6.
 */
public class ElectricityDetailFragment extends BaseFragment {

    private String mArea;
    private String mRoom;

    private static final String SCHOOL_AREA = "area";
    private static final String SCHOOL_ROOM = "room";


    public static ElectricityDetailFragment newInstance(String area,String room){
        Bundle args = new Bundle();
        args.putString(SCHOOL_AREA,area);
        args.putString(SCHOOL_ROOM,room);
        ElectricityDetailFragment detailFragment = new ElectricityDetailFragment();
        detailFragment.setArguments(args);
        return detailFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArea = getArguments().getString(SCHOOL_AREA);
        mRoom = getArguments().getString(SCHOOL_ROOM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_electricity_detail,container,false);
        TextView textView1= (TextView)view.findViewById(R.id.title_ec_left);
        TextView textView2= (TextView)view.findViewById(R.id.ec_left);
        TextView textView3= (TextView)view.findViewById(R.id.title__last_month_ec_use);
        TextView textView4= (TextView)view.findViewById(R.id.last_month_ec_use);
        TextView textView5= (TextView)view.findViewById(R.id.title_month_ec_use);
        TextView textView6= (TextView)view.findViewById(R.id.month_ec_use);
        return view;

    }
}
