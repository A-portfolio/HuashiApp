package net.muxi.huashiapp.common.data;

import java.io.Serializable;

/**
 * Created by december on 17/2/28.
 */

public class ItemData implements Serializable {

    private int position;
    private String name;
    private String icon;


    public ItemData(int position,String name,String icon){
        this.position = position;
        this.name = name;
        this.icon = icon;
    }
    public ItemData(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public ItemData(){

    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }
}
