package com.giangdinh.returnnotfound.findhouse.UI.Main.News.HouseForRent;

import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;

import java.util.ArrayList;

/**
 * Created by GiangDinh on 22/03/2018.
 */

public interface INewsHouseForRentView {
    void refreshList();

    void showRefresh(boolean show);

    void addItemHouse(HouseForRent houseForRent);

    void addItemHouse(int position, HouseForRent houseForRent);

    void changeItemHouse(HouseForRent houseForRent);

    void removeAllItemHouse();

    void loadNews(ArrayList<HouseForRent> houseForRents);

    void showNotification(String title, String content);
}