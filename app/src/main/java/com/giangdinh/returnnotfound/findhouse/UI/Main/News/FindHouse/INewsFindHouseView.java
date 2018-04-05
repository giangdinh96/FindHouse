package com.giangdinh.returnnotfound.findhouse.UI.Main.News.FindHouse;

import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;

import java.util.ArrayList;

/**
 * Created by GiangDinh on 05/04/2018.
 */

public interface INewsFindHouseView {
    void refreshList();

    void showRefresh(boolean show);

    void addItemHouse(FindHouse findHouse);

    void addItemHouse(int position, FindHouse findHouse);

    void changeItemHouse(FindHouse findHouse);

    void removeAllItemHouse();

    void loadNews(ArrayList<FindHouse> findHouses);

    void showNotification(String title, String content);
}
