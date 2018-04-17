package com.giangdinh.returnnotfound.findhouse.UI.Main.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.UI.Information.InformationActivity;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Profile.EnterUserInformation.ConfirmUserInformationDialog;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Profile.EnterUserInformation.IConfirmUserInformationDialogListener;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Profile.SignOut.ISignOutDialogListener;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Profile.SignOut.SignOutDialog;
import com.giangdinh.returnnotfound.findhouse.UI.Profile.ProfileActivity;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal;
import com.giangdinh.returnnotfound.findhouse.Utils.ViewUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by GiangDinh on 24/01/2018.
 */

public class ProfileFragment extends Fragment implements IProfileView, ISignOutDialogListener, IConfirmUserInformationDialogListener {

    private Context context;
    public IProfilePresenter iProfilePresenter;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;

    @BindView(R.id.clUser)
    ConstraintLayout clUser;

    @BindView(R.id.clSignOut)
    ConstraintLayout clSignOut;

    @BindView(R.id.clGoogleSignIn)
    ConstraintLayout clGoogleSignIn;

    @BindView(R.id.clFacebookSignIn)
    ConstraintLayout clFacebookSignIn;

    @BindView(R.id.clSupport)
    ConstraintLayout clSupport;

    @BindView(R.id.clInformation)
    ConstraintLayout clInformation;


    @BindView(R.id.ivUserPicture)
    ImageView ivUserPicture;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    private SweetAlertDialog dialogSignIn;
    private RequestManager requestManager;

    public static final int RC_SIGN_IN = 1000;
    public static final String EXTRA_USER = "com.giangdinh.returnnotfound.findhouse.UI.Main.Profile.EXTRA_USER";

    Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.requestManager = Glide.with(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        iProfilePresenter = new ProfilePresenter(this);

        initViews();
        initEvents();

        return view;
    }

    private void initViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        dialogSignIn = new SweetAlertDialog(context);

        initGoogleSignIn();
        initFacebookSignIn();
    }

    public void initFacebookSignIn() {
        callbackManager = CallbackManager.Factory.create();
        iProfilePresenter.handleFacebookSignInCallback(callbackManager);
    }

    public void initGoogleSignIn() {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);
    }

    ////// Init events
    public void initEvents() {
        iProfilePresenter.handleAuthStateChanged();
    }

    @OnClick(R.id.clUser)
    public void userClick() {
        if (!FirebaseUtils.isSignIn())
            return;
        FirebaseUtils.getDatabase().getReference().child("users").child(FirebaseUtils.getCurrentUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        user.setId(FirebaseUtils.getCurrentUserId());

                        Intent intentProfile = new Intent(getContext(), ProfileActivity.class);
                        intentProfile.putExtra(VariableGlobal.EXTRA_USER, user);
                        startActivity(intentProfile);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @OnClick(R.id.clSignOut)
    public void signOutClick() {
        showDialogSignOut();
    }

    @OnClick(R.id.clGoogleSignIn)
    public void googleSignInClick() {
        delayClickGoogleSignIn(500);
        delayClickFacebookSignIn(500);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.clFacebookSignIn)
    public void facebookSignInClick() {
        delayClickGoogleSignIn(500);
        delayClickFacebookSignIn(500);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
    }

    @OnClick(R.id.clInformation)
    public void infomationClick() {
        Intent intentInfomation = new Intent(getContext(), InformationActivity.class);
        startActivity(intentInfomation);
    }
    ////// Implement IView

    // Implement IProfileView
    @Override
    public void showDialogSignInLoading(String title, String content) {
        dialogSignIn.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        dialogSignIn.setCancelable(false);
        dialogSignIn.setTitleText(title);
        dialogSignIn.setContentText(content);
        dialogSignIn.show();
    }

    @Override
    public void showDialogSignInFailue(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener) {
        dialogSignIn.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        dialogSignIn.setCancelable(false);
        dialogSignIn.setTitleText(title);
        dialogSignIn.setContentText(content);
        dialogSignIn.setConfirmClickListener(onSweetClickListener);
        dialogSignIn.show();
    }

    @Override
    public void hideDialogSignIn() {
        dialogSignIn.dismiss();
    }

    @Override
    public void showDialogSignOut() {
        ViewUtils.delayAfterPress(clUser, 500);
        ViewUtils.delayAfterPress(clSignOut, 500);
        SignOutDialog signOutDialog = new SignOutDialog();
        signOutDialog.setTargetFragment(this, 1000);
        signOutDialog.setCancelable(false);
        signOutDialog.show(getFragmentManager(), "showSignOutDialog");
    }

    @Override
    public void showDialogEnterUserInformation(User user) {
        ConfirmUserInformationDialog confirmUserInformationDialog = ConfirmUserInformationDialog.newInstance(user);
        confirmUserInformationDialog.setTargetFragment(this, 10000);
        confirmUserInformationDialog.setCancelable(false);
        confirmUserInformationDialog.show(getFragmentManager(), "showDialogEnterUserInformation");
    }

    @Override
    public void signOut() {
        firebaseAuth.signOut();
        googleSignInClient.signOut();
        LoginManager.getInstance().logOut();
    }

    @Override
    public void updateUISignIn(int durationShow, int durationHide) {
        showUser(durationShow);
        showSignOut(durationShow);
        hideGoogleSignIn(durationHide);
        hideFacebookSignIn(durationHide);
    }

    @Override
    public void updateUISignOut(int durationShow, int durationHide) {
        hideUser(durationHide);
        hideSignOut(durationHide);
        showGoogleSignIn(durationShow);
        showFacebookSignIn(durationShow);
    }

    @Override
    public void showUser(int duration) {
        ViewUtils.showViewAnimate(clUser, duration);
    }

    @Override
    public void hideUser(int duration) {
        ViewUtils.hideViewAnimate(clUser, duration);
    }

    @Override
    public void setInformationForUser(String name, String photoUrl) {
        tvUserName.setText(name);
        requestManager.load(photoUrl).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivUserPicture) {
            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivUserPicture.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void showSignOut(int duration) {
        ViewUtils.showViewAnimate(clSignOut, duration);
    }

    @Override
    public void hideSignOut(int duration) {
        ViewUtils.hideViewAnimate(clSignOut, duration);
    }

    @Override
    public void showGoogleSignIn(int duration) {
        ViewUtils.showViewAnimate(clGoogleSignIn, duration);
    }

    @Override
    public void hideGoogleSignIn(int duration) {
        ViewUtils.hideViewAnimate(clGoogleSignIn, duration);
    }

    @Override
    public void showFacebookSignIn(int duration) {
        ViewUtils.showViewAnimate(clFacebookSignIn, duration);
    }

    @Override
    public void hideFacebookSignIn(int duration) {
        ViewUtils.hideViewAnimate(clFacebookSignIn, duration);
    }

    // Delay
    @Override
    public void delayClickUser(int duration) {
        ViewUtils.delayAfterPress(clUser, duration);
    }

    @Override
    public void delayClickSignOut(int duration) {
        ViewUtils.delayAfterPress(clSignOut, duration);
    }

    @Override
    public void delayClickGoogleSignIn(int duration) {
        ViewUtils.delayAfterPress(clGoogleSignIn, duration);
    }

    @Override
    public void delayClickFacebookSignIn(int duration) {
        ViewUtils.delayAfterPress(clFacebookSignIn, duration);
    }

    @Override
    public void delayClickSupport(int duration) {
        ViewUtils.delayAfterPress(clSupport, duration);
    }

    @Override
    public void delayClickInformation(int duration) {
        ViewUtils.delayAfterPress(clInformation, duration);
    }

    // Implement ISignOutDialogListener
    @Override
    public void onSignOutAcceptClick() {
        iProfilePresenter.handleSignOutAccept();
    }

    @Override
    public void onConfirmInformationConfirmSuccess() {
        iProfilePresenter.handleConfirmInformationSuccess();
    }

    @Override
    public void onConfirmInformationCancelClick() {
        iProfilePresenter.handleConfirmInformationCancel();
    }

    ////// Override
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnProfileSignOut:
                Toast.makeText(getContext(), "Click item updateUISignOut profile", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        iProfilePresenter.handleGoogleSignInCallback(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iProfilePresenter.handleDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }
}
