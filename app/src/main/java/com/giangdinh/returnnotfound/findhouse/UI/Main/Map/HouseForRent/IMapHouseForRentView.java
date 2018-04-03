package com.giangdinh.returnnotfound.findhouse.UI.Main.Map.HouseForRent;

import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by GiangDinh on 03/04/2018.
 */

public interface IMapHouseForRentView {
    public void setSearchAddress(String searchAddress);

    public void addHouseMarker(HouseForRent houseForRent);

    public void removeHouseMarker(HouseForRent houseForRent);

    public void removeAllHousesMaker();

    public void addUserMarker(LatLng latLng);

    public void removeUserMarker();

    public void moveCamera(LatLng latLng, boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    public void showToast(String message);
}
