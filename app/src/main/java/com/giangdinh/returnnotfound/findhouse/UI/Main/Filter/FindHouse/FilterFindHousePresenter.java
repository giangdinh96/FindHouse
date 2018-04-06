package com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.FindHouse;

import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.TextUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

/**
 * Created by GiangDinh on 05/04/2018.
 */

public class FilterFindHousePresenter implements IFilterFindHousePresenter {

    private IFilterFindHouseView iFilterFindHouseView;
    private ArrayList<Province> provinces;
    private boolean isProvinceFirstLoad = true;
    private boolean isTownFirstLoad = true;
    private Province provinceHawk = Hawk.get(FilterFindHouseFragment.HAWK_PROVINCE);
    private Town townHawk = Hawk.get(FilterFindHouseFragment.HAWK_TOWN);
    private boolean isLoadProvinceHawk = false;

    public FilterFindHousePresenter(IFilterFindHouseView iFilterFindHouseView) {
        this.iFilterFindHouseView = iFilterFindHouseView;
    }

    @Override
    public void getProvinces() {
        DatabaseReference databaseReferenceProvinces = FirebaseUtils.getDatabase().getReference().child("provinces");

        databaseReferenceProvinces.orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                provinces = FirebaseUtils.getProvinces(dataSnapshot);
                iFilterFindHouseView.loadSpinnerProvinces(provinces);
                if (provinceHawk != null) {
                    isLoadProvinceHawk = true;
                    iFilterFindHouseView.selectProvince(provinceHawk);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void handleProvinceSelected(Province province, int position) {
        if (isProvinceFirstLoad) {
            isProvinceFirstLoad = false;
        } else {
            iFilterFindHouseView.loadSpinnerTowns(province.getTowns());
            if (isLoadProvinceHawk) {
                isLoadProvinceHawk = false;
                if (townHawk != null) {
                    iFilterFindHouseView.selectTown(townHawk);
                }
            } else {
                if (position == 0) {
                    Hawk.put(FilterFindHouseFragment.HAWK_PROVINCE, null);
                } else {
                    Hawk.put(FilterFindHouseFragment.HAWK_PROVINCE, province);
                }
            }
        }
    }

    @Override
    public void handleTownSelected(Town town, int position) {
        if (isTownFirstLoad) {
            isTownFirstLoad = false;
        } else {
            if (position == 0) {
                Hawk.put(FilterFindHouseFragment.HAWK_TOWN, null);
            } else {
                Hawk.put(FilterFindHouseFragment.HAWK_TOWN, town);
            }
        }
    }

    @Override
    public void handlePriceChange(Number minValue, Number maxValue) {
        int min = Integer.parseInt(String.valueOf(minValue));
        int max = Integer.parseInt(String.valueOf(maxValue));

        String result = "";
        if (min == FilterFindHouseFragment.MIN_PRICE && max == FilterFindHouseFragment.MAX_PRICE) {
            result = "Tất cả";
        } else if (min == FilterFindHouseFragment.MIN_PRICE) {
            result = "Dưới " + TextUtils.formatPrice(String.valueOf(max));
        } else if (max == FilterFindHouseFragment.MAX_PRICE) {
            result = "Trên " + TextUtils.formatPrice(String.valueOf(min));
        } else if (min == max) {
            result = TextUtils.formatPrice(String.valueOf(min));
        } else {
            result = TextUtils.formatPrice(String.valueOf(min)) + " - " + TextUtils.formatPrice(String.valueOf(max));
        }
        iFilterFindHouseView.setPrice(result);
    }

    @Override
    public void handlePriceFinalChange(Number minValue, Number maxValue) {
        int hawkMinPrice = Integer.parseInt(String.valueOf(minValue));
        int hawkMaxPrice = Integer.parseInt(String.valueOf(maxValue));
        Hawk.put(FilterFindHouseFragment.HAWK_MIN_START_PRICE, hawkMinPrice);
        Hawk.put(FilterFindHouseFragment.HAWK_MAX_START_PRICE, hawkMaxPrice);
    }

    @Override
    public void handleStretchChange(Number minValue, Number maxValue) {
        int min = Integer.parseInt(String.valueOf(minValue));
        int max = Integer.parseInt(String.valueOf(maxValue));

        String result = "";
        if (min == FilterFindHouseFragment.MIN_STRETCH && max == FilterFindHouseFragment.MAX_STRETCH) {
            result = "Tất cả";
        } else if (min == FilterFindHouseFragment.MIN_STRETCH) {
            result = "Dưới " + TextUtils.formatStretch(String.valueOf(max));
        } else if (max == FilterFindHouseFragment.MAX_STRETCH) {
            result = "Trên " + TextUtils.formatStretch(String.valueOf(min));
        } else if (min == max) {
            result = TextUtils.formatStretch(String.valueOf(min));
        } else {
            result = TextUtils.formatStretch(String.valueOf(min)) + " - " + TextUtils.formatStretch(String.valueOf(max));
        }
        iFilterFindHouseView.setStretch(result);
    }

    @Override
    public void handleStretchFinalChange(Number minValue, Number maxValue) {
        int hawkMinStretch = Integer.parseInt(String.valueOf(minValue));
        int hawkMaxStretch = Integer.parseInt(String.valueOf(maxValue));
        Hawk.put(FilterFindHouseFragment.HAWK_MIN_START_STRETCH, hawkMinStretch);
        Hawk.put(FilterFindHouseFragment.HAWK_MAX_START_STRETCH, hawkMaxStretch);
    }

    @Override
    public void handlePubDateChange(Number minValue) {
        int min = Integer.parseInt(String.valueOf(minValue));
        String result = "";
        if (min != FilterFindHouseFragment.MAX_PUBDATE) {
            result = "Cách đây " + min + " ngày";
        } else {
            result = "Tất cả";
        }
        iFilterFindHouseView.setPubDate(result);
    }

    @Override
    public void handlePubDateFinalChange(Number minValue) {
        int hawkMinPubDate = Integer.parseInt(String.valueOf(minValue));
        Hawk.put(FilterFindHouseFragment.HAWK_MIN_START_PUBDATE, hawkMinPubDate);
    }
}
