package com.giangdinh.returnnotfound.findhouse.UI.ChooseHouseLocation;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal.EXTRA_LAT;
import static com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal.EXTRA_LNG;

/**
 * Created by GiangDinh on 23/02/2018.
 */

public class ChooseHouseLocationPresenter implements IChooseHouseLocationPresenter {
    private Activity activity;
    private IChooseHouseLocationView iChooseHouseLocationView;
    private double latitudeHouse;
    private double longitudeHouse;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    public ChooseHouseLocationPresenter(IChooseHouseLocationView iChooseHouseLocationView) {
        this.activity = (Activity) iChooseHouseLocationView;
        this.iChooseHouseLocationView = iChooseHouseLocationView;
    }

    @Override
    public void handleReceiveCurrentLatLngHouse(double latitudeHouse, double longitudeHouse) {
        this.latitudeHouse = latitudeHouse;
        this.longitudeHouse = longitudeHouse;
    }

    @Override
    public void handleCurrentHouseLocationClick() {
        handleGetCurrentHouseLocation(false, true, 14);
    }

    @Override
    public void handleGetCurrentHouseLocation(final boolean isNeedZoom, final boolean isNeedAnimate, final int zoom) {
        iChooseHouseLocationView.removeHouseMarker();
        if (latitudeHouse == 0 && longitudeHouse == 0) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            // Get lastLocation
            mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        LatLng currentHousePosition = new LatLng(location.getLatitude(), location.getLongitude());
                        iChooseHouseLocationView.addHouseMarker(currentHousePosition);
                        iChooseHouseLocationView.moveCamera(currentHousePosition, isNeedZoom, isNeedAnimate, zoom);
                        latitudeHouse = currentHousePosition.latitude;
                        longitudeHouse = currentHousePosition.longitude;
                    } else {
                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    Toast.makeText(activity, "onLocationResult", Toast.LENGTH_SHORT).show();
                    super.onLocationResult(locationResult);
                    LatLng currentHousePosition = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                    iChooseHouseLocationView.addHouseMarker(currentHousePosition);
                    iChooseHouseLocationView.moveCamera(currentHousePosition, isNeedZoom, isNeedAnimate, zoom);
                    latitudeHouse = currentHousePosition.latitude;
                    longitudeHouse = currentHousePosition.longitude;
                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                }
            };

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        } else {
            LatLng currentHousePosition = new LatLng(latitudeHouse, longitudeHouse);
            iChooseHouseLocationView.addHouseMarker(currentHousePosition);
            iChooseHouseLocationView.moveCamera(currentHousePosition, isNeedZoom, isNeedAnimate, zoom);
        }
    }

    @Override
    public void handleCurrentUserLocationClick() {
        handleGetCurrentUserLocation(false, true, 14);
    }

    @Override
    public void handleGetCurrentUserLocation(final boolean isNeedZoom, final boolean isNeedAnimate, final int zoom) {
        iChooseHouseLocationView.removeUserMarker();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Get lastLocation
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    LatLng currentUserPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    iChooseHouseLocationView.addUserMarker(currentUserPosition);
                    iChooseHouseLocationView.moveCamera(currentUserPosition, isNeedZoom, isNeedAnimate, zoom);
                } else {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                iChooseHouseLocationView.addUserMarker(currentUserPosition);
                iChooseHouseLocationView.moveCamera(currentUserPosition, isNeedZoom, isNeedAnimate, zoom);
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            }
        };

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void handleDoneClick() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_LAT, latitudeHouse);
        intent.putExtra(EXTRA_LNG, longitudeHouse);
        activity.setResult(Activity.RESULT_OK, intent);
        iChooseHouseLocationView.finishActivity();
    }


    @Override
    public void handlePositionSelected(LatLng latLng) {
        latitudeHouse = latLng.latitude;
        longitudeHouse = latLng.longitude;
        handleGetCurrentHouseLocation(false, true, 14);
    }

    @Override
    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChooseHouseLocationActivity.RC_PLACE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(activity, data);
                latitudeHouse = place.getLatLng().latitude;
                longitudeHouse = place.getLatLng().longitude;
                handleGetCurrentHouseLocation(true, true, 14);
                iChooseHouseLocationView.setSearchAddress(place.getAddress().toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                iChooseHouseLocationView.showToast("Error");
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }
}
