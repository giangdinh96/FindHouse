package com.giangdinh.returnnotfound.findhouse.UI.ChooseHouseLocation;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by GiangDinh on 23/02/2018.
 */

public interface IChooseHouseLocationView {
    public void setSearchAddress(String searchAddress);

    public void addHouseMarker(LatLng latLng);

    public void removeHouseMarker();

    public void addUserMarker(LatLng latLng);

    public void removeUserMarker();

    public void moveCamera(LatLng latLng, boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    public void showToast(String message);

    public void finishActivity();
}
