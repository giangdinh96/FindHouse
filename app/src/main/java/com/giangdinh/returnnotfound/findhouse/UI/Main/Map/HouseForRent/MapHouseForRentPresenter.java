package com.giangdinh.returnnotfound.findhouse.UI.Main.Map.HouseForRent;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;
import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent.FilterHouseForRentFragment;
import com.giangdinh.returnnotfound.findhouse.Utils.DateUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;

import java.util.HashMap;

/**
 * Created by GiangDinh on 03/04/2018.
 */

public class MapHouseForRentPresenter implements IMapHouseForRentPresenter {
    private Context context;
    private IMapHouseForRentView iMapHouseForRentView;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    // HAWK
    private Province hawkProvince;
    private Town hawkTown;
    private Integer hawkMinStartPrice;
    private Integer hawkMaxStartPrice;
    private Integer hawkMinStartStretch;
    private Integer hawkMaxStartStretch;
    private Integer hawkMinStartPubDate;

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

    public MapHouseForRentPresenter(Context context, IMapHouseForRentView iMapHouseForRentView) {
        this.getHawk();
        this.iMapHouseForRentView = iMapHouseForRentView;
        this.context = context;
    }

    @Override
    public void handleRefresh() {
        getHawk();
        removeGetHousesEvent();
        iMapHouseForRentView.removeAllHousesMaker();
        handleGetNews();
    }

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            final HouseForRent houseForRent = dataSnapshot.getValue(HouseForRent.class);

            // Filter
            // Province
            if (hawkProvince != null && !houseForRent.getAddress().getProvinceId().equals(hawkProvince.getId())) {
                return;
            }
            // Town
            if (hawkTown != null && !houseForRent.getAddress().getTownId().equals(hawkTown.getId())) {
                return;
            }
            // Price
            if (hawkMinStartPrice == FilterHouseForRentFragment.MIN_PRICE && hawkMaxStartPrice == FilterHouseForRentFragment.MAX_PRICE) {

            } else if (hawkMinStartPrice == FilterHouseForRentFragment.MIN_PRICE) {
                if (houseForRent.getPrice() > hawkMaxStartPrice)
                    return;
            } else if (hawkMaxStartPrice == FilterHouseForRentFragment.MAX_PRICE) {
                if (houseForRent.getPrice() < hawkMinStartPrice)
                    return;
            } else if (hawkMinStartPrice == hawkMaxStartPrice) {
                if (houseForRent.getPrice() != hawkMinStartPrice)
                    return;
            } else {
                if (houseForRent.getPrice() < hawkMinStartPrice || houseForRent.getPrice() > hawkMaxStartPrice)
                    return;
            }
            // Stretch
            if (hawkMinStartStretch == FilterHouseForRentFragment.MIN_STRETCH && hawkMaxStartStretch == FilterHouseForRentFragment.MAX_STRETCH) {

            } else if (hawkMinStartStretch == FilterHouseForRentFragment.MIN_STRETCH) {
                if (houseForRent.getStretch() > hawkMaxStartStretch)
                    return;
            } else if (hawkMaxStartStretch == FilterHouseForRentFragment.MAX_STRETCH) {
                if (houseForRent.getStretch() < hawkMinStartStretch)
                    return;
            } else if (hawkMinStartStretch == hawkMaxStartStretch) {
                if (houseForRent.getStretch() != hawkMinStartStretch)
                    return;
            } else {
                if (houseForRent.getStretch() < hawkMinStartStretch || houseForRent.getStretch() > hawkMaxStartStretch)
                    return;
            }

            // PubDate
            if (hawkMinStartPubDate == FilterHouseForRentFragment.MAX_PUBDATE) {
            } else if (DateUtils.getDayAgoFromPubDate(-houseForRent.getPubDate()) > hawkMinStartPubDate) {
                return;
            }

            houseForRent.setId(dataSnapshot.getKey());

            // Get UserPicture and Username
            FirebaseUtils.getDatabase().getReference().child("users").child(houseForRent.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.hasChildren()) {
                        HashMap<String, Object> hashMapUser = (HashMap<String, Object>) dataSnapshot.getValue();
                        if (hashMapUser.get("name") != null) {
                            String userName = hashMapUser.get("name").toString();
                            houseForRent.setUserName(userName);
                        }
                        if (hashMapUser.get("photoUrl") != null) {
                            String userPicture = hashMapUser.get("photoUrl").toString();
                            houseForRent.setUserPicture(userPicture);
                        }

                        if (houseForRent.isDataLoadComplete()) {
                            iMapHouseForRentView.addHouseMarker(houseForRent);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            // Get Address
            FirebaseUtils.getDatabase().getReference().child("provinces").child(houseForRent.getAddress().getProvinceId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null & dataSnapshot.hasChildren()) {
                        HashMap<String, Object> hashMapProvince = (HashMap<String, Object>) dataSnapshot.getValue();
                        String provinceName = hashMapProvince.get("name").toString();
                        HashMap<String, Object> hashMapTowns = (HashMap<String, Object>) hashMapProvince.get("towns");
                        HashMap<String, Object> hashMapTown = (HashMap<String, Object>) hashMapTowns.get(houseForRent.getAddress().getTownId());
                        String townName = (String) hashMapTown.get("name");
                        String detail = houseForRent.getAddress().getDetail();
                        String address = String.format("%s, %s, %s", detail, townName, provinceName);
                        houseForRent.setFullAddress(address);

                        if (houseForRent.isDataLoadComplete()) {
                            iMapHouseForRentView.addHouseMarker(houseForRent);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            iMapHouseForRentView.addHouseMarker(houseForRent);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.e("Test", "onChildChanged-------Map-------" + dataSnapshot.getKey());
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
        MapHouseForRentFragment.isNeedLoad = false;
        handleGetCurrentUserLocation(true, true, 6);
        DatabaseReference databaseReferenceHouses = FirebaseUtils.getDatabase().getReference().child("news/houseForRent");
        Query query = databaseReferenceHouses.orderByChild("time");
        query.addChildEventListener(childEventListener);
    }

    @Override
    public void handleCurrentUserLocationClick() {
        handleGetCurrentUserLocation(true, true, 14);
    }

    @Override
    public void handleGetCurrentUserLocation(final boolean isNeedZoom, final boolean isNeedAnimate, final int zoom) {
        iMapHouseForRentView.removeUserMarker();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Get lastLocation
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    LatLng currentUserPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    iMapHouseForRentView.addUserMarker(currentUserPosition);
                    iMapHouseForRentView.moveCamera(currentUserPosition, isNeedZoom, isNeedAnimate, zoom);
                } else {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                }
            }
        });

        // If getLastLocation fail use requestLocationUpdate
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LatLng currentUserPosition = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                iMapHouseForRentView.addUserMarker(currentUserPosition);
                iMapHouseForRentView.moveCamera(currentUserPosition, isNeedZoom, isNeedAnimate, zoom);
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            }
        };

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void handleDestroy() {
        removeGetHousesEvent();
        MapHouseForRentFragment.isNeedLoad = true;
    }

    public void removeGetHousesEvent() {
        FirebaseUtils.getDatabase().getReference().child("news/houseForRent").removeEventListener(childEventListener);
    }
}
