package com.giangdinh.returnnotfound.findhouse.UI.Main.Profile;

import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by GiangDinh on 31/01/2018.
 */

public interface IProfileView {
    void showDialogSignInLoading(String title, String content);

    void showDialogSignInFailue(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener);

    void hideDialogSignIn();

    void showDialogSignOut();

    void showDialogEnterUserInformation(User user);

    void signOut();

    void updateUISignIn(int durationShow, int durationHide);

    void updateUISignOut(int durationShow, int durationHide);

    void showUser(int duration);

    void hideUser(int duration);

    void setInformationForUser(String name, String photoUrl);

    void showSignOut(int duration);

    void hideSignOut(int duration);

    void showGoogleSignIn(int duration);

    void hideGoogleSignIn(int duration);

    void showFacebookSignIn(int duration);

    void hideFacebookSignIn(int duration);

    void delayClickUser(int duration);

    void delayClickSignOut(int duration);

    void delayClickGoogleSignIn(int duration);

    void delayClickFacebookSignIn(int duration);

    void delayClickSupport(int duration);

    void delayClickInformation(int duration);
}
