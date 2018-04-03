package com.giangdinh.returnnotfound.findhouse.UI.Main.News.HouseForRent;

import android.os.Handler;
import android.util.Log;

import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;
import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.Date;

/**
 * Created by GiangDinh on 22/03/2018.
 */

public class NewsHouseForRentPresenter implements INewsHouseForRentPresenter {
    private INewsHouseForRentView iNewsHouseForRentView;
    private long timeStartRequest;
    private int newsCount;
    private boolean isRefreshShow;

    // HAWK
    private Province hawkProvince;
    private Town hawkTown;
    private Integer hawkMinStartPrice;
    private Integer hawkMaxStartPrice;
    private Integer hawkMinStartStretch;
    private Integer hawkMaxStartStretch;
    private Integer hawkMinStartPubDate;

    public NewsHouseForRentPresenter(INewsHouseForRentView iNewsHouseForRentView) {
        this.iNewsHouseForRentView = iNewsHouseForRentView;
        this.getHawk();
        this.newsCount = 0;
        this.timeStartRequest = -1;
    }

    public void getHawk() {
//        hawkProvince = Hawk.get(FilterFragment.HAWK_PROVINCE);
//        hawkTown = Hawk.get(FilterFragment.HAWK_TOWN);
//
//        if (Hawk.get(FilterFragment.HAWK_MIN_START_PRICE) != null) {
//            hawkMinStartPrice = Hawk.get(FilterFragment.HAWK_MIN_START_PRICE);
//        } else {
//            hawkMinStartPrice = FilterFragment.MIN_PRICE;
//        }
//
//        if (Hawk.get(FilterFragment.HAWK_MAX_START_PRICE) != null) {
//            hawkMaxStartPrice = Hawk.get(FilterFragment.HAWK_MAX_START_PRICE);
//        } else {
//            hawkMaxStartPrice = FilterFragment.MAX_PRICE;
//        }
//
//        if (Hawk.get(FilterFragment.HAWK_MIN_START_STRETCH) != null) {
//            hawkMinStartStretch = Hawk.get(FilterFragment.HAWK_MIN_START_STRETCH);
//        } else {
//            hawkMinStartStretch = FilterFragment.MIN_STRETCH;
//        }
//
//        if (Hawk.get(FilterFragment.HAWK_MAX_START_STRETCH) != null) {
//            hawkMaxStartStretch = Hawk.get(FilterFragment.HAWK_MAX_START_STRETCH);
//        } else {
//            hawkMaxStartStretch = FilterFragment.MAX_STRETCH;
//        }
//
//        if (Hawk.get(FilterFragment.HAWK_MIN_START_PUBDATE) != null) {
//            hawkMinStartPubDate = Hawk.get(FilterFragment.HAWK_MIN_START_PUBDATE);
//        } else {
//            hawkMinStartPubDate = FilterFragment.MAX_PUBDATE;
//        }

    }

    @Override
    public void handleRefresh() {
        if (isRefreshShow)
            return;
        isRefreshShow = true;
        iNewsHouseForRentView.showRefresh(true);
        getHawk();
        newsCount = 0;
        removeGetHousesEvent();
        iNewsHouseForRentView.refreshList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefreshShow = false;
                iNewsHouseForRentView.showRefresh(false);
                handleGetNews();
            }
        }, 1000);
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.e("Test", "onChildAdded-------New-------" + dataSnapshot.getKey());
            HouseForRent houseForRent = dataSnapshot.getValue(HouseForRent.class);

            // Filter
            // Province
//            if (hawkProvince != null && !houseForRent.getAddress().getProvinceId().equals(hawkProvince.getId())) {
//                return;
//            }
//            // Town
//            if (hawkTown != null && !houseForRent.getAddress().getTownId().equals(hawkTown.getId())) {
//                return;
//            }
//            // Price
//            if (hawkMinStartPrice == FilterFragment.MIN_PRICE && hawkMaxStartPrice == FilterFragment.MAX_PRICE) {
//
//            } else if (hawkMinStartPrice == FilterFragment.MIN_PRICE) {
//                if (houseForRent.getPrice() > hawkMaxStartPrice)
//                    return;
//            } else if (hawkMaxStartPrice == FilterFragment.MAX_PRICE) {
//                if (houseForRent.getPrice() < hawkMinStartPrice)
//                    return;
//            } else if (hawkMinStartPrice == hawkMaxStartPrice) {
//                if (houseForRent.getPrice() != hawkMinStartPrice)
//                    return;
//            } else {
//                if (houseForRent.getPrice() < hawkMinStartPrice || houseForRent.getPrice() > hawkMaxStartPrice)
//                    return;
//            }
//            // Stretch
//            if (hawkMinStartStretch == FilterFragment.MIN_STRETCH && hawkMaxStartStretch == FilterFragment.MAX_STRETCH) {
//
//            } else if (hawkMinStartStretch == FilterFragment.MIN_STRETCH) {
//                if (houseForRent.getStretch() > hawkMaxStartStretch)
//                    return;
//            } else if (hawkMaxStartStretch == FilterFragment.MAX_STRETCH) {
//                if (houseForRent.getStretch() < hawkMinStartStretch)
//                    return;
//            } else if (hawkMinStartStretch == hawkMaxStartStretch) {
//                if (houseForRent.getStretch() != hawkMinStartStretch)
//                    return;
//            } else {
//                if (houseForRent.getStretch() < hawkMinStartStretch || houseForRent.getStretch() > hawkMaxStartStretch)
//                    return;
//            }
//
//            // PubDate
//            if (hawkMinStartPubDate == FilterFragment.MAX_PUBDATE) {
//            } else if (DateUtils.getDayAgoFromPubDate(-houseForRent.getPubDate()) > hawkMinStartPubDate) {
//                return;
//            }

            // Check ok
            // Add to List
            houseForRent.setId(dataSnapshot.getKey());
            if (timeStartRequest >= (-houseForRent.getPubDate())) {
                iNewsHouseForRentView.addItemHouse(houseForRent);
            } else {
                if (FirebaseUtils.isSignIn() && houseForRent.getUserId().equals(FirebaseUtils.getCurrentUserId())) {
                    iNewsHouseForRentView.addItemHouse(0, houseForRent);
                } else {
                    newsCount++;
                    iNewsHouseForRentView.showNotification("Bài đăng mới", "Có " + newsCount + " tin mới");
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.e("Test", "onChildChanged-------New-------" + dataSnapshot.getKey());
            HouseForRent houseForRent = dataSnapshot.getValue(HouseForRent.class);

            // Filter
            // Province
//            if (hawkProvince != null && !houseForRent.getAddress().getProvinceId().equals(hawkProvince.getId())) {
//                return;
//            }
//            // Town
//            if (hawkTown != null && !houseForRent.getAddress().getTownId().equals(hawkTown.getId())) {
//                return;
//            }
//            // Price
//            if (hawkMinStartPrice == FilterFragment.MIN_PRICE && hawkMaxStartPrice == FilterFragment.MAX_PRICE) {
//
//            } else if (hawkMinStartPrice == FilterFragment.MIN_PRICE) {
//                if (houseForRent.getPrice() > hawkMaxStartPrice)
//                    return;
//            } else if (hawkMaxStartPrice == FilterFragment.MAX_PRICE) {
//                if (houseForRent.getPrice() < hawkMinStartPrice)
//                    return;
//            } else if (hawkMinStartPrice == hawkMaxStartPrice) {
//                if (houseForRent.getPrice() != hawkMinStartPrice)
//                    return;
//            } else {
//                if (houseForRent.getPrice() < hawkMinStartPrice || houseForRent.getPrice() > hawkMaxStartPrice)
//                    return;
//            }
//            // Stretch
//            if (hawkMinStartStretch == FilterFragment.MIN_STRETCH && hawkMaxStartStretch == FilterFragment.MAX_STRETCH) {
//
//            } else if (hawkMinStartStretch == FilterFragment.MIN_STRETCH) {
//                if (houseForRent.getStretch() > hawkMaxStartStretch)
//                    return;
//            } else if (hawkMaxStartStretch == FilterFragment.MAX_STRETCH) {
//                if (houseForRent.getStretch() < hawkMinStartStretch)
//                    return;
//            } else if (hawkMinStartStretch == hawkMaxStartStretch) {
//                if (houseForRent.getStretch() != hawkMinStartStretch)
//                    return;
//            } else {
//                if (houseForRent.getStretch() < hawkMinStartStretch || houseForRent.getStretch() > hawkMaxStartStretch)
//                    return;
//            }
//
//            // PubDate
//            if (hawkMinStartPubDate == FilterFragment.MAX_PUBDATE) {
//            } else if (DateUtils.getDayAgoFromPubDate(-houseForRent.getPubDate()) > hawkMinStartPubDate) {
//                return;
//            }

            houseForRent.setId(dataSnapshot.getKey());
            iNewsHouseForRentView.changeItemHouse(houseForRent);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Log.d("Test", "onChildRemoved --------------" + dataSnapshot.getKey());
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            Log.d("Test", "onChildMoved");
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d("Test", "onCancelled");
        }
    };

    @Override
    public void handleGetNews() {
        NewsHouseForRentFragment.isNeedLoad = false;
        timeStartRequest = new Date().getTime();
        DatabaseReference databaseReferenceHouses = FirebaseUtils.getDatabase().getReference().child("news/houseForRent");
        Query query = databaseReferenceHouses.orderByChild("pubDate");
        query.addChildEventListener(childEventListener);
    }

    @Override
    public void handleDestroy() {
        removeGetHousesEvent();
        NewsHouseForRentFragment.isNeedLoad = true;
    }

    public void removeGetHousesEvent() {
        FirebaseUtils.getDatabase().getReference().child("news/houseForRent").removeEventListener(childEventListener);
    }
}
