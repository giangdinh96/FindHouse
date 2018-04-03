package com.giangdinh.returnnotfound.findhouse.UI.HouseForRentDetail;

import android.os.Bundle;

/**
 * Created by GiangDinh on 28/01/2018.
 */

public interface IHouseForRentDetailView {
    void setUserPicture(String userPicture);

    void setUserName(String userName);

    void setTime(String time);

    void setAddress(String address);

    void setPrice(String price);

    void setStretch(String stretch);

    void setPhone(String phone);

    void setEmail(String email);

    void setDescribe(String describe);

    void setLikeSelected(boolean likeSelected);

    void showDivider3();

    void hideDivider3();

    void setUsersLike(String usersLike);

    void showUsersLike();

    void hideUsersLike();

    void showDivider4();

    void hideDivider4();

    void initMapView(Bundle savedInstanceState);

    void addMarker(double latitude, double longitude);
}