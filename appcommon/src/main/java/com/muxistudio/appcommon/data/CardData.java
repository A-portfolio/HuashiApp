package com.muxistudio.appcommon.data;

/**
 * Created by ybao on 16/7/4.
 */
public class CardData {


    public String dealDateTime;
    //余额
    public String outMoney;
    public String userName;

    public String transMoney;

    public String dealTypeName;

    public String getDealDateTime() {
        return dealDateTime;
    }

    public void setDealDateTime(String dealDateTime) {
        this.dealDateTime = dealDateTime;
    }

    public String getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(String outMoney) {
        this.outMoney = outMoney;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTransMoney() {
        return transMoney;
    }

    public void setTransMoney(String transMoney) {
        this.transMoney = transMoney;
    }

    public String getDealTypeName() {
        return dealTypeName;
    }

    public void setDealTypeName(String dealTypeName) {
        this.dealTypeName = dealTypeName;
    }
}
