package com.giangdinh.returnnotfound.findhouse.UI.Profile.Intro;

import com.giangdinh.returnnotfound.findhouse.Model.User;

public class IntroPresenter implements IIntroPresenter {
    private IIntroView iIntroView;

    public IntroPresenter(IIntroView iIntroView) {
        this.iIntroView = iIntroView;
    }

    @Override
    public void handleReceiveUser(User user) {
        iIntroView.setUserPicture(user.getPhotoUrl());
        iIntroView.setUserName(user.getName());
        iIntroView.setUserRole(user.getRole().equals("1") ? "Người cho thuê" : "Người tìm phòng");
        iIntroView.setUserPhone(user.getPhone());
        iIntroView.setUserEmail(user.getEmail());
        iIntroView.setUserCmt(user.getCmtnd());
    }
}
