package com.giangdinh.returnnotfound.findhouse.UI.MapDetailActivity;

import android.content.Intent;

/**
 * Created by GiangDinh on 26/02/2018.
 */

public interface IMapDetailPresenter {
    public void handleReceiveCurrentLatLngHouse(double latitudeHouse, double longitudeHouse);

    public void handleCurrentHouseLocationClick();

    public void handleCurrentHouseLocationLongClick();

    public void handleGetCurrentHouseLocation(boolean isMove, boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    public void handleCurrentUserLocationClick();

    public void handleCurrentUserLocationLongClick();

    public void handleGetCurrentUserLocation(boolean isShowInfor, boolean isMove, boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    public void handleDirectionClick(String mode);

    public void handleOnActivityResult(int requestCode, int resultCode, Intent data);

}
