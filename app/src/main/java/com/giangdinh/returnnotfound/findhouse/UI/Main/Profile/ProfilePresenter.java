package com.giangdinh.returnnotfound.findhouse.UI.Main.Profile;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import static com.giangdinh.returnnotfound.findhouse.UI.Main.Profile.ProfileFragment.RC_SIGN_IN;

/**
 * Created by GiangDinh on 31/01/2018.
 */

public class ProfilePresenter implements IProfilePresenter {
    private Context context;
    private IProfileView iProfileView;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private boolean isFirstStart = true;

    public ProfilePresenter(IProfileView iProfileView) {
        this.context = ((ProfileFragment) iProfileView).getContext();
        this.iProfileView = iProfileView;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void handleAuthStateChanged() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if (!isFirstStart) {
//                    NewsFragment.isNeedLoad = true;
//                    MapFragment.isNeedLoad = true;
                    if (firebaseAuth.getCurrentUser() != null) {
                        // SignIn success
                        FirebaseUtils.getDatabase().getReference().child("users").child(FirebaseUtils.getCurrentUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null && dataSnapshot.exists()) {
                                    iProfileView.setInformationForUser(FirebaseUtils.getCurrentUserName(), FirebaseUtils.getCurrentUserPhotoUrl());
                                    iProfileView.updateUISignIn(1000, 300);
                                } else {
                                    User user = new User();
                                    user.setId(FirebaseUtils.getCurrentUserId());
                                    user.setName(FirebaseUtils.getCurrentUserName());
                                    user.setEmail(FirebaseUtils.getCurrentUserEmail());
                                    user.setPhotoUrl(FirebaseUtils.getCurrentUserPhotoUrl());
                                    iProfileView.showDialogEnterUserInformation(user);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("Test", "Error in signIn: " + databaseError);
                            }
                        });
                    } else {
                        // SignOut
                        iProfileView.updateUISignOut(1000, 300);
                    }
                } else {
                    isFirstStart = false;
                    if (firebaseAuth.getCurrentUser() != null) {
                        // SignIn success
                        FirebaseUtils.getDatabase().getReference().child("users").child(FirebaseUtils.getCurrentUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot != null && dataSnapshot.exists()) {
                                    iProfileView.setInformationForUser(FirebaseUtils.getCurrentUserName(), FirebaseUtils.getCurrentUserPhotoUrl());
                                    iProfileView.updateUISignIn(0, 0);
                                } else {
                                    iProfileView.signOut();
                                    iProfileView.updateUISignOut(0, 0);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("Test", "Error in signIn: " + databaseError);
                            }
                        });
                    } else {
                        // SignOut
                        iProfileView.updateUISignOut(0, 0);
                    }
                }
            }
        };

        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void handleSignOutAccept() {
        iProfileView.signOut();
    }

    @Override
    public void handleConfirmInformationSuccess() {
        iProfileView.setInformationForUser(FirebaseUtils.getCurrentUserName(), FirebaseUtils.getCurrentUserPhotoUrl());
        iProfileView.updateUISignIn(1000, 300);
    }

    @Override
    public void handleConfirmInformationCancel() {
        iProfileView.signOut();
    }

    @Override
    public void handleGoogleSignInCallback(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount googleSignInAccount = task.getResult(ApiException.class);
                Log.d("Test", "onActivityResult SignIn with Google success");

                AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                handleSignInWithCredential(credential);
            } catch (ApiException e) {
                Log.d("Test", "onActivityResult SignIn with Google failue: " + e.getMessage());
                if (e.toString().contains("12501") || e.toString().contains("12502") || e.toString().contains("8")) {
                    if (e.toString().contains("12502") || e.toString().contains("8"))
                        Toast.makeText(context, "Bạn không nhân nhấn quá nhanh!", Toast.LENGTH_SHORT).show();
                    return;
                }
                iProfileView.showDialogSignInFailue("Có lỗi xảy ra!", "Vui lòng kiểm tra lại đường truyền!", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        iProfileView.hideDialogSignIn();
                    }
                });
            }
        }
    }

    @Override
    public void handleFacebookSignInCallback(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AuthCredential authCredential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                        handleSignInWithCredential(authCredential);
                        Log.d("Test", "Callback SignIn with Facebook  success");
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Test", "Callback SignIn with Facebook cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("Test", "Callback SignIn with Facebook error :" + exception);
                        if (exception.toString().contains("CONNECTION_FAILURE")) {
                            iProfileView.showDialogSignInFailue("Có lỗi xảy ra!", "Vui lòng kiểm tra lại đường truyền!", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    iProfileView.hideDialogSignIn();
                                }
                            });
                        } else {
                            iProfileView.showDialogSignInFailue("Có lỗi xảy ra!", "" + exception, new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    iProfileView.hideDialogSignIn();
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public void handleSignInWithCredential(AuthCredential authCredential) {
        iProfileView.showDialogSignInLoading("Đăng nhập", "Đang đăng nhập...");
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("Test", "handleSignInWithCredential Success");
                    iProfileView.hideDialogSignIn();
                    delayClick();
                } else {
                    Log.d("Test", "handleSignInWithCredential Failure", task.getException());
                    if (task.getException().toString().contains("A network error")) {
                        iProfileView.showDialogSignInFailue("Có lỗi xảy ra!", "Vui lòng kiểm tra lại đường truyền!", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                iProfileView.hideDialogSignIn();
                            }
                        });
                    }
                }
            }
        });
    }

    public void delayClick() {
        iProfileView.delayClickUser(500);
        iProfileView.delayClickSignOut(500);
        iProfileView.delayClickGoogleSignIn(500);
        iProfileView.delayClickFacebookSignIn(500);
        iProfileView.delayClickSupport(500);
        iProfileView.delayClickInformation(500);
    }

    @Override
    public void handleDestroy() {
        firebaseAuth.removeAuthStateListener(authStateListener);
    }
}