package com.giangdinh.returnnotfound.findhouse.UI.FindHouseDetail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;
import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;
import com.giangdinh.returnnotfound.findhouse.UI.MapDetailActivity.MapDetailActivity;
import com.giangdinh.returnnotfound.findhouse.Utils.DateUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.TextUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by GiangDinh on 06/04/2018.
 */

public class FindHouseDetailPresenter implements IFindHouseDetailPresenter {
    private Context context;
    private IFindHouseDetailView iFindHouseDetailView;
    private FindHouse findHouse;
    private DatabaseReference databaseReferenceUsersLike;

    public FindHouseDetailPresenter(IFindHouseDetailView iFindHouseDetailView) {
        this.context = (Context) iFindHouseDetailView;
        this.iFindHouseDetailView = iFindHouseDetailView;
    }

    private ValueEventListener valueEventListenerUsersLike;

    @Override
    public void handleReceiveFindHouse(FindHouse findHouse) {
        this.findHouse = findHouse;
        this.databaseReferenceUsersLike = FirebaseUtils.getDatabase().getReference().child("news/findHouse/" + findHouse.getId() + "/usersLike");
        setData();
    }

    public void setData() {
        iFindHouseDetailView.setTime(DateUtils.getDateTimeAgoString(-findHouse.getPubDate()));
        iFindHouseDetailView.setPrice(String.valueOf(findHouse.getPrice()));
        iFindHouseDetailView.setStretch(String.valueOf(findHouse.getStretch()));
        iFindHouseDetailView.setPhone(findHouse.getPhone());
        iFindHouseDetailView.setEmail(findHouse.getEmail());
        iFindHouseDetailView.setDescribe(findHouse.getDescription());

        // Set userPicture
        if (findHouse.getUserPicture() != null) {
            iFindHouseDetailView.setUserPicture(findHouse.getUserPicture());
        } else {
            FirebaseUtils.getDatabase().getReference().child("users").child(findHouse.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                        HashMap<String, Object> hashMapUser = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (hashMapUser.get("photoUrl") != null) {
                            String userPicture = hashMapUser.get("photoUrl").toString();
                            iFindHouseDetailView.setUserPicture(userPicture);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        // Set username
        if (findHouse.getUserName() != null) {
            iFindHouseDetailView.setUserName(findHouse.getUserName());
        } else {
            FirebaseUtils.getDatabase().getReference().child("users").child(findHouse.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                        HashMap<String, Object> hashMapUser = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (hashMapUser.get("name") != null) {
                            String userName = hashMapUser.get("name").toString();
                            iFindHouseDetailView.setUserName(userName);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        // set address
        if (findHouse.getFullAddress() != null) {
            iFindHouseDetailView.setAddress(findHouse.getFullAddress());
        } else {
            FirebaseUtils.getDatabase().getReference().child("provinces").child(findHouse.getAddress().getProvinceId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null & dataSnapshot.hasChildren()) {
                        HashMap<String, Object> hashMapProvince = (HashMap<String, Object>) dataSnapshot.getValue();
                        String provinceName = hashMapProvince.get("name").toString();
                        HashMap<String, Object> hashMapTowns = (HashMap<String, Object>) hashMapProvince.get("towns");
                        HashMap<String, Object> hashMapTown = (HashMap<String, Object>) hashMapTowns.get(findHouse.getAddress().getTownId());
                        String townName = (String) hashMapTown.get("name");
                        String detail = findHouse.getAddress().getDetail();
                        String address = String.format("%s, %s, %s", detail, townName, provinceName);
                        iFindHouseDetailView.setAddress(address);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        // set usersLike
        if (findHouse.getUsersLike().size() <= 0) {
            iFindHouseDetailView.hideDivider3();
            iFindHouseDetailView.hideUsersLike();
            iFindHouseDetailView.hideDivider4();
            iFindHouseDetailView.setLikeSelected(false);
        } else {
            if (FirebaseUtils.isSignIn() && findHouse.getUsersLike().containsKey(FirebaseUtils.getCurrentUserId())) {
                iFindHouseDetailView.setLikeSelected(true);
            } else {
                iFindHouseDetailView.setLikeSelected(false);
            }
            iFindHouseDetailView.setUsersLike(findHouse.getUsersLike().size() + " lượt thích");
            iFindHouseDetailView.showDivider3();
            iFindHouseDetailView.showUsersLike();
            iFindHouseDetailView.showDivider4();
        }

        valueEventListenerUsersLike = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    iFindHouseDetailView.hideDivider3();
                    iFindHouseDetailView.hideUsersLike();
                    iFindHouseDetailView.hideDivider4();
                    iFindHouseDetailView.setLikeSelected(false);
                    findHouse.getUsersLike().clear();
                    return;
                }
                findHouse.setUsersLike((HashMap<String, Object>) dataSnapshot.getValue());
                if (FirebaseUtils.isSignIn() && findHouse.getUsersLike().containsKey(FirebaseUtils.getCurrentUserId())) {
                    iFindHouseDetailView.setLikeSelected(true);
                } else {
                    iFindHouseDetailView.setLikeSelected(false);
                }
                iFindHouseDetailView.setUsersLike(findHouse.getUsersLike().size() + " lượt thích");
                iFindHouseDetailView.showDivider3();
                iFindHouseDetailView.showUsersLike();
                iFindHouseDetailView.showDivider4();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReferenceUsersLike.addValueEventListener(valueEventListenerUsersLike);
    }

    @Override
    public void handleCallClick(FindHouse findHouse) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + findHouse.getPhone()));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(callIntent);
    }

    @Override
    public void handleLikeClick(final FindHouse findHouse) {
        if (FirebaseUtils.isSignIn()) {
            if (findHouse.getUsersLike().containsKey(FirebaseUtils.getCurrentUserId()))
                databaseReferenceUsersLike.child(FirebaseUtils.getCurrentUserId())
                        .setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Error UnLike In HouseDetail", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            else {
                databaseReferenceUsersLike.child(FirebaseUtils.getCurrentUserId())
                        .setValue(FirebaseUtils.getCurrentUserId()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Error Like In HouseDetail", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });
            }
        } else {
            Toast.makeText(context, "Bạn cần đăng nhập để sử dụng tính năng này!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void handleShareClick(FindHouse findHouse) {
        StringBuilder textShare = new StringBuilder();
        textShare.append("Địa chỉ: ").append(findHouse.getFullAddress()).append("\n")
                .append("Giá phòng: ").append(TextUtils.formatPrice(findHouse.getPrice())).append("\n")
                .append("Diện tích: ").append(TextUtils.formatStretch(findHouse.getStretch())).append("\n")
                .append("Người đăng: ").append(findHouse.getUserName()).append("\n")
                .append("Email: ").append(findHouse.getEmail()).append("\n")
                .append("Điện thoại: ").append(findHouse.getPhone()).append("\n");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(textShare));
        shareIntent.setType("text/plain");
        context.startActivity(shareIntent);
    }

    @Override
    public void handleMapDetailClick() {
        Intent mapDetailIntent = new Intent(context, MapDetailActivity.class);
        mapDetailIntent.putExtra(VariableGlobal.EXTRA_LAT, findHouse.getLatitude());
        mapDetailIntent.putExtra(VariableGlobal.EXTRA_LNG, findHouse.getLongitude());
        context.startActivity(mapDetailIntent);
    }

    @Override
    public void handleDestroy() {
        removeLikeEvent();
    }

    public void removeLikeEvent() {
        databaseReferenceUsersLike.removeEventListener(valueEventListenerUsersLike);
    }

}
