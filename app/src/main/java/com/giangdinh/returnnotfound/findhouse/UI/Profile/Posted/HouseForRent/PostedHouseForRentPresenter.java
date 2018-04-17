package com.giangdinh.returnnotfound.findhouse.UI.Profile.Favorite.HouseForRent;

import android.os.Handler;
import android.util.Log;

import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.Date;

public class FavoriteHouseForRentPresenter implements IFavoriteHouseForRentPresenter {
    private IFavoriteHouseForRentView iFavoriteHouseForRentView;
    private long timeStartRequest;
    private boolean isRefreshShow;
    private User user;

    public FavoriteHouseForRentPresenter(IFavoriteHouseForRentView iFavoriteHouseForRentView, User user) {
        this.iFavoriteHouseForRentView = iFavoriteHouseForRentView;
        this.timeStartRequest = -1;
        this.user = user;
    }

    @Override
    public void handleRefresh() {
        if (isRefreshShow)
            return;
        isRefreshShow = true;
        removeGetHousesEvent();
        iFavoriteHouseForRentView.refreshList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefreshShow = false;
                iFavoriteHouseForRentView.showRefresh(false);
                handleGetNews();
            }
        }, 1000);
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.e("Test", "onChildAdded-------New-------" + dataSnapshot.getKey());
            HouseForRent houseForRent = dataSnapshot.getValue(HouseForRent.class);

            // Check ok
            // Add to List
            if (!houseForRent.getUsersLike().containsKey(user.getId()))
                return;
            houseForRent.setId(dataSnapshot.getKey());
            if (timeStartRequest >= (-houseForRent.getPubDate())) {
                iFavoriteHouseForRentView.addItemHouse(houseForRent);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.e("Test", "onChildChanged-------New-------" + dataSnapshot.getKey());
            HouseForRent houseForRent = dataSnapshot.getValue(HouseForRent.class);

            houseForRent.setId(dataSnapshot.getKey());
            iFavoriteHouseForRentView.changeItemHouse(houseForRent);
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
        timeStartRequest = new Date().getTime();
        DatabaseReference databaseReferenceHouses = FirebaseUtils.getDatabase().getReference().child("news/houseForRent");
        Query query = databaseReferenceHouses.orderByChild("pubDate");
        query.addChildEventListener(childEventListener);
    }

    @Override
    public void handleDestroy() {
        removeGetHousesEvent();
    }

    public void removeGetHousesEvent() {
        FirebaseUtils.getDatabase().getReference().child("news/houseForRent").removeEventListener(childEventListener);
    }
}
