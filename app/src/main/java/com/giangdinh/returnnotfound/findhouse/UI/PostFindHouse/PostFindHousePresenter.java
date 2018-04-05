package com.giangdinh.returnnotfound.findhouse.UI.PostFindHouse;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.giangdinh.returnnotfound.findhouse.Model.Address;
import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;
import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;
import com.giangdinh.returnnotfound.findhouse.UI.ChooseHouseLocation.ChooseHouseLocationActivity;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.InternetUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.giangdinh.returnnotfound.findhouse.UI.PostHouseForRent.PostHouseForRentActivity.RC_LATLNG;
import static com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal.EXTRA_LAT;
import static com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal.EXTRA_LNG;

/**
 * Created by GiangDinh on 05/04/2018.
 */

public class PostFindHousePresenter implements IPostFindHousePresenter {
    private Activity activity;
    private IPostFindHouseView iPostFindHouseView;
    private FindHouse findHouse;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceFindHouse;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    public PostFindHousePresenter(IPostFindHouseView iPostFindHouseView) {
        this.iPostFindHouseView = iPostFindHouseView;
        this.activity = (Activity) iPostFindHouseView;
        initFirebase();
        initHouse();
    }

    public void initFirebase() {
        firebaseDatabase = FirebaseUtils.getDatabase();
        databaseReferenceFindHouse = firebaseDatabase.getReference().child("news/findHouse").push();
    }

    public void initHouse() {
        findHouse = new FindHouse();
        findHouse.setId(databaseReferenceFindHouse.getKey());
    }

    @Override
    public void getProvinces() {
        final ArrayList<Province> provinces = new ArrayList<>();
        DatabaseReference databaseReferenceProvinces = firebaseDatabase.getReference().child("provinces");

        databaseReferenceProvinces.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                provinces.addAll(FirebaseUtils.getProvinces(dataSnapshot));
                iPostFindHouseView.loadSpinnerProvinces(provinces);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Test", "Load Province Error");
            }
        });
    }

    @Override
    public void handleProvinceSelected(Province province, int position) {
        if (position == 0) {
            findHouse.getAddress().setProvinceId(null);
        } else {
            findHouse.getAddress().setProvinceId(province.getId());
        }
        iPostFindHouseView.loadSpinnerTowns(province.getTowns());
    }

    @Override
    public void handleTownSelected(Town town, int position) {
        if (position == 0) {
            findHouse.getAddress().setTownId(null);
        } else {
            findHouse.getAddress().setTownId(town.getId());
        }
    }

    @Override
    public void handleGetCurrentHouseLocation(final boolean isNeedZoom, final boolean isNeedAnimate, final int zoom) {
        iPostFindHouseView.removeHouseMarker();
        if (findHouse.getLatitude() == 0 && findHouse.getLongitude() == 0) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            // Get lastLocation
            mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        LatLng currentHousePosition = new LatLng(location.getLatitude(), location.getLongitude());
                        iPostFindHouseView.addHouseMarker(currentHousePosition);
                        iPostFindHouseView.moveCamera(currentHousePosition, isNeedZoom, isNeedAnimate, zoom);
                        findHouse.setLatitude(currentHousePosition.latitude);
                        findHouse.setLongitude(currentHousePosition.longitude);
                    } else {
                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                    }
                }
            });

            // If getLastLocation fail use requestLocationUpdate
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Toast.makeText(activity, "onLocationResult", Toast.LENGTH_SHORT).show();
                    super.onLocationResult(locationResult);
                    LatLng currentHousePosition = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    iPostFindHouseView.addHouseMarker(currentHousePosition);
                    iPostFindHouseView.moveCamera(currentHousePosition, isNeedZoom, isNeedAnimate, zoom);
                    findHouse.setLatitude(currentHousePosition.latitude);
                    findHouse.setLongitude(currentHousePosition.longitude);
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                }
            };

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        } else {
            LatLng currentHousePosition = new LatLng(findHouse.getLatitude(), findHouse.getLongitude());
            iPostFindHouseView.addHouseMarker(currentHousePosition);
            iPostFindHouseView.moveCamera(currentHousePosition, isNeedZoom, isNeedAnimate, zoom);
        }
    }

    @Override
    public void handleChooseHouseLocationClick() {
        Intent intentChooseHouseLocationActivity = new Intent(activity, ChooseHouseLocationActivity.class);
        intentChooseHouseLocationActivity.putExtra(EXTRA_LAT, findHouse.getLatitude());
        intentChooseHouseLocationActivity.putExtra(EXTRA_LNG, findHouse.getLongitude());
        activity.startActivityForResult(intentChooseHouseLocationActivity, PostFindHouseActivity.RC_LATLNG);
    }

    @Override
    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_LATLNG:
                if (resultCode == RESULT_OK) {
                    double latitudeHouse = data.getDoubleExtra(EXTRA_LAT, 0);
                    double longitudeHouse = data.getDoubleExtra(EXTRA_LNG, 0);
                    findHouse.setLatitude(latitudeHouse);
                    findHouse.setLongitude(longitudeHouse);
                    handleGetCurrentHouseLocation(true, true, 14);
                }
                break;
        }
    }

    @Override
    public void handlePostClick(String addressDetail, String price, String stretch, String phone, String email, String describe) {
        if (!InternetUtils.isNetworkConnected(activity)) {
            Toast.makeText(activity, "Kết nối không ổn định, xin thử lại!", Toast.LENGTH_SHORT).show();
            return;
        }

        iPostFindHouseView.showDialogPostLoading("Đăng tin", "Đang đăng tin...");

        String error = checkError(findHouse.getAddress(), addressDetail, price, stretch, phone, email, describe);
        if (error != null) {
            iPostFindHouseView.showDialogPostFailue("Có lỗi xảy ra!", error, new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    iPostFindHouseView.hideDialogPost();
                }
            });
            return;
        }

        findHouse.getAddress().setDetail(addressDetail);
        findHouse.setDescription(describe);
        findHouse.setEmail(email);
        findHouse.setPhone(phone);
        findHouse.setPrice(Long.parseLong(price));
        findHouse.setStretch(Double.parseDouble(stretch));
        findHouse.setUserId(FirebaseUtils.getCurrentUserId());

        // Handle upload
        handleUpload();
    }

    public void handleUpload() {
        uploadDatabase();
    }

    public void uploadDatabase() {
        findHouse.setPubDate(-new Date().getTime());
        databaseReferenceFindHouse.setValue(findHouse).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    iPostFindHouseView.showDialogPostSuccess("Đăng tin", "Đăng tin thành công", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            iPostFindHouseView.finishActivity();
                        }
                    });
                } else {
                    iPostFindHouseView.showDialogPostFailue("Có lỗi xảy ra!", "" + task.getException(), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            iPostFindHouseView.hideDialogPost();
                        }
                    });
                }
            }
        });
    }

    // Check
    public String checkError(Address address, String addressDetail, String price, String stretch, String phone, String email, String describe) {
        StringBuilder error = new StringBuilder("");
        if (address.getProvinceId() == null) {
            error.append("\n\t- Bạn chưa chọn tỉnh thành!");
        }
        if (address.getTownId() == null) {
            error.append("\n\t- Bạn chưa chọn huyện!");
        }
        if (addressDetail == null || addressDetail.trim().equals("")) {
            error.append("\n\t- Bạn chưa nhập địa chỉ chi tiết!");
        }
        if (price == null || price.trim().equals("")) {
            error.append("\n\t- Bạn chưa nhập giá phòng!");
        } else {
            try {
                long pri = Long.parseLong(price);
                if (pri <= 0) {
                    error.append("\n\t- Bạn phải nhập số tiền lớn hơn 0!");
                } else if (pri < 200000)
                    error.append("\n\t- Bạn nhập số tiến quá bé!");
            } catch (NumberFormatException ex) {
                error.append("\n\t- Bạn phải nhập giá phòng là một con số!");
            }
        }

        if (stretch == null || stretch.trim().equals("")) {
            error.append("\n\t- Bạn chưa nhập diện tích!");
        } else {
            try {
                double stret = Double.parseDouble(stretch);
                if (stret <= 0) {
                    error.append("\n\t- Bạn phải nhập diện tích lớn hơn 0!");
                } else if (stret < 5)
                    error.append("\n\t- Bạn nhập diện tích quá bé!");
            } catch (NumberFormatException ex) {
                error.append("\n\t- Bạn phải nhập Diện tích là một con số!");
            }
        }

        if (phone == null || phone.trim().equals("")) {
            error.append("\n\t- Bạn chưa nhập số điện thoại!");
        }
        if (email == null || email.trim().equals("")) {
            error.append("\n\t- Bạn chưa nhập email!");
        }
        if (describe == null || describe.trim().equals("")) {
            error.append("\n\t- Bạn chưa nhập mô tả!");
        }

        if (error.toString().equals("")) {
            return null;
        }
        return String.valueOf(error);
    }
}
