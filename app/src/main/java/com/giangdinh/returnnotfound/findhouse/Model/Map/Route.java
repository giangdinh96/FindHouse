package com.giangdinh.returnnotfound.findhouse.Model.Map;

import com.google.gson.annotations.SerializedName;

/**
 * Created by GiangDinh on 05/03/2018.
 */

public class Route {
    private Bound bounds;
    private Leg[] legs;
    @SerializedName("overview_polyline")
    private Polyline overviewPolyline;

    public Route() {

    }

    public Bound getBounds() {
        return bounds;
    }

    public void setBounds(Bound bounds) {
        this.bounds = bounds;
    }

    public Leg[] getLegs() {
        return legs;
    }

    public Leg getLeg(int position) {
        return legs[position];
    }

    public void setLegs(Leg[] legs) {
        this.legs = legs;
    }

    public Polyline getOverviewPolyline() {
        return overviewPolyline;
    }

    public void setOverviewPolyline(Polyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }
}
