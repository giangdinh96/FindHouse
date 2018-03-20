package com.giangdinh.returnnotfound.findhouse.UI.Main.Profile;

import android.content.Intent;

import com.facebook.CallbackManager;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.google.firebase.auth.AuthCredential;

/**
 * Created by GiangDinh on 31/01/2018.
 */

public interface IProfilePresenter {
    void handleAuthStateChanged();

    void handleSignOutAccept();

    void handleConfirmInformationSuccess();

    void handleConfirmInformationCancel();

    void handleGoogleSignInCallback(int requestCode, int resultCode, Intent data);

    void handleFacebookSignInCallback(CallbackManager callbackManager);

    void handleSignInWithCredential(AuthCredential authCredential);

    void handleDestroy();
}