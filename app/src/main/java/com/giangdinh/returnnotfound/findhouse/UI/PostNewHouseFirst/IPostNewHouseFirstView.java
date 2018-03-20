package com.giangdinh.returnnotfound.findhouse.UI.PostNewHouseFirst;

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

public interface IPostNewHouseFirstView {

    public void showDialogPostSuccess(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener);

    public void showDialogPostLoading(String title, String content);

    public void showDialogPostFailue(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener);

    public void hideDialogPost();

    public void setImagePictureFirst(Uri uri);

    public void setImagePictureSecond(Uri uri);

    public void setImagePictureThird(Uri uri);

    public void setImagePictureFourth(Uri uri);

    public void setImagePictureFifth(Uri uri);

    public void setImagePictureSixth(Uri uri);

    public void loadSpinnerProvinces(ArrayList<Province> provinces);

    public void loadSpinnerTowns(ArrayList<Town> towns);

    public void initMapView(Bundle savedInstanceState);

    public void addHouseMarker(LatLng latLng);

    public void removeHouseMarker();

    public void moveCamera(LatLng latLng, boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    public void finishActivity();
}
