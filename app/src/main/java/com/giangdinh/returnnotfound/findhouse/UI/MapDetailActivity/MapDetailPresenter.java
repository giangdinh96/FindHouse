package com.giangdinh.returnnotfound.findhouse.UI.MapDetailActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.giangdinh.returnnotfound.findhouse.Model.Map.Leg;
import com.giangdinh.returnnotfound.findhouse.Model.Map.MapDirection;
import com.giangdinh.returnnotfound.findhouse.Model.Map.Route;
import com.giangdinh.returnnotfound.findhouse.Model.Map.Step;
import com.giangdinh.returnnotfound.findhouse.Utils.UrlUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.VolleySingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by GiangDinh on 26/02/2018.
 */

public class MapDetailPresenter implements IMapDetailPresenter {
    private Context context;
    private IMapDetailView iMapDetailView;

    private double latitudeHouse;
    private double longitudeHouse;
    private LatLng currentUserPosition;
    private LatLng currentHousePosition;
    private LatLng currentAddressSearch;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    public MapDetailPresenter(IMapDetailView iMapDetailView) {
        this.context = (Context) iMapDetailView;
        this.iMapDetailView = iMapDetailView;
    }

    @Override
    public void handleReceiveCurrentLatLngHouse(double latitudeHouse, double longitudeHouse) {
        this.latitudeHouse = latitudeHouse;
        this.longitudeHouse = longitudeHouse;
    }

    @Override
    public void handleCurrentHouseLocationClick() {
        handleGetCurrentHouseLocation(true, false, true, 14);
    }

    @Override
    public void handleCurrentHouseLocationLongClick() {
        handleGetCurrentHouseLocation(true, true, true, 14);
    }

    @Override
    public void handleGetCurrentHouseLocation(boolean isMove, boolean isNeedZoom, boolean isNeedAnimate, int zoom) {
        iMapDetailView.removeHouseMarker();
        currentHousePosition = new LatLng(latitudeHouse, longitudeHouse);
        iMapDetailView.addHouseMarker(currentHousePosition);
        if (isMove)
            iMapDetailView.moveCamera(currentHousePosition, isNeedZoom, isNeedAnimate, zoom);
    }

    @Override
    public void handleCurrentUserLocationClick() {
        handleGetCurrentUserLocation(true, true, false, true, 14);
    }

    @Override
    public void handleCurrentUserLocationLongClick() {
        handleGetCurrentUserLocation(true, true, true, true, 14);
    }

    @Override
    public void handleGetCurrentUserLocation(final boolean isShowInfor, final boolean isMove, final boolean isNeedZoom, final boolean isNeedAnimate, final int zoom) {
        iMapDetailView.removeUserMarker();
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
                    currentUserPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    iMapDetailView.addUserMarker(isShowInfor, currentUserPosition);
                    if (isMove)
                        iMapDetailView.moveCamera(currentUserPosition, isNeedZoom, isNeedAnimate, zoom);
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
                currentUserPosition = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                iMapDetailView.addUserMarker(isShowInfor, currentUserPosition);
                if (isMove)
                    iMapDetailView.moveCamera(currentUserPosition, isNeedZoom, isNeedAnimate, zoom);
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            }
        };

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public void handleDirectionClick(String mode) {
        iMapDetailView.setDirectionClickable(false);
        handleGetCurrentUserLocation(false, false, false, false, 14);
        if (currentUserPosition != null & currentHousePosition != null) {
            VolleySingleton volleySingleton = VolleySingleton.getInstance(context);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlUtils.makeUrlDirection(currentUserPosition, currentHousePosition, mode), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    iMapDetailView.setDirectionClickable(true);
                    MapDirection mapDirection = new Gson().fromJson(response, MapDirection.class);
                    if (mapDirection == null) {
                        iMapDetailView.showToast("Lỗi không tìm thấy đường đi!");
                        return;
                    }
                    if (mapDirection.getRoutes() == null || mapDirection.getRoutes().length == 0) {
                        iMapDetailView.showToast("Không có đường đi!");
                        return;
                    }

                    ArrayList<LatLng> points = new ArrayList<>();

                    Route route = mapDirection.getRoute(0);
                    Leg leg = route.getLeg(0);
                    for (Step step : leg.getSteps()) {
                        points.addAll(PolyUtil.decode(step.getPolyline().getPoints()));
                    }
                    iMapDetailView.showToast(leg.getDistance().getText());

                    HashMap<String, Float> southwest = route.getBounds().getSouthwest();
                    HashMap<String, Float> northeast = route.getBounds().getNortheast();
                    if (route.getBounds() == null || southwest == null || northeast == null) {
                        iMapDetailView.showToast("Bound null");
                        return;
                    }
                    LatLng south = new LatLng(southwest.get("lat"), southwest.get("lng"));
                    LatLng north = new LatLng(northeast.get("lat"), northeast.get("lng"));
                    LatLngBounds latLngBounds = new LatLngBounds(south, north);

                    iMapDetailView.showDirection(points, latLngBounds);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    iMapDetailView.setDirectionClickable(true);
                    iMapDetailView.showToast("Vui lòng kiểm tra lại kết nối mạng!");
                }
            });
            volleySingleton.addToRequestQueue(stringRequest);
        }
    }

    @Override
    public void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MapDetailActivity.RC_PLACE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(context, data);
                currentAddressSearch = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
                iMapDetailView.moveCamera(currentAddressSearch, true, true, 14);
                iMapDetailView.setSearchAddress(place.getAddress().toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                iMapDetailView.showToast("Error");
            } else if (resultCode == RESULT_CANCELED) {
//                iMapDetailView.showToast("Cancel");
            }
        }
    }
}
