package com.giangdinh.returnnotfound.findhouse.UI.MapDetailActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.BitmapUltils;
import com.giangdinh.returnnotfound.findhouse.Utils.MapUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal.EXTRA_LAT;
import static com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal.EXTRA_LNG;

public class MapDetailActivity extends FragmentActivity implements OnMapReadyCallback, IMapDetailView {

    private IMapDetailPresenter iMapDetailPresenter;

    @BindView(R.id.tvSearchAddress)
    TextView tvSearchAddress;
    @BindView(R.id.ivCurrentUserLocation)
    ImageView ivCurrentUserLocation;
    @BindView(R.id.ivCurrentHouseLocation)
    ImageView ivCurrentHouseLocation;
    @BindView(R.id.ivChangeType)
    ImageView ivChangeType;
    @BindView(R.id.ivDirection)
    ImageView ivDirection;

    private GoogleMap map;
    private Marker currentHouseMarker;
    private Marker currentUserMarker;
    private Polyline polyline;

    public static final int RC_PLACE_AUTOCOMPLETE = 1;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_detail);
        unbinder = ButterKnife.bind(this);
        iMapDetailPresenter = new MapDetailPresenter(this);
        initViews();

        receiveCurrentLatLngHouse();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initEvents();
    }

    private void receiveCurrentLatLngHouse() {
        double latitudeHouse = getIntent().getDoubleExtra(EXTRA_LAT, 0);
        double longitudeHouse = getIntent().getDoubleExtra(EXTRA_LNG, 0);
        iMapDetailPresenter.handleReceiveCurrentLatLngHouse(latitudeHouse, longitudeHouse);
    }

    private void initViews() {
    }

    private void initEvents() {
        tvSearchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(MapDetailActivity.this);
                    startActivityForResult(intent, RC_PLACE_AUTOCOMPLETE);
                } catch (GooglePlayServicesRepairableException e) {

                } catch (GooglePlayServicesNotAvailableException e) {

                }
            }
        });

        ivCurrentHouseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iMapDetailPresenter.handleCurrentHouseLocationClick();
            }
        });
        ivCurrentHouseLocation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                iMapDetailPresenter.handleCurrentHouseLocationLongClick();
                return false;
            }
        });

        ivCurrentUserLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iMapDetailPresenter.handleCurrentUserLocationClick();
            }
        });
        ivCurrentUserLocation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                iMapDetailPresenter.handleCurrentUserLocationLongClick();
                return false;
            }
        });

        ivChangeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MapDetailActivity.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_change_map_type, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mnNormal:
                                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                break;

                            case R.id.mnSatellite:
                                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;

                            case R.id.mnTerrain:
                                map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;

                            case R.id.mnHybrid:
                                map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                break;
                        }
                        return false;
                    }
                });
            }
        });

        ivDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iMapDetailPresenter.handleDirectionClick("driving");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        iMapDetailPresenter.handleGetCurrentUserLocation(false, false, false, false, 0);
        iMapDetailPresenter.handleGetCurrentHouseLocation(true, true, false, 14);
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
    public void addUserMarker(boolean isShowInfor, LatLng latLng) {
        View iconMarker = LayoutInflater.from(this).inflate(R.layout.item_user_marker, null);
        currentUserMarker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(BitmapUltils.loadBitmapFromView(this, iconMarker))));
        currentUserMarker.setTitle("Vị trí của bạn");
        currentUserMarker.setVisible(true);
        if (isShowInfor)
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
        Toast.makeText(this, "" + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setDirectionClickable(boolean clickable) {
        ivDirection.setClickable(clickable);
    }

    @Override
    public void showDirection(ArrayList<LatLng> points, LatLngBounds latLngBounds) {
        if (polyline != null) {
            polyline.remove();
        }
        polyline = map.addPolyline(new PolylineOptions().addAll(points));
        MapUtils.stylePolyline(polyline);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 80));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        iMapDetailPresenter.handleOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
