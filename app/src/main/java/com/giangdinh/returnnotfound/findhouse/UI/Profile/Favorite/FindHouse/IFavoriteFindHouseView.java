package com.giangdinh.returnnotfound.findhouse.UI.Profile.Favorite.FindHouse;

import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;

import java.util.ArrayList;

public interface IFavoriteFindHouseView {
    void refreshList();

    void showRefresh(boolean show);

    void addItemHouse(FindHouse findHouse);

    void addItemHouse(int position, FindHouse findHouse);

    void changeItemHouse(FindHouse findHouse);

    void removeAllItemHouse();

    void loadNews(ArrayList<FindHouse> findHouses);
}
