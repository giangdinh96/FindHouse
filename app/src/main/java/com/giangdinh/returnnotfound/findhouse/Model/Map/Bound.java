package com.giangdinh.returnnotfound.findhouse.Model.Map;

import java.util.HashMap;

/**
 * Created by GiangDinh on 05/03/2018.
 */

public class Bound {
    private HashMap<String, Float> northeast;
    private HashMap<String, Float> southwest;

    public Bound() {

    }

    public HashMap<String, Float> getNortheast() {
        return northeast;
    }

    public void setNortheast(HashMap<String, Float> northeast) {
        this.northeast = northeast;
    }

    public HashMap<String, Float> getSouthwest() {
        return southwest;
    }

    public void setSouthwest(HashMap<String, Float> southwest) {
        this.southwest = southwest;
    }
}
