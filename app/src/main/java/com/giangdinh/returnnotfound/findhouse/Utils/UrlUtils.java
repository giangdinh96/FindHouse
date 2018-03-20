package com.giangdinh.returnnotfound.findhouse.Utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by GiangDinh on 06/03/2018.
 */

public class UrlUtils {
    public static String makeUrlDirection(LatLng origin, LatLng destination, String mode) {
        String pOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String pDestination = "destination=" + destination.latitude + "," + destination.longitude;
        String pSensor = "sensor=false";
        String pMode = "mode=" + mode;
        String pUnits = "units=metric";
        String pAlternatives = "alternatives=false";
        String pKey = "AIzaSyDFwLfs7CroLYKPwc_D3BdyEhdY7sliQqs";
        StringBuffer params = new StringBuffer(pOrigin)
                .append("&").append(pDestination)
                .append("&").append(pSensor)
                .append("&").append(pMode)
                .append("&").append(pUnits)
                .append("&").append(pAlternatives)
                .append("&").append(pKey);
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;
        return url;
    }
}
