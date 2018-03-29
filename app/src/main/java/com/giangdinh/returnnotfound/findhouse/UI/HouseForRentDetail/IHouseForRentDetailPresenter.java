package com.giangdinh.returnnotfound.findhouse.UI.HouseForRentDetail;

import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;

/**
 * Created by GiangDinh on 28/01/2018.
 */

public interface IHouseForRentDetailPresenter {
    void handleReceiveHouseForRent(HouseForRent houseForRent);

    void handleCallClick(HouseForRent houseForRent);

    void handleLikeClick(HouseForRent houseForRent);

    void handleShareClick(HouseForRent houseForRent);

    void handleMapDetailClick();

    void handleDestroy();
}
