package com.giangdinh.returnnotfound.findhouse.UI.Main.News.FindHouse;

import android.os.Handler;
import android.util.Log;

import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;
import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent.FilterHouseForRentFragment;
import com.giangdinh.returnnotfound.findhouse.Utils.DateUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.orhanobut.hawk.Hawk;

import java.util.Date;

/**
 * Created by GiangDinh on 05/04/2018.
 */

public class NewsFindHousePresenter implements INewsFindHousePresenter {
    private INewsFindHouseView iNewsFindHouseView;
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

    public NewsFindHousePresenter(INewsFindHouseView iNewsFindHouseView) {
        this.iNewsFindHouseView = iNewsFindHouseView;
        this.getHawk();
        this.newsCount = 0;
        this.timeStartRequest = -1;
    }

    public void getHawk() {
        hawkProvince = Hawk.get(FilterHouseForRentFragment.HAWK_PROVINCE);
        hawkTown = Hawk.get(FilterHouseForRentFragment.HAWK_TOWN);

        if (Hawk.get(FilterHouseForRentFragment.HAWK_MIN_START_PRICE) != null) {
            hawkMinStartPrice = Hawk.get(FilterHouseForRentFragment.HAWK_MIN_START_PRICE);
        } else {
            hawkMinStartPrice = FilterHouseForRentFragment.MIN_PRICE;
        }

        if (Hawk.get(FilterHouseForRentFragment.HAWK_MAX_START_PRICE) != null) {
            hawkMaxStartPrice = Hawk.get(FilterHouseForRentFragment.HAWK_MAX_START_PRICE);
        } else {
            hawkMaxStartPrice = FilterHouseForRentFragment.MAX_PRICE;
        }

        if (Hawk.get(FilterHouseForRentFragment.HAWK_MIN_START_STRETCH) != null) {
            hawkMinStartStretch = Hawk.get(FilterHouseForRentFragment.HAWK_MIN_START_STRETCH);
        } else {
            hawkMinStartStretch = FilterHouseForRentFragment.MIN_STRETCH;
        }

        if (Hawk.get(FilterHouseForRentFragment.HAWK_MAX_START_STRETCH) != null) {
            hawkMaxStartStretch = Hawk.get(FilterHouseForRentFragment.HAWK_MAX_START_STRETCH);
        } else {
            hawkMaxStartStretch = FilterHouseForRentFragment.MAX_STRETCH;
        }

        if (Hawk.get(FilterHouseForRentFragment.HAWK_MIN_START_PUBDATE) != null) {
            hawkMinStartPubDate = Hawk.get(FilterHouseForRentFragment.HAWK_MIN_START_PUBDATE);
        } else {
            hawkMinStartPubDate = FilterHouseForRentFragment.MAX_PUBDATE;
        }

    }

    @Override
    public void handleRefresh() {
        if (isRefreshShow)
            return;
        isRefreshShow = true;
        iNewsFindHouseView.showRefresh(true);
        getHawk();
        newsCount = 0;
        removeGetHousesEvent();
        iNewsFindHouseView.refreshList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefreshShow = false;
                iNewsFindHouseView.showRefresh(false);
                handleGetNews();
            }
        }, 1000);
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.e("Test", "onChildAdded-------New-------" + dataSnapshot.getKey());
            FindHouse findHouse = dataSnapshot.getValue(FindHouse.class);

            //////Filter
            //Province
            if (hawkProvince != null && !findHouse.getAddress().getProvinceId().equals(hawkProvince.getId())) {
                return;
            }
            // Town
            if (hawkTown != null && !findHouse.getAddress().getTownId().equals(hawkTown.getId())) {
                return;
            }
            // Price
            if (hawkMinStartPrice == FilterHouseForRentFragment.MIN_PRICE && hawkMaxStartPrice == FilterHouseForRentFragment.MAX_PRICE) {

            } else if (hawkMinStartPrice == FilterHouseForRentFragment.MIN_PRICE) {
                if (findHouse.getPrice() > hawkMaxStartPrice)
                    return;
            } else if (hawkMaxStartPrice == FilterHouseForRentFragment.MAX_PRICE) {
                if (findHouse.getPrice() < hawkMinStartPrice)
                    return;
            } else if (hawkMinStartPrice == hawkMaxStartPrice) {
                if (findHouse.getPrice() != hawkMinStartPrice)
                    return;
            } else {
                if (findHouse.getPrice() < hawkMinStartPrice || findHouse.getPrice() > hawkMaxStartPrice)
                    return;
            }
            // Stretch
            if (hawkMinStartStretch == FilterHouseForRentFragment.MIN_STRETCH && hawkMaxStartStretch == FilterHouseForRentFragment.MAX_STRETCH) {

            } else if (hawkMinStartStretch == FilterHouseForRentFragment.MIN_STRETCH) {
                if (findHouse.getStretch() > hawkMaxStartStretch)
                    return;
            } else if (hawkMaxStartStretch == FilterHouseForRentFragment.MAX_STRETCH) {
                if (findHouse.getStretch() < hawkMinStartStretch)
                    return;
            } else if (hawkMinStartStretch == hawkMaxStartStretch) {
                if (findHouse.getStretch() != hawkMinStartStretch)
                    return;
            } else {
                if (findHouse.getStretch() < hawkMinStartStretch || findHouse.getStretch() > hawkMaxStartStretch)
                    return;
            }

            // PubDate
            if (hawkMinStartPubDate == FilterHouseForRentFragment.MAX_PUBDATE) {
            } else if (DateUtils.getDayAgoFromPubDate(-findHouse.getPubDate()) > hawkMinStartPubDate) {
                return;
            }

            // Check ok
            // Add to List
            findHouse.setId(dataSnapshot.getKey());
            if (timeStartRequest >= (-findHouse.getPubDate())) {
                iNewsFindHouseView.addItemHouse(findHouse);
            } else {
                if (FirebaseUtils.isSignIn() && findHouse.getUserId().equals(FirebaseUtils.getCurrentUserId())) {
                    iNewsFindHouseView.addItemHouse(0, findHouse);
                } else {
                    newsCount++;
                    iNewsFindHouseView.showNotification("Bài đăng mới", "Có " + newsCount + " tin mới");
                }
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.e("Test", "onChildChanged-------New-------" + dataSnapshot.getKey());
            FindHouse findHouse = dataSnapshot.getValue(FindHouse.class);

            // Filter
            // Province
            if (hawkProvince != null && !findHouse.getAddress().getProvinceId().equals(hawkProvince.getId())) {
                return;
            }
            // Town
            if (hawkTown != null && !findHouse.getAddress().getTownId().equals(hawkTown.getId())) {
                return;
            }
            // Price
            if (hawkMinStartPrice == FilterHouseForRentFragment.MIN_PRICE && hawkMaxStartPrice == FilterHouseForRentFragment.MAX_PRICE) {

            } else if (hawkMinStartPrice == FilterHouseForRentFragment.MIN_PRICE) {
                if (findHouse.getPrice() > hawkMaxStartPrice)
                    return;
            } else if (hawkMaxStartPrice == FilterHouseForRentFragment.MAX_PRICE) {
                if (findHouse.getPrice() < hawkMinStartPrice)
                    return;
            } else if (hawkMinStartPrice == hawkMaxStartPrice) {
                if (findHouse.getPrice() != hawkMinStartPrice)
                    return;
            } else {
                if (findHouse.getPrice() < hawkMinStartPrice || findHouse.getPrice() > hawkMaxStartPrice)
                    return;
            }
            // Stretch
            if (hawkMinStartStretch == FilterHouseForRentFragment.MIN_STRETCH && hawkMaxStartStretch == FilterHouseForRentFragment.MAX_STRETCH) {

            } else if (hawkMinStartStretch == FilterHouseForRentFragment.MIN_STRETCH) {
                if (findHouse.getStretch() > hawkMaxStartStretch)
                    return;
            } else if (hawkMaxStartStretch == FilterHouseForRentFragment.MAX_STRETCH) {
                if (findHouse.getStretch() < hawkMinStartStretch)
                    return;
            } else if (hawkMinStartStretch == hawkMaxStartStretch) {
                if (findHouse.getStretch() != hawkMinStartStretch)
                    return;
            } else {
                if (findHouse.getStretch() < hawkMinStartStretch || findHouse.getStretch() > hawkMaxStartStretch)
                    return;
            }

            // PubDate
            if (hawkMinStartPubDate == FilterHouseForRentFragment.MAX_PUBDATE) {
            } else if (DateUtils.getDayAgoFromPubDate(-findHouse.getPubDate()) > hawkMinStartPubDate) {
                return;
            }

            findHouse.setId(dataSnapshot.getKey());
            iNewsFindHouseView.changeItemHouse(findHouse);
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
        NewsFindHouseFragment.IS_NEED_LOAD = false;
        timeStartRequest = new Date().getTime();
        DatabaseReference databaseReferenceHouses = FirebaseUtils.getDatabase().getReference().child("news/findHouse");
        Query query = databaseReferenceHouses.orderByChild("pubDate");
        query.addChildEventListener(childEventListener);
    }

    @Override
    public void handleDestroy() {
        removeGetHousesEvent();
        NewsFindHouseFragment.IS_NEED_LOAD = true;
    }

    public void removeGetHousesEvent() {
        FirebaseUtils.getDatabase().getReference().child("news/findHouse").removeEventListener(childEventListener);
    }
}
