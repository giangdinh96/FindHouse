package com.giangdinh.returnnotfound.findhouse.UI.Main.Profile.EnterUserInformation;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by GiangDinh on 18/03/2018.
 */

public interface ICofirmUserInformationView {
    void showDialogConfirmSuccess(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener);

    void showDialogConfirmFailue(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener);

    void hideDialogConfirm();

    void setUserPicture(String url);

    void setUserName(String userName);

    void setEmail(String email);

    void disableEnterEmail();
}
