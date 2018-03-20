package com.giangdinh.returnnotfound.findhouse.UI.ChooseHouseLocation;

import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by GiangDinh on 23/02/2018.
 */

public interface IChooseHouseLocationPresenter {
    public void handleReceiveCurrentLatLngHouse(double latitudeHouse, double longitudeHouse);

    public void handleCurrentHouseLocationClick();

    public void handleGetCurrentHouseLocation(boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    public void handleCurrentUserLocationClick();

    public void handleGetCurrentUserLocation(boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    public void handleDoneClick();

    public void handlePositionSelected(LatLng latLng);

    public void handleOnActivityResult(int requestCode, int resultCode, Intent data);

}
