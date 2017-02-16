package net.muxi.huashiapp.common.data;

import java.util.List;

/**
 * Created by december on 16/7/30.
 */
public class ApartmentData {

    /**
     * phone : ["67867510"]
     * apartment : 学生事务大厅
     * place : 文华公书林（老图书馆）3楼
     */

    public String apartment;
    public String place;
    public List<String> phone;

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }
}
