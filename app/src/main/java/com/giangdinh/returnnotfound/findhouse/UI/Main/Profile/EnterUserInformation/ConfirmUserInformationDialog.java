package com.giangdinh.returnnotfound.findhouse.UI.Main.Profile.EnterUserInformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Profile.ProfileFragment;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.InternetUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GiangDinh on 17/03/2018.
 */

public class ConfirmUserInformationDialog extends DialogFragment implements ICofirmUserInformationView {

    private User user;
    private IConfirmUserInformationDialogListener iConfirmUserInformationDialogListener;

    @BindView(R.id.ivUserPicture)
    ImageView ivUserPicture;
    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etCmtnd)
    EditText etCmtnd;
    @BindView(R.id.rbOne)

    RadioButton rbOne;
    @BindView(R.id.rbTwo)
    RadioButton rbTwo;
    @BindView(R.id.rbThree)
    RadioButton rbThree;

    private RequestManager requestManager;
    private SweetAlertDialog confirmDialog;

    public ConfirmUserInformationDialog() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        requestManager = Glide.with(context);
        iConfirmUserInformationDialogListener = (IConfirmUserInformationDialogListener) getTargetFragment();
    }

    public static ConfirmUserInformationDialog newInstance(User user) {
        Bundle args = new Bundle();
        args.putSerializable(ProfileFragment.EXTRA_USER, user);
        ConfirmUserInformationDialog fragment = new ConfirmUserInformationDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(ProfileFragment.EXTRA_USER);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_user_information, container, false);
        ButterKnife.bind(this, view);

        initViews();
        initDialog();

        return view;
    }

    ////// Init views
    private void initDialog() {
        confirmDialog = new SweetAlertDialog(getContext());
        confirmDialog.setCancelable(false);
    }

    public void initViews() {
        setUserPicture(user.getPhotoUrl());
        setUserName(user.getName());
        setEmail(user.getEmail());
    }

    ////// Init events
    @OnClick(R.id.btnConfirm)
    public void confirmClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleConfirmClick();
            }
        }, 100);
    }

    public void handleConfirmClick() {
        if (checkError().equals("")) {
            // Assign data to user
            user.setPhone(etPhone.getText().toString());
            user.setEmail(etEmail.getText().toString());
            user.setCmtnd(etCmtnd.getText().toString());
            if (rbOne.isChecked())
                user.setRole("1");
            if (rbTwo.isChecked())
                user.setRole("2");
            if (rbThree.isChecked())
                user.setRole("3");
            // Sync to firebase

            if (!InternetUtils.isNetworkConnected(getContext())) {
                showDialogConfirmFailue("Có lỗi xảy ra!", "Vui lòng kiểm tra lại đường truyền!",
                        new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                hideDialogConfirm();
                            }
                        });
            }

            FirebaseUtils.getDatabase().getReference().child("users").child(user.getId()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        showDialogConfirmFailue("Có lỗi xảy ra!", task.getException().toString(),
                                new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        hideDialogConfirm();
                                    }
                                });
                        return;
                    }
                    iConfirmUserInformationDialogListener.onConfirmInformationConfirmSuccess();
                    showDialogConfirmSuccess("Chúc mừng!", "Bạn đã tạo xác thực thành công!",
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    hideDialogConfirm();
                                    getDialog().dismiss();
                                }
                            });
                }
            });
        } else {
            showDialogConfirmFailue("Có lỗi xảy ra!", checkError(),
                    new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            hideDialogConfirm();
                        }
                    });
        }
    }

    @OnClick(R.id.btnCancel)
    public void cancelClick() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDialog().dismiss();
                iConfirmUserInformationDialogListener.onConfirmInformationCancelClick();
            }
        }, 100);
    }

    @Override
    public void showDialogConfirmSuccess(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener) {
        confirmDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        confirmDialog.setTitleText(title);
        confirmDialog.setContentText(content);
        confirmDialog.setConfirmClickListener(onSweetClickListener);
        confirmDialog.show();
    }

    @Override
    public void showDialogConfirmFailue(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener) {
        confirmDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        confirmDialog.setTitleText(title);
        confirmDialog.setContentText(content);
        confirmDialog.setConfirmClickListener(onSweetClickListener);
        confirmDialog.show();
    }

    @Override
    public void hideDialogConfirm() {
        confirmDialog.dismiss();
    }

    ////// Implement IView
    @Override
    public void setUserPicture(String url) {
        requestManager.load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivUserPicture) {
            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivUserPicture.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void setUserName(String userName) {
        tvUserName.setText(userName);
    }

    @Override
    public void setEmail(String email) {
        etEmail.setText(email);
        if (etEmail.getText() != null && !etEmail.getText().toString().equals(""))
            disableEnterEmail();
    }

    @Override
    public void disableEnterEmail() {
        etEmail.setFocusable(false);
        etEmail.setFocusableInTouchMode(false);
    }

    ////// Method Utils
    public String checkError() {
        StringBuilder error = new StringBuilder("");
        if (etPhone.getText().toString().equals("")) {
            error.append("- Bạn chưa nhập số điện thoại!\n");
        }
        if (etEmail.getText().toString().equals("")) {
            error.append("- Bạn chưa nhập địa chỉ email!\n");
        }
        if (etCmtnd.getText().toString().equals("")) {
            error.append("- Bạn chưa nhập số chứng minh thư!\n");
        } else if (etCmtnd.getText().toString().length() != 9) {
            error.append("- Số chứng minh thư phải có 9 số!\n");
        }
//        if (String.valueOf(error).endsWith("\n")) {
//        }
        return String.valueOf(error);
    }
}
