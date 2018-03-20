package com.giangdinh.returnnotfound.findhouse.Model;

import com.google.firebase.database.Exclude;

/**
 * Created by GiangDinh on 02/02/2018.
 */

public class Address {
    private String id;
    private String detail;
    private String provinceId;
    private String townId;

    public Address() {

    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId;
    }
}
