package net.muxi.huashiapp.ui.electricity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.muxi.huashiapp.R;
import net.muxi.huashiapp.common.base.BaseFragment;
import net.muxi.huashiapp.common.data.Electricity;

/**
 * Created by december on 16/7/6.
 */
public class ElectricityDetailFragment extends BaseFragment {


//    @BindView(R.id.card_money_left)
//    CardView mCardMoneyLeft;
//    @BindView(R.id.card_degree_left)
//    CardView mCardDegreeLeft;
//    @BindView(R.id.card_total_use)
//    CardView mCardTotalUse;

    private TextView mTvDegreeLeft;
    private TextView mTvDegreeLastMonth;
    private TextView mTvDegreeCurMonth;
    private TextView mTvMoneyLeft;
    private TextView mTvMoneyLastMonth;
    private TextView mTvMoneyCurMonth;

    private CardView mCardMoneyLeft;
    private CardView mCardDegreeLeft;
    private CardView mCardTotalUse;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_electricity_detail, container, false);
        mTvDegreeLeft = (TextView) view.findViewById(R.id.tv_degree_left);
        mTvDegreeLastMonth = (TextView) view.findViewById(R.id.tv_degree_last_month);
        mTvDegreeCurMonth = (TextView) view.findViewById(R.id.tv_degree_cur_month);
        mTvMoneyLeft = (TextView) view.findViewById(R.id.tv_money_left);
        mTvMoneyLastMonth = (TextView) view.findViewById(R.id.tv_money_last_month);
        mTvMoneyCurMonth = (TextView) view.findViewById(R.id.tv_money_cur_month);

        mCardMoneyLeft = (CardView) view.findViewById(R.id.card_money_left);
        mCardDegreeLeft = (CardView) view.findViewById(R.id.card_degree_left);
        mCardTotalUse = (CardView) view.findViewById(R.id.card_total_use);


        return view;
    }


    public void setCardColor(int position) {
        if (isAdded()) {
            if (position == 0) {
                mCardMoneyLeft.setCardBackgroundColor(getResources().getColor(R.color.color_card_light_one));
                mCardDegreeLeft.setCardBackgroundColor(getResources().getColor(R.color.color_card_light_one));
                mCardTotalUse.setCardBackgroundColor(getResources().getColor(R.color.color_card_light_two));
            } else {
                if (position == 1) {
                    mCardMoneyLeft.setCardBackgroundColor(getResources().getColor(R.color.color_card_air_one));
                    mCardDegreeLeft.setCardBackgroundColor(getResources().getColor(R.color.color_card_air_one));
                    mCardTotalUse.setCardBackgroundColor(getResources().getColor(R.color.color_card_air_two));
                }
            }
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        if (isAdded()) {
//            mCardMoneyLeft.setCardBackgroundColor(getResources().getColor(R.color.color_card_light_one));
//            mCardDegreeLeft.setCardBackgroundColor(getResources().getColor(R.color.color_card_light_one));
//            mCardTotalUse.setCardBackgroundColor(getResources().getColor(R.color.color_card_light_two));
//
//        }
//
//
//    }

    /**
     * set the detail info of the electricity
     *
     * @param eleData
     */
    public void setEleDetail(Electricity eleData) {
        mTvDegreeLeft.setText(eleData.getDegree().getRemain() + "");
        mTvDegreeLastMonth.setText(eleData.getDegree().getBefore());
        mTvDegreeCurMonth.setText(eleData.getDegree().getCurrent());
        mTvMoneyLeft.setText(eleData.getEle().getRemain());
        mTvMoneyLastMonth.setText(eleData.getEle().getBefore());
        mTvMoneyCurMonth.setText(eleData.getEle().getCurrent());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
