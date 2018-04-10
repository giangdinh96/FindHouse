package com.giangdinh.returnnotfound.findhouse.UI.HouseForRentDetail;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

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
 * Created by GiangDinh on 28/01/2018.
 */

public class HouseForRentDetailPresenter implements IHouseForRentDetailPresenter {
    private Context context;
    private IHouseForRentDetailView iHouseForRentDetailView;
    private HouseForRent houseForRent;
    private DatabaseReference databaseReferenceUsersLike;

    public HouseForRentDetailPresenter(IHouseForRentDetailView iHouseForRentDetailView) {
        this.context = (Context) iHouseForRentDetailView;
        this.iHouseForRentDetailView = iHouseForRentDetailView;
    }

    private ValueEventListener valueEventListenerUsersLike;

    @Override
    public void handleReceiveHouseForRent(HouseForRent houseForRent) {
        this.houseForRent = houseForRent;
        this.databaseReferenceUsersLike = FirebaseUtils.getDatabase().getReference().child("news/houseForRent/" + houseForRent.getId() + "/usersLike");
        setData();
    }

    public void setData() {
        iHouseForRentDetailView.setTime(DateUtils.getDateTimeAgoString(-houseForRent.getPubDate()));
        iHouseForRentDetailView.setPrice(String.valueOf(houseForRent.getPrice()));
        iHouseForRentDetailView.setStretch(String.valueOf(houseForRent.getStretch()));
        iHouseForRentDetailView.setPhone(houseForRent.getPhone());
        iHouseForRentDetailView.setEmail(houseForRent.getEmail());
        iHouseForRentDetailView.setDescribe(houseForRent.getDescription());

        // Set userPicture
        if (houseForRent.getUserPicture() != null) {
            iHouseForRentDetailView.setUserPicture(houseForRent.getUserPicture());
        } else {
            FirebaseUtils.getDatabase().getReference().child("users").child(houseForRent.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                        HashMap<String, Object> hashMapUser = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (hashMapUser.get("photoUrl") != null) {
                            String userPicture = hashMapUser.get("photoUrl").toString();
                            iHouseForRentDetailView.setUserPicture(userPicture);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        // Set username
        if (houseForRent.getUserName() != null) {
            iHouseForRentDetailView.setUserName(houseForRent.getUserName());
        } else {
            FirebaseUtils.getDatabase().getReference().child("users").child(houseForRent.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                        HashMap<String, Object> hashMapUser = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (hashMapUser.get("name") != null) {
                            String userName = hashMapUser.get("name").toString();
                            iHouseForRentDetailView.setUserName(userName);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        // set address
        if (houseForRent.getFullAddress() != null) {
            iHouseForRentDetailView.setAddress(houseForRent.getFullAddress());
        } else {
            FirebaseUtils.getDatabase().getReference().child("provinces").child(houseForRent.getAddress().getProvinceId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null & dataSnapshot.hasChildren()) {
                        HashMap<String, Object> hashMapProvince = (HashMap<String, Object>) dataSnapshot.getValue();
                        String provinceName = hashMapProvince.get("name").toString();
                        HashMap<String, Object> hashMapTowns = (HashMap<String, Object>) hashMapProvince.get("towns");
                        HashMap<String, Object> hashMapTown = (HashMap<String, Object>) hashMapTowns.get(houseForRent.getAddress().getTownId());
                        String townName = (String) hashMapTown.get("name");
                        String detail = houseForRent.getAddress().getDetail();
                        String address = String.format("%s, %s, %s", detail, townName, provinceName);
                        iHouseForRentDetailView.setAddress(address);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

        // set usersLike
        if (houseForRent.getUsersLike().size() <= 0) {
            iHouseForRentDetailView.hideDivider3();
            iHouseForRentDetailView.hideUsersLike();
            iHouseForRentDetailView.hideDivider4();
            iHouseForRentDetailView.setLikeSelected(false);
        } else {
            if (FirebaseUtils.isSignIn() && houseForRent.getUsersLike().containsKey(FirebaseUtils.getCurrentUserId())) {
                iHouseForRentDetailView.setLikeSelected(true);
            } else {
                iHouseForRentDetailView.setLikeSelected(false);
            }
            iHouseForRentDetailView.setUsersLike(houseForRent.getUsersLike().size() + " lượt thích");
            iHouseForRentDetailView.showDivider3();
            iHouseForRentDetailView.showUsersLike();
            iHouseForRentDetailView.showDivider4();
        }

        valueEventListenerUsersLike = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    iHouseForRentDetailView.hideDivider3();
                    iHouseForRentDetailView.hideUsersLike();
                    iHouseForRentDetailView.hideDivider4();
                    iHouseForRentDetailView.setLikeSelected(false);
                    houseForRent.getUsersLike().clear();
                    return;
                }
                houseForRent.setUsersLike((HashMap<String, Object>) dataSnapshot.getValue());
                if (FirebaseUtils.isSignIn() && houseForRent.getUsersLike().containsKey(FirebaseUtils.getCurrentUserId())) {
                    iHouseForRentDetailView.setLikeSelected(true);
                } else {
                    iHouseForRentDetailView.setLikeSelected(false);
                }
                iHouseForRentDetailView.setUsersLike(houseForRent.getUsersLike().size() + " lượt thích");
                iHouseForRentDetailView.showDivider3();
                iHouseForRentDetailView.showUsersLike();
                iHouseForRentDetailView.showDivider4();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReferenceUsersLike.addValueEventListener(valueEventListenerUsersLike);
    }

    @Override
    public void handleCallClick(HouseForRent houseForRent) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + houseForRent.getPhone()));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(callIntent);
    }

    @Override
    public void handleLikeClick(final HouseForRent houseForRent) {
        if (FirebaseUtils.isSignIn()) {
            if (houseForRent.getUsersLike().containsKey(FirebaseUtils.getCurrentUserId()))
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
    public void handleShareClick(HouseForRent houseForRent) {
        StringBuilder textShare = new StringBuilder();
        textShare.append("Địa chỉ: ").append(houseForRent.getFullAddress()).append("\n")
                .append("Giá phòng: ").append(TextUtils.formatPrice(houseForRent.getPrice())).append("\n")
                .append("Diện tích: ").append(TextUtils.formatStretch(houseForRent.getStretch())).append("\n")
                .append("Người đăng: ").append(houseForRent.getUserName()).append("\n")
                .append("Email: ").append(houseForRent.getEmail()).append("\n")
                .append("Điện thoại: ").append(houseForRent.getPhone()).append("\n");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(textShare));
        shareIntent.setType("text/plain");
        context.startActivity(shareIntent);
    }

    @Override
    public void handleMapDetailClick() {
        Intent mapDetailIntent = new Intent(context, MapDetailActivity.class);
        mapDetailIntent.putExtra(VariableGlobal.EXTRA_LAT, houseForRent.getLatitude());
        mapDetailIntent.putExtra(VariableGlobal.EXTRA_LNG, houseForRent.getLongitude());
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
