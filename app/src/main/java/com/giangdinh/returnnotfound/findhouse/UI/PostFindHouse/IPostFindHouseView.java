package com.giangdinh.returnnotfound.findhouse.UI.PostFindHouse;

import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;
import com.google.android.gms.maps.model.LatLng;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;

/**
 * Created by GiangDinh on 05/04/2018.
 */

public interface IPostFindHouseView {
    void showDialogPostSuccess(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener);

    void showDialogPostLoading(String title, String content);

    void showDialogPostFailue(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener);

    void hideDialogPost();

    void loadSpinnerProvinces(ArrayList<Province> provinces);

    void loadSpinnerTowns(ArrayList<Town> towns);

    void addHouseMarker(LatLng latLng);

    void removeHouseMarker();

    void moveCamera(LatLng latLng, boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    void finishActivity();
}
