package com.giangdinh.returnnotfound.findhouse.UI.Profile.Posted.HouseForRent;

import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;

import java.util.ArrayList;

public interface IPostedHouseForRentView {
    void refreshList();

    void showRefresh(boolean show);

    void addItemHouse(HouseForRent houseForRent);

    void addItemHouse(int position, HouseForRent houseForRent);

    void changeItemHouse(HouseForRent houseForRent);

    void removeAllItemHouse();

    void loadNews(ArrayList<HouseForRent> houseForRents);
}
