package com.giangdinh.returnnotfound.findhouse.UI.FindHouseDetail;

/**
 * Created by GiangDinh on 06/04/2018.
 */

public interface IFindHouseDetailView {
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

    void addMarker(double latitude, double longitude);
}
