package com.giangdinh.returnnotfound.findhouse.UI.Profile.Intro;

public interface IIntroView {
    void setUserPicture(String photoUrl);

    void setUserName(String userName);

    void setUserRole(String userRole);

    void setUserPhone(String userPhone);

    void setUserEmail(String userEmail);

    void setUserCmt(String userCmt);

    void showMessage(String message);
}
