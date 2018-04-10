package com.giangdinh.returnnotfound.findhouse.UI.FindHouseDetail;

import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;

/**
 * Created by GiangDinh on 06/04/2018.
 */

public interface IFindHouseDetailPresenter {
    void handleReceiveFindHouse(FindHouse findHouse);

    void handleCallClick(FindHouse findHouse);

    void handleLikeClick(FindHouse findHouse);

    void handleShareClick(FindHouse FindHouse);

    void handleMapDetailClick();

    void handleDestroy();
}
