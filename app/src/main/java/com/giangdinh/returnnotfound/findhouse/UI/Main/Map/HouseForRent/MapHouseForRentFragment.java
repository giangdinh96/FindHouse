package com.giangdinh.returnnotfound.findhouse.UI.Main.Map.HouseForRent;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.giangdinh.returnnotfound.findhouse.Adapter.HouseForRentInfoWindowAdapter;
import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.UI.HouseForRentDetail.HouseForRentDetailActivity;
import com.giangdinh.returnnotfound.findhouse.Utils.BitmapUltils;
import com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MapHouseForRentFragment extends Fragment implements IMapHouseForRentView, OnMapReadyCallback {

    IMapHouseForRentPresenter iMapHouseForRentPresenter;

    @BindView(R.id.mapView)
    MapView mapView;

    GoogleMap map;

    private Marker currentUserMarker;
    private Marker currentSearchMarker;

    private ArrayList<Marker> markers;
    private ArrayList<HouseForRent> houseForRents;

    public static final int RC_PLACE_AUTOCOMPLETE = 0;
    public static boolean IS_NEED_LOAD = true;
    public static boolean isMapReady = false;

    Unbinder unbinder;

    public IMapHouseForRentPresenter getPresenter() {
        return iMapHouseForRentPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_house_for_rent, container, false);
        iMapHouseForRentPresenter = new MapHouseForRentPresenter(getContext(), this);
        unbinder = ButterKnife.bind(this, view);

        initViews();
        initMapView(savedInstanceState);

        return view;
    }

    private void initViews() {
        markers = new ArrayList<>();
        houseForRents = new ArrayList<>();
    }

    public void initMapView(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.onStart();
        mapView.onResume();
        mapView.getMapAsync(this);
    }


    @Override
    public void setSearchAddress(String searchAddress) {

    }

    @Override
    public void addHouseMarker(HouseForRent houseForRent) {
        map.setInfoWindowAdapter(new HouseForRentInfoWindowAdapter(getContext()));
        View iconMarker = LayoutInflater.from(getContext()).inflate(R.layout.item_house_for_rent_marker, null);
        Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(houseForRent.getLatitude(), houseForRent.getLongitude())).icon(BitmapDescriptorFactory.fromBitmap(BitmapUltils.loadBitmapFromView(getContext(), iconMarker))));
        marker.setTag(houseForRent);
        markers.add(marker);
        houseForRents.add(houseForRent);
    }

    @Override
    public void removeHouseMarker(HouseForRent houseForRent) {
        int positionHouse = houseForRents.indexOf(houseForRent);
        if (positionHouse == -1) {
            return;
        }
        markers.get(positionHouse).remove();
        markers.remove(positionHouse);
        houseForRents.remove(positionHouse);
    }

    @Override
    public void removeAllHousesMaker() {
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
        houseForRents.clear();
    }

    @Override
    public void addUserMarker(LatLng latLng) {
        View iconMarker = LayoutInflater.from(getContext()).inflate(R.layout.item_user_marker, null);
        currentUserMarker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(BitmapUltils.loadBitmapFromView(getContext(), iconMarker))));
        currentUserMarker.setTitle("Vị trí của bạn");
        currentUserMarker.setTag(0);
        currentUserMarker.showInfoWindow();
    }

    @Override
    public void removeUserMarker() {
        if (currentUserMarker != null) {
            currentUserMarker.remove();
            currentUserMarker = null;
        }
    }

    @Override
    public void addSearchMarker(LatLng latLng, String address) {
        View iconMarker = LayoutInflater.from(getContext()).inflate(R.layout.item_search_marker, null);
        currentSearchMarker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(BitmapUltils.loadBitmapFromView(getContext(), iconMarker))));
        currentSearchMarker.setTitle(address);
        currentSearchMarker.setTag(-1);
        currentSearchMarker.showInfoWindow();
    }

    @Override
    public void removeSearchMarker() {
        if (currentSearchMarker != null) {
            currentSearchMarker.remove();
            currentSearchMarker = null;
        }
    }

    @Override
    public void navigateToSearchPlace() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
            startActivityForResult(intent, RC_PLACE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {

        } catch (GooglePlayServicesNotAvailableException e) {

        }
    }

    @Override
    public void moveCamera(LatLng latLng, boolean isNeedZoom, boolean isNeedAnimate, int zoom) {
        if (isNeedZoom) {
            if (isNeedAnimate) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            } else {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            }
        } else {
            if (isNeedAnimate) {
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            } else {
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (marker.getTag() instanceof HouseForRent) {
                    Intent intentHouseForRentDetail = new Intent(getContext(), HouseForRentDetailActivity.class);
                    intentHouseForRentDetail.putExtra(VariableGlobal.EXTRA_HOUSE_FOR_RENT, (HouseForRent) marker.getTag());
                    getContext().startActivity(intentHouseForRentDetail);
                }
            }
        });

        map.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
                if (marker.getTag() instanceof HouseForRent)
                    moveCamera(marker.getPosition(), true, true, 14);
                else if (marker.getTag().equals(0)) {
                    marker.remove();
                } else if (marker.getTag().equals(-1)) {
                    removeSearchMarker();
                }
            }
        });

        iMapHouseForRentPresenter.handleGetCurrentUserLocation(true, true, 6);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnMapCurrentUserLocation:
                iMapHouseForRentPresenter.handleGetCurrentUserLocation(true, true, 14);
                break;
            case R.id.mnMapSearch:
                if (currentSearchMarker == null)
                    navigateToSearchPlace();
                else {
                    moveCamera(currentSearchMarker.getPosition(), true, true, 14);
                    if (!currentSearchMarker.isInfoWindowShown())
                        currentSearchMarker.showInfoWindow();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_PLACE_AUTOCOMPLETE:
                if (resultCode == Activity.RESULT_OK) {
                    removeSearchMarker();
                    Place place = PlaceAutocomplete.getPlace(getContext(), data);
                    double latitudeHouse = place.getLatLng().latitude;
                    double longitudeHouse = place.getLatLng().longitude;
                    String address = place.getAddress().toString();
                    addSearchMarker(new LatLng(latitudeHouse, longitudeHouse), address);
                    moveCamera(new LatLng(latitudeHouse, longitudeHouse), true, true, 14);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
        iMapHouseForRentPresenter.handleDestroy();
    }
}
