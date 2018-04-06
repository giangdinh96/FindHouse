package com.giangdinh.returnnotfound.findhouse.UI.Main.Map.FindHouse;

/**
 * Created by GiangDinh on 05/04/2018.
 */

public interface IMapFindHousePresenter {
    void handleRefresh();

    void handleGetNews();

    void handleCurrentUserLocationClick();

    void handleGetCurrentUserLocation(boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    void handleDestroy();
}
