package com.giangdinh.returnnotfound.findhouse.UI.Main.Map.HouseForRent;

import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by GiangDinh on 03/04/2018.
 */

public interface IMapHouseForRentView {
    void setSearchAddress(String searchAddress);

    void addHouseMarker(HouseForRent houseForRent);

    void removeHouseMarker(HouseForRent houseForRent);

    void removeAllHousesMaker();

    void addUserMarker(LatLng latLng);

    void removeUserMarker();

    void addSearchMarker(LatLng latLng, String address);

    void removeSearchMarker();

    void navigateToSearchPlace();

    void moveCamera(LatLng latLng, boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    void showToast(String message);
}
