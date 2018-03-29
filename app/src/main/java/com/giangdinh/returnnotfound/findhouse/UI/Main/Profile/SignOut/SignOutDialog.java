package com.giangdinh.returnnotfound.findhouse.UI.Main.Profile.SignOut;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

/**
 * Created by GiangDinh on 16/03/2018.
 */

public class SignOutDialog extends DialogFragment {

    private ISignOutDialogListener iSignOutDialogListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iSignOutDialogListener = (ISignOutDialogListener) getTargetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder signOutDialogBuilder = new AlertDialog.Builder(getContext());
        signOutDialogBuilder.setTitle("Đăng xuất");
        signOutDialogBuilder.setMessage("Bạn có chắc chắn muốn đăng xuất?");

        signOutDialogBuilder.setNegativeButton("không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        signOutDialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                iSignOutDialogListener.onSignOutAcceptClick();
                dialog.dismiss();
            }
        });

        Dialog signOutDialog = signOutDialogBuilder.create();
        return signOutDialog;
    }
}
