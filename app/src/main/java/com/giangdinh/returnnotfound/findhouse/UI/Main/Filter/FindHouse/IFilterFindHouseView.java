package com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.FindHouse;

import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;

import java.util.ArrayList;

/**
 * Created by GiangDinh on 05/04/2018.
 */

public interface IFilterFindHouseView {
    void loadSpinnerProvinces(ArrayList<Province> provinces);

    void loadSpinnerTowns(ArrayList<Town> towns);

    void selectProvince(Province province);

    void selectTown(Town town);

    void setPrice(String price);

    void setStretch(String stretch);

    void setPubDate(String pubDate);
}
