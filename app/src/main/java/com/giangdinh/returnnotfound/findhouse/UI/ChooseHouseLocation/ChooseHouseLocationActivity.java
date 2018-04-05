package com.giangdinh.returnnotfound.findhouse.UI.ChooseHouseLocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.BitmapUltils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal.EXTRA_LAT;
import static com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal.EXTRA_LNG;

public class ChooseHouseLocationActivity extends FragmentActivity implements OnMapReadyCallback, IChooseHouseLocationView {
    private IChooseHouseLocationPresenter iChooseHouseLocationPresenter;

    @BindView(R.id.tvSearchAddress)
    TextView tvSearchAddress;
    @BindView(R.id.ivCurrentUserLocation)
    ImageView ivCurrentUserLocation;
    @BindView(R.id.ivCurrentHouseLocation)
    ImageView ivCurrentHouseLocation;
    @BindView(R.id.ivDone)
    ImageView ivDone;

    private GoogleMap map;
    private Marker currentHouseMarker;
    private Marker currentUserMarker;

    public static final int RC_PLACE_AUTOCOMPLETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_house_location);
        ButterKnife.bind(this);
        iChooseHouseLocationPresenter = new ChooseHouseLocationPresenter(this);

        initViews();
        receiveCurrentLatLngHouse();
        initEvents();
    }

    ////// Init Views
    private void initViews() {
        initMap();
    }


    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    ////// Init Events
    private void initEvents() {

    }

    @OnClick(R.id.tvSearchAddress)
    public void searchAddressClick() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(ChooseHouseLocationActivity.this);
            startActivityForResult(intent, RC_PLACE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {

        } catch (GooglePlayServicesNotAvailableException e) {

        }
    }

    @OnClick(R.id.ivCurrentUserLocation)
    public void currentUserLocationClick() {
        iChooseHouseLocationPresenter.handleCurrentUserLocationClick();
    }

    @OnClick(R.id.ivCurrentHouseLocation)
    public void currentHouseLocationClick() {
        iChooseHouseLocationPresenter.handleCurrentHouseLocationClick();
    }

    @OnClick(R.id.ivDone)
    public void doneClick() {
        iChooseHouseLocationPresenter.handleDoneClick();
    }
    //////

    private void receiveCurrentLatLngHouse() {
        double latitudeHouse = getIntent().getDoubleExtra(EXTRA_LAT, 0);
        double longitudeHouse = getIntent().getDoubleExtra(EXTRA_LNG, 0);
        iChooseHouseLocationPresenter.handleReceiveCurrentLatLngHouse(latitudeHouse, longitudeHouse);
    }

    ////// Implement IView
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                iChooseHouseLocationPresenter.handlePositionSelected(latLng);
            }
        });
        map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                iChooseHouseLocationPresenter.handlePositionSelected(marker.getPosition());
            }
        });
        iChooseHouseLocationPresenter.handleGetCurrentHouseLocation(true, false, 14);
    }

    @Override
    public void setSearchAddress(String searchAddress) {
        tvSearchAddress.setText(searchAddress);
    }

    @Override
    public void addHouseMarker(LatLng latLng) {
        View iconMarker = LayoutInflater.from(this).inflate(R.layout.item_house_for_rent_marker, null);
        currentHouseMarker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(BitmapUltils.loadBitmapFromView(this, iconMarker))));
        currentHouseMarker.setDraggable(true);
        currentHouseMarker.setTitle("Vị trí phòng trọ");
        currentHouseMarker.showInfoWindow();
    }

    @Override
    public void removeHouseMarker() {
        if (currentHouseMarker != null)
            currentHouseMarker.remove();
    }

    @Override
    public void addUserMarker(LatLng latLng) {
        View iconMarker = LayoutInflater.from(this).inflate(R.layout.item_user_marker, null);
        currentUserMarker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(BitmapUltils.loadBitmapFromView(this, iconMarker))));
        currentUserMarker.setTitle("Vị trí của bạn");
        currentUserMarker.setVisible(true);
        currentUserMarker.showInfoWindow();
    }

    @Override
    public void removeUserMarker() {
        if (currentUserMarker != null)
            currentUserMarker.remove();
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
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    ////// Override
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        iChooseHouseLocationPresenter.handleOnActivityResult(requestCode, resultCode, data);
    }
}
