package com.giangdinh.returnnotfound.findhouse.UI.Main.Map.FindHouse;

import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by GiangDinh on 05/04/2018.
 */

public interface IMapFindHouseView {
    void setSearchAddress(String searchAddress);

    void addHouseMarker(FindHouse findHouse);

    void removeHouseMarker(FindHouse findHouse);

    void removeAllHousesMaker();

    void addUserMarker(LatLng latLng);

    void removeUserMarker();

    void moveCamera(LatLng latLng, boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    void showToast(String message);
}
