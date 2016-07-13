package net.muxi.huashiapp.common.data;

/**
 * Created by ybao on 16/7/4.
 */
public class CardData {


    private String dealDateTime;
    //余额
    private String outMoney;
    private String userName;

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
}
