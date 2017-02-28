package net.muxi.huashiapp.common.data;

/**
 * Created by december on 17/2/28.
 */

public class Item {

    private int position;
    private String name;
    private Integer icon;

    public Item(int position,String name,Integer icon){
        this.position = position;
        this.name = name;
        this.icon = icon;
    }

    public Item(String name,Integer icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }
}
