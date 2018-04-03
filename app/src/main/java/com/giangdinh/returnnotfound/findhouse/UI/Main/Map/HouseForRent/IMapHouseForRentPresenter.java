package com.giangdinh.returnnotfound.findhouse.UI.Main.Map.HouseForRent;

/**
 * Created by GiangDinh on 03/04/2018.
 */

public interface IMapHouseForRentPresenter {
    void handleRefresh();

    void handleGetNews();

    void handleCurrentUserLocationClick();

    void handleGetCurrentUserLocation(boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    void handleDestroy();
}
