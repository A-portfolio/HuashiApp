package net.muxi.huashiapp.electricity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.muxi.huashiapp.R;

/**
 * Created by december on 16/7/6.
 */
public class ElectricityDetailFragment extends Fragment {




    private String marea;
    private String mroom;

    private static final String SCHOOL_AREA = "area";
    private static final String SCHOOL_ROOM = "room";

    public static ElectricityDetailFragment newInstance(String area,String romm) {
        Bundle args = new Bundle();
        args.putString(SCHOOL_AREA,"area");
        args.putString(SCHOOL_ROOM,"room");

        ElectricityDetailFragment fragment = new ElectricityDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marea = getArguments().getString(SCHOOL_AREA);
        mroom = getArguments().getString(SCHOOL_ROOM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_electricity_detail,container,false);
        TextView tv1= (TextView)view.findViewById(R.id.ec_left);
        TextView tv2= (TextView)view.findViewById(R.id.last_month_ec_use);
        TextView tv3= (TextView)view.findViewById(R.id.month_ec_use);
        TextView tv4= (TextView)view.findViewById(R.id.money_left);
        TextView tv5= (TextView)view.findViewById(R.id.month_money_use);
        TextView tv6= (TextView)view.findViewById(R.id.last_month_money_use);
        Button mchange = (Button)view.findViewById(R.id.room_change_button);
        return view;

    }

}
