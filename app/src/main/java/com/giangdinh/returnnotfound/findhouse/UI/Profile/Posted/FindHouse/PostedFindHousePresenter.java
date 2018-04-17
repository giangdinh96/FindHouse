package com.giangdinh.returnnotfound.findhouse.UI.Profile.Favorite.FindHouse;

import android.os.Handler;
import android.util.Log;

import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.Date;

public class FavoriteFindHousePresenter implements IFavoriteFindHousePresenter {
    private IFavoriteFindHouseView iFavoriteFindHouseView;
    private long timeStartRequest;
    private boolean isRefreshShow;
    private User user;

    public FavoriteFindHousePresenter(IFavoriteFindHouseView iFavoriteFindHouseView, User user) {
        this.iFavoriteFindHouseView = iFavoriteFindHouseView;
        this.timeStartRequest = -1;
        this.user = user;
    }

    @Override
    public void handleRefresh() {
        if (isRefreshShow)
            return;
        isRefreshShow = true;
        removeGetHousesEvent();
        iFavoriteFindHouseView.refreshList();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefreshShow = false;
                iFavoriteFindHouseView.showRefresh(false);
                handleGetNews();
            }
        }, 1000);
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.e("Test", "onChildAdded-------New-------" + dataSnapshot.getKey());
            FindHouse findHouse = dataSnapshot.getValue(FindHouse.class);

            // Check ok
            // Add to List
            if (!findHouse.getUsersLike().containsKey(user.getId()))
                return;
            findHouse.setId(dataSnapshot.getKey());
            if (timeStartRequest >= (-findHouse.getPubDate())) {
                iFavoriteFindHouseView.addItemHouse(findHouse);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.e("Test", "onChildChanged-------New-------" + dataSnapshot.getKey());
            FindHouse findHouse = dataSnapshot.getValue(FindHouse.class);

            findHouse.setId(dataSnapshot.getKey());
            iFavoriteFindHouseView.changeItemHouse(findHouse);
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
        DatabaseReference databaseReferenceHouses = FirebaseUtils.getDatabase().getReference().child("news/findHouse");
        Query query = databaseReferenceHouses.orderByChild("pubDate");
        query.addChildEventListener(childEventListener);
    }

    @Override
    public void handleDestroy() {
        removeGetHousesEvent();
    }

    public void removeGetHousesEvent() {
        FirebaseUtils.getDatabase().getReference().child("news/findHouse").removeEventListener(childEventListener);
    }
}
