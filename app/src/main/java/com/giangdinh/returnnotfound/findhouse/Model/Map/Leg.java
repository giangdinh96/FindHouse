package com.giangdinh.returnnotfound.findhouse.Model.Map;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by GiangDinh on 05/03/2018.
 */

public class Leg {
    private Duration duration;
    private Distance distance;
    private String end_address;
    private String start_address;
    @SerializedName("end_location")
    private HashMap<String, Float> endLocation;
    @SerializedName("start_location")
    private HashMap<String, Float> startLocation;
    private Step[] steps;

    public Leg() {

    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public HashMap<String, Float> getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(HashMap<String, Float> endLocation) {
        this.endLocation = endLocation;
    }

    public HashMap<String, Float> getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(HashMap<String, Float> startLocation) {
        this.startLocation = startLocation;
    }

    public Step[] getSteps() {
        return steps;
    }

    public void setSteps(Step[] steps) {
        this.steps = steps;
    }
}
