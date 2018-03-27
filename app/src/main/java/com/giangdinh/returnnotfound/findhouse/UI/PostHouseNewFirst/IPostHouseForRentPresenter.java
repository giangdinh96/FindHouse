package com.giangdinh.returnnotfound.findhouse.UI.PostHouseNewFirst;

import android.content.Intent;
import android.view.View;

import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;

/**
 * Created by GiangDinh on 29/01/2018.
 */

public interface IPostHouseForRentPresenter {
    void getProvinces();

    void handleHousePictureClick(View view);

    void handleProvinceSelected(Province province, int position);

    void handleTownSelected(Town town, int position);

    void handleGetCurrentHouseLocation(boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    void handleChooseHouseLocationClick();

    void handleOnActivityResult(int requestCode, int resultCode, Intent data);

    void handlePostClick(String addressDetail, String price, String stretch, String phone, String email, String describe);
}
