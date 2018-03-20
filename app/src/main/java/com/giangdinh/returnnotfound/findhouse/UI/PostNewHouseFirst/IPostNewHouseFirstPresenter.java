package com.giangdinh.returnnotfound.findhouse.UI.PostNewHouseFirst;

import android.content.Intent;
import android.view.View;

import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;

/**
 * Created by GiangDinh on 29/01/2018.
 */

public interface IPostNewHouseFirstPresenter {
    public void getProvinces();

    public void handleHousePictureClick(View view);

    public void handleProvinceSelected(Province province, int position);

    public void handleTownSelected(Town town, int position);

    public void handleGetCurrentHouseLocation(boolean isNeedZoom, boolean isNeedAnimate, int zoom);

    public void handleChooseHouseLocationClick();

    public void handleOnActivityResult(int requestCode, int resultCode, Intent data);

    public void handlePostClick(String addressDetail, String price, String stretch, String phone, String email, String describe);
}
