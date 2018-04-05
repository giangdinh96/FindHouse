package com.giangdinh.returnnotfound.findhouse.UI.PostFindHouse;

import android.content.Intent;

import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;

/**
 * Created by GiangDinh on 05/04/2018.
 */

public interface IPostFindHousePresenter {
    void getProvinces();

    void handleProvinceSelected(Province province, int position);

    void handleTownSelected(Town town, int position);

    void handleGetCurrentHouseLocation(boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    void handleChooseHouseLocationClick();

    void handleOnActivityResult(int requestCode, int resultCode, Intent data);

    void handlePostClick(String addressDetail, String price, String stretch, String phone, String email, String describe);
}
