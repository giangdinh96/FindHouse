package com.giangdinh.returnnotfound.findhouse.UI.PostHouseForRent;

import android.net.Uri;
import android.os.Bundle;

import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;
import com.google.android.gms.maps.model.LatLng;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;

/**
 * Created by GiangDinh on 29/01/2018.
 */

public interface IPostHouseForRentView {

    void showDialogPostSuccess(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener);

    void showDialogPostLoading(String title, String content);

    void showDialogPostFailue(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener);

    void hideDialogPost();

    void setImagePictureFirst(Uri uri);

    void setImagePictureSecond(Uri uri);

    void setImagePictureThird(Uri uri);

    void setImagePictureFourth(Uri uri);

    void setImagePictureFifth(Uri uri);

    void setImagePictureSixth(Uri uri);

    void loadSpinnerProvinces(ArrayList<Province> provinces);

    void loadSpinnerTowns(ArrayList<Town> towns);

    void initMapView(Bundle savedInstanceState);

    void addHouseMarker(LatLng latLng);

    void removeHouseMarker();

    void moveCamera(LatLng latLng, boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    void finishActivity();
}
