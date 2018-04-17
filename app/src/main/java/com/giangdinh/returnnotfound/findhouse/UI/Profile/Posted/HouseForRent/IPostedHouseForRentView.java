package com.giangdinh.returnnotfound.findhouse.UI.Profile.Favorite.HouseForRent;

import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;
import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;

import java.util.ArrayList;

public interface IFavoriteHouseForRentView {
    void refreshList();

    void showRefresh(boolean show);

    void addItemHouse(HouseForRent houseForRent);

    void addItemHouse(int position, HouseForRent houseForRent);

    void changeItemHouse(HouseForRent houseForRent);

    void removeAllItemHouse();

    void loadNews(ArrayList<HouseForRent> houseForRents);
}
