package com.giangdinh.returnnotfound.findhouse.UI.MapDetailActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

/**
 * Created by GiangDinh on 26/02/2018.
 */

public interface IMapDetailView {
    public void setSearchAddress(String searchAddress);

    public void addHouseMarker(LatLng latLng);

    public void removeHouseMarker();

    public void addUserMarker(boolean isShowInfor, LatLng latLng);

    public void removeUserMarker();

    public void moveCamera(LatLng latLng, boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    public void showToast(String message);

    public void setDirectionClickable(boolean clickable);

    public void showDirection(ArrayList<LatLng> points, LatLngBounds latLngBounds);
}
