package com.muxistudio.appcommon.view;

import com.muxistudio.appcommon.data.CardDailyUse;
import com.muxistudio.appcommon.data.CardDataEtp;

public interface ICardView {
    void initView(CardDailyUse dailyUse, CardDataEtp cardDataEtp);
}
