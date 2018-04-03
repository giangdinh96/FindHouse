package com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent;

import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;

/**
 * Created by GiangDinh on 03/04/2018.
 */

public interface IFilterHouseForRentPresenter {
    void getProvinces();

    void handleProvinceSelected(Province province, int position);

    void handleTownSelected(Town town, int position);

    void handlePriceChange(Number minValue, Number maxValue);

    void handlePriceFinalChange(Number minValue, Number maxValue);

    void handleStretchChange(Number minValue, Number maxValue);

    void handleStretchFinalChange(Number minValue, Number maxValue);

    void handlePubDateChange(Number minValue);

    void handlePubDateFinalChange(Number minValue);
}
