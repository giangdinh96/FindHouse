package com.giangdinh.returnnotfound.findhouse.UI.PostNewHouseFirst;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.giangdinh.returnnotfound.findhouse.Model.Address;
import com.giangdinh.returnnotfound.findhouse.Model.NewHouseFirst;
import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;
import com.giangdinh.returnnotfound.findhouse.R;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.giangdinh.returnnotfound.findhouse.UI.PostNewHouseFirst.PostNewHouseFirstActivity.RC_LATLNG;
import static com.giangdinh.returnnotfound.findhouse.UI.PostNewHouseFirst.PostNewHouseFirstActivity.RC_PICK_1;
import static com.giangdinh.returnnotfound.findhouse.UI.PostNewHouseFirst.PostNewHouseFirstActivity.RC_PICK_2;
import static com.giangdinh.returnnotfound.findhouse.UI.PostNewHouseFirst.PostNewHouseFirstActivity.RC_PICK_3;
import static com.giangdinh.returnnotfound.findhouse.UI.PostNewHouseFirst.PostNewHouseFirstActivity.RC_PICK_4;
import static com.giangdinh.returnnotfound.findhouse.UI.PostNewHouseFirst.PostNewHouseFirstActivity.RC_PICK_5;
import static com.giangdinh.returnnotfound.findhouse.UI.PostNewHouseFirst.PostNewHouseFirstActivity.RC_PICK_6;
import static com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal.EXTRA_LAT;
import static com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal.EXTRA_LNG;

/**
 * Created by GiangDinh on 29/01/2018.
 */

public class PostNewHouseFirstPresenter implements IPostNewHouseFirstPresenter {
    private Activity activity;
    private IPostNewHouseFirstView iPostNewHouseFirstView;

    private HashMap<String, Uri> housePictureUri;
    private NewHouseFirst newHouseFirst;

    private boolean isUploadPictureComplete[];
    private boolean isUploadPictureFailue;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceNewsHouseFirst;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private StringBuilder errorUploadPicture;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    public PostNewHouseFirstPresenter(IPostNewHouseFirstView iPostNewHouseFirstView) {
        this.iPostNewHouseFirstView = iPostNewHouseFirstView;
        this.activity = (Activity) iPostNewHouseFirstView;
        setupFirebase();
        setupHouse();
    }

    public void setupFirebase() {
        firebaseDatabase = FirebaseUtils.getDatabase();
        databaseReferenceNewsHouseFirst = firebaseDatabase.getReference().child("news/newsHouseFirst").push();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    public void setupHouse() {
        housePictureUri = new HashMap<>();
        newHouseFirst = new NewHouseFirst();
        newHouseFirst.setId(databaseReferenceNewsHouseFirst.getKey());
    }

    @Override
    public void getProvinces() {
        final ArrayList<Province> provinces = new ArrayList<>();
        DatabaseReference databaseReferenceProvinces = firebaseDatabase.getReference().child("provinces");

        databaseReferenceProvinces.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                provinces.addAll(FirebaseUtils.getProvinces(dataSnapshot));
                iPostNewHouseFirstView.loadSpinnerProvinces(provinces);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Test", "Load Province Error");
            }
        });
    }

    @Override
    public void handleHousePictureClick(View view) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        switch (view.getId()) {
            case R.id.ivHousePictureFirst:
                activity.startActivityForResult(pickIntent, RC_PICK_1);
                break;
            case R.id.ivHousePictureSecond:
                activity.startActivityForResult(pickIntent, RC_PICK_2);
                break;
            case R.id.ivHousePictureThird:
                activity.startActivityForResult(pickIntent, RC_PICK_3);
                break;
            case R.id.ivHousePictureFourth:
                activity.startActivityForResult(pickIntent, RC_PICK_4);
                break;
            case R.id.ivHousePictureFifth:
                activity.startActivityForResult(pickIntent, RC_PICK_5);
                break;
            case R.id.ivHousePictureSixth:
                activity.startActivityForResult(pickIntent, RC_PICK_6);
                break;
        }
    }

    @Override
    public void handleProvinceSelected(Province province, int position) {
        if (position == 0) {
            newHouseFirst.getAddress().setProvinceId(null);
        } else {
            newHouseFirst.getAddress().setProvinceId(province.getId());
        }
        iPostNewHouseFirstView.loadSpinnerTowns(province.getTowns());
    }

    @Override
    public void handleTownSelected(Town town, int position) {
        if (position == 0) {
            newHouseFirst.getAddress().setTownId(null);
        } else {
            newHouseFirst.getAddress().setTownId(town.getId());
        }
    }

    @Override
    public void handleGetCurrentHouseLocation(final boolean isNeedZoom, final boolean isNeedAnimate, final int zoom) {
        iPostNewHouseFirstView.removeHouseMarker();
        if (newHouseFirst.getLatitude() == 0 && newHouseFirst.getLongitude() == 0) {
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
                        iPostNewHouseFirstView.addHouseMarker(currentHousePosition);
                        iPostNewHouseFirstView.moveCamera(currentHousePosition, isNeedZoom, isNeedAnimate, zoom);
                        newHouseFirst.setLatitude(currentHousePosition.latitude);
                        newHouseFirst.setLongitude(currentHousePosition.longitude);
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
                    iPostNewHouseFirstView.addHouseMarker(currentHousePosition);
                    iPostNewHouseFirstView.moveCamera(currentHousePosition, isNeedZoom, isNeedAnimate, zoom);
                    newHouseFirst.setLatitude(currentHousePosition.latitude);
                    newHouseFirst.setLongitude(currentHousePosition.longitude);
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                }
            };

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        } else {
            LatLng currentHousePosition = new LatLng(newHouseFirst.getLatitude(), newHouseFirst.getLongitude());
            iPostNewHouseFirstView.addHouseMarker(currentHousePosition);
            iPostNewHouseFirstView.moveCamera(currentHousePosition, isNeedZoom, isNeedAnimate, zoom);
        }
    }

    @Override
    public void handleChooseHouseLocationClick() {
        Intent intentChooseHouseLocationActivity = new Intent(activity, ChooseHouseLocationActivity.class);
        intentChooseHouseLocationActivity.putExtra(EXTRA_LAT, newHouseFirst.getLatitude());
        intentChooseHouseLocationActivity.putExtra(EXTRA_LNG, newHouseFirst.getLongitude());
        activity.startActivityForResult(intentChooseHouseLocationActivity, PostNewHouseFirstActivity.RC_LATLNG);
    }

    @Override
    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_PICK_1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (housePictureUri.containsValue(uri)) {
                        Toast.makeText(activity, "Bạn đã chọn ảnh đó rồi", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    iPostNewHouseFirstView.setImagePictureFirst(uri);
                    housePictureUri.put("pictureFirst", uri);
                    newHouseFirst.putPicture("pictureFirst");
                }
                break;
            case RC_PICK_2:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (housePictureUri.containsValue(uri)) {
                        Toast.makeText(activity, "Bạn đã chọn ảnh đó rồi", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    iPostNewHouseFirstView.setImagePictureSecond(uri);
                    housePictureUri.put("pictureSecond", uri);
                    newHouseFirst.putPicture("pictureSecond");
                }
                break;
            case RC_PICK_3:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (housePictureUri.containsValue(uri)) {
                        Toast.makeText(activity, "Bạn đã chọn ảnh đó rồi", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    iPostNewHouseFirstView.setImagePictureThird(uri);
                    housePictureUri.put("pictureThird", uri);
                    newHouseFirst.putPicture("pictureThird");
                }
                break;
            case RC_PICK_4:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (housePictureUri.containsValue(uri)) {
                        Toast.makeText(activity, "Bạn đã chọn ảnh đó rồi", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    iPostNewHouseFirstView.setImagePictureFourth(uri);
                    housePictureUri.put("pictureFourth", uri);
                    newHouseFirst.putPicture("pictureFourth");
                }
                break;
            case RC_PICK_5:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (housePictureUri.containsValue(uri)) {
                        Toast.makeText(activity, "Bạn đã chọn ảnh đó rồi", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    iPostNewHouseFirstView.setImagePictureFifth(uri);
                    housePictureUri.put("pictureFifth", uri);
                    newHouseFirst.putPicture("pictureFifth");
                }
                break;
            case RC_PICK_6:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (housePictureUri.containsValue(uri)) {
                        Toast.makeText(activity, "Bạn đã chọn ảnh đó rồi", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    iPostNewHouseFirstView.setImagePictureSixth(uri);
                    housePictureUri.put("pictureSixth", uri);
                    newHouseFirst.putPicture("pictureSixth");
                }
                break;
            case RC_LATLNG:
                if (resultCode == RESULT_OK) {
                    double latitudeHouse = data.getDoubleExtra(EXTRA_LAT, 0);
                    double longitudeHouse = data.getDoubleExtra(EXTRA_LNG, 0);
                    newHouseFirst.setLatitude(latitudeHouse);
                    newHouseFirst.setLongitude(longitudeHouse);
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

        iPostNewHouseFirstView.showDialogPostLoading("Đăng tin", "Đang đăng tin...");

        String error = checkError(newHouseFirst.getPictures(), newHouseFirst.getAddress(), addressDetail, price, stretch, phone, email, describe);
        if (error != null) {
            iPostNewHouseFirstView.showDialogPostFailue("Có lỗi xảy ra!", error, new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    iPostNewHouseFirstView.hideDialogPost();
                }
            });
            return;
        }

        newHouseFirst.getAddress().setDetail(addressDetail);
        newHouseFirst.setDescription(describe);
        newHouseFirst.setEmail(email);
        newHouseFirst.setPhone(phone);
        newHouseFirst.setPrice(Long.parseLong(price));
        newHouseFirst.setStretch(Double.parseDouble(stretch));
        newHouseFirst.setUserId(FirebaseUtils.getCurrentUserId());

        // Handle upload
        handleUpload();
    }

    public void handleUpload() {
        String[] pictureUploadPath = new String[]{
                newHouseFirst.getPictures().get("pictureFirst") == null ? null : newHouseFirst.getPictures().get("pictureFirst").toString(),
                newHouseFirst.getPictures().get("pictureSecond") == null ? null : newHouseFirst.getPictures().get("pictureSecond").toString(),
                newHouseFirst.getPictures().get("pictureThird") == null ? null : newHouseFirst.getPictures().get("pictureThird").toString(),
                newHouseFirst.getPictures().get("pictureFourth") == null ? null : newHouseFirst.getPictures().get("pictureFourth").toString(),
                newHouseFirst.getPictures().get("pictureFifth") == null ? null : newHouseFirst.getPictures().get("pictureFifth").toString(),
                newHouseFirst.getPictures().get("pictureSixth") == null ? null : newHouseFirst.getPictures().get("pictureSixth").toString()
        };

        Uri[] housePictureUriArray = new Uri[]{
                housePictureUri.get("pictureFirst"),
                housePictureUri.get("pictureSecond"),
                housePictureUri.get("pictureThird"),
                housePictureUri.get("pictureFourth"),
                housePictureUri.get("pictureFifth"),
                housePictureUri.get("pictureSixth")
        };
        isUploadPictureComplete = new boolean[6];
        errorUploadPicture = new StringBuilder();

        uploadPicture(pictureUploadPath, housePictureUriArray, 0);
        uploadPicture(pictureUploadPath, housePictureUriArray, 1);
        uploadPicture(pictureUploadPath, housePictureUriArray, 2);
        uploadPicture(pictureUploadPath, housePictureUriArray, 3);
        uploadPicture(pictureUploadPath, housePictureUriArray, 4);
        uploadPicture(pictureUploadPath, housePictureUriArray, 5);
    }

    public void uploadPicture(String[] pictureUploadPath, Uri[] housePictureUriArray, final int position) {
        if (pictureUploadPath[position] == null) {
            isUploadPictureComplete[position] = true;
        } else {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), housePictureUriArray[position]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();

            UploadTask uploadTask = storageReference.child(pictureUploadPath[position]).putBytes(data);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    isUploadPictureComplete[position] = true;
                    if (!task.isSuccessful()) {
                        isUploadPictureFailue = true;
                        errorUploadPicture.append("Tải lên ảnh " + (position + 1) + " thất bại!");
                    } else {
                        Log.d("Test", "Upload picture" + (position + 1) + " success!");
                    }
                    if (checkUploadComplete()) {
                        uploadDatabase();
                    }
                }
            });
        }
    }

    public void uploadDatabase() {
        if (isUploadPictureFailue) {
            iPostNewHouseFirstView.showDialogPostFailue("Có lỗi xảy ra!", String.valueOf(errorUploadPicture), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    iPostNewHouseFirstView.hideDialogPost();
                }
            });
            return;
        }
        newHouseFirst.setPubDate(-new Date().getTime());
        databaseReferenceNewsHouseFirst.setValue(newHouseFirst).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    iPostNewHouseFirstView.showDialogPostSuccess("Đăng tin", "Đăng tin thành công", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            iPostNewHouseFirstView.finishActivity();
                        }
                    });
                } else {
                    iPostNewHouseFirstView.showDialogPostFailue("Có lỗi xảy ra!", "" + task.getException(), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            iPostNewHouseFirstView.hideDialogPost();
                        }
                    });
                }
            }
        });
    }

    // Check
    public String checkError(HashMap<String, Object> pictures, Address address, String addressDetail, String price, String stretch, String phone, String email, String describe) {
        StringBuilder error = new StringBuilder("");
        if (pictures.size() < 1) {
            error.append("\n\t- Bạn phải chọn ít nhất 1 tấm ảnh!");
        }
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

    public boolean checkUploadComplete() {
        return isUploadPictureComplete[0] &&
                isUploadPictureComplete[1] &&
                isUploadPictureComplete[2] &&
                isUploadPictureComplete[3] &&
                isUploadPictureComplete[4] &&
                isUploadPictureComplete[5];
    }
}