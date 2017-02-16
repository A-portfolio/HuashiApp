package net.muxi.huashiapp.common.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by december on 17/2/11.
 */

public class ClassRoom {


    @SerializedName("1")
    private List<String> value1;
    @SerializedName("3")
    private List<String> value3;
    @SerializedName("5")
    private List<String> value5;
    @SerializedName("7")
    private List<String> value7;
    @SerializedName("9")
    private List<String> value9;
    @SerializedName("11")
    private List<String> value11;
    @SerializedName("13")
    private List<String> value13;

    public List<String> getValue1() {
        return value1;
    }

    public void setValue1(List<String> value1) {
        this.value1 = value1;
    }

    public List<String> getValue3() {
        return value3;
    }

    public void setValue3(List<String> value3) {
        this.value3 = value3;
    }

    public List<String> getValue5() {
        return value5;
    }

    public void setValue5(List<String> value5) {
        this.value5 = value5;
    }

    public List<String> getValue7() {
        return value7;
    }

    public void setValue7(List<String> value7) {
        this.value7 = value7;
    }

    public List<String> getValue9() {
        return value9;
    }

    public void setValue9(List<String> value9) {
        this.value9 = value9;
    }

    public List<String> getValue11() {
        return value11;
    }

    public void setValue11(List<String> value11) {
        this.value11 = value11;
    }

    public List<String> getValue13() {
        return value13;
    }

    public void setValue13(List<String> value13) {
        this.value13 = value13;
    }
}
