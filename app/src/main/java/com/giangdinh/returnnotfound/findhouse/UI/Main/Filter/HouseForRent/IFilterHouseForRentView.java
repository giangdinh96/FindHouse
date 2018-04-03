package com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent;

import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;

import java.util.ArrayList;

/**
 * Created by GiangDinh on 03/04/2018.
 */

public interface IFilterHouseForRentView {
    void loadSpinnerProvinces(ArrayList<Province> provinces);

    void loadSpinnerTowns(ArrayList<Town> towns);

    void selectProvince(Province province);

    void selectTown(Town town);

    void setPrice(String price);

    void setStretch(String stretch);

    void setPubDate(String pubDate);
}
