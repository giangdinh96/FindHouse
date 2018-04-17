package com.giangdinh.returnnotfound.findhouse.UI.Profile.Intro;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IntroFragment extends Fragment implements IIntroView {
    private IIntroPresenter iIntroPresenter;
    private User user;
    private RequestManager requestManager;

    @BindView(R.id.ivUserPicture)
    ImageView ivUserPicture;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvUserRole)
    TextView tvUserRole;
    @BindView(R.id.tvUserPhone)
    TextView tvUserPhone;
    @BindView(R.id.tvUserEmail)
    TextView tvUserEmail;
    @BindView(R.id.tvUserCmt)
    TextView tvUserCmt;

    private Unbinder unbinder;

    public static IntroFragment newInstance(User user) {
        IntroFragment fragment = new IntroFragment();
        Bundle args = new Bundle();
        args.putSerializable(VariableGlobal.EXTRA_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(VariableGlobal.EXTRA_USER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro, container, false);
        unbinder = ButterKnife.bind(this, view);
        iIntroPresenter = new IntroPresenter(this);

        initViews();
        initEvents();

        return view;
    }

    private void initViews() {
        requestManager = Glide.with(getContext());
        iIntroPresenter.handleReceiveUser(user);
    }

    private void initEvents() {

    }


    @Override
    public void setUserPicture(String photoUrl) {
        requestManager.load(photoUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivUserPicture) {
            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
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
    public void setUserRole(String userRole) {
        tvUserRole.setText(userRole);
    }

    @Override
    public void setUserPhone(String userPhone) {
        tvUserPhone.setText(userPhone);
    }

    @Override
    public void setUserEmail(String userEmail) {
        tvUserEmail.setText(userEmail);
    }

    @Override
    public void setUserCmt(String userCmt) {
        tvUserCmt.setText(userCmt);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }
}