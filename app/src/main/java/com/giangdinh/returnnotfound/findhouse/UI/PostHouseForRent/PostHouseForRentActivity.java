package com.giangdinh.returnnotfound.findhouse.UI.PostHouseForRent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.giangdinh.returnnotfound.findhouse.Adapter.ProvincesAdapter;
import com.giangdinh.returnnotfound.findhouse.Adapter.TownsAdapter;
import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.BitmapUltils;
import com.giangdinh.returnnotfound.findhouse.Utils.ViewUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostHouseForRentActivity extends AppCompatActivity implements IPostHouseForRentView, OnMapReadyCallback {

    private IPostHouseForRentPresenter iPostHouseForRentPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ivHousePictureFirst)
    ImageView ivHousePictureFirst;
    @BindView(R.id.ivHousePictureSecond)
    ImageView ivHousePictureSecond;
    @BindView(R.id.ivHousePictureThird)
    ImageView ivHousePictureThird;
    @BindView(R.id.ivHousePictureFourth)
    ImageView ivHousePictureFourth;
    @BindView(R.id.ivHousePictureFifth)
    ImageView ivHousePictureFifth;
    @BindView(R.id.ivHousePictureSixth)
    ImageView ivHousePictureSixth;

    private View.OnClickListener housePictureOnClickListener;

    @BindView(R.id.sProvinces)
    Spinner sProvinces;
    private List<Province> provinces;
    private ProvincesAdapter provincesAdapter;

    @BindView(R.id.sTowns)
    Spinner sTowns;
    private List<Town> towns;
    private TownsAdapter townsAdapter;

    @BindView(R.id.etAddressDetail)
    EditText etAddressDetail;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.etStretch)
    EditText etStretch;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etDescription)
    EditText etDescription;

    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.fabChooseHouseLocation)
    FloatingActionButton fabChooseHouseLocation;

    private GoogleMap map;
    private Marker currentHouseMarker;

    @BindView(R.id.btnPost)
    Button btnPost;

    private SweetAlertDialog postDialog;

    public static final int RC_PICK_1 = 1;
    public static final int RC_PICK_2 = 2;
    public static final int RC_PICK_3 = 3;
    public static final int RC_PICK_4 = 4;
    public static final int RC_PICK_5 = 5;
    public static final int RC_PICK_6 = 6;

    public static final int RC_LATLNG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_house_first);
        ButterKnife.bind(this);
        iPostHouseForRentPresenter = new PostHouseForRentPresenter(this);

        initViews();
        initMapView(savedInstanceState);
        iPostHouseForRentPresenter.getProvinces();

        initEvents();
    }

    ////// Init Views
    private void initViews() {
        initToolbar();
        initDialog();
        initSpinnerProvince();
        initSpinnerTown();
    }

    public void initToolbar() {
        setSupportActionBar(toolbar);
    }

    public void initDialog() {
        postDialog = new SweetAlertDialog(this);
    }

    public void initSpinnerProvince() {
        provinces = new ArrayList<>();
        provinces.add(new Province("", "Chọn tỉnh", null));
        provincesAdapter = new ProvincesAdapter(this, provinces);
        sProvinces.setAdapter(provincesAdapter);
    }

    public void initSpinnerTown() {
        towns = new ArrayList<>();
        towns.add(new Town("", "Chọn huyện"));
        townsAdapter = new TownsAdapter(this, towns);
        sTowns.setAdapter(townsAdapter);
    }

    ////// Init Events
    private void initEvents() {
        housePictureOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iPostHouseForRentPresenter.handleHousePictureClick(view);
            }
        };
        ivHousePictureFirst.setOnClickListener(housePictureOnClickListener);
        ivHousePictureSecond.setOnClickListener(housePictureOnClickListener);
        ivHousePictureThird.setOnClickListener(housePictureOnClickListener);
        ivHousePictureFourth.setOnClickListener(housePictureOnClickListener);
        ivHousePictureFifth.setOnClickListener(housePictureOnClickListener);
        ivHousePictureSixth.setOnClickListener(housePictureOnClickListener);

        sProvinces.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iPostHouseForRentPresenter.handleProvinceSelected(provinces.get(i), i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sTowns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iPostHouseForRentPresenter.handleTownSelected(towns.get(i), i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick(R.id.btnPost)
    public void postClick(View view) {
        ViewUtils.delayAfterPress(view, 500);
        String addressDetail = etAddressDetail.getText().toString();
        String price = etPrice.getText().toString();
        String stretch = etStretch.getText().toString();
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        String describe = etDescription.getText().toString();
        iPostHouseForRentPresenter.handlePostClick(addressDetail, price, stretch, phone, email, describe);
    }

    @OnClick(R.id.fabChooseHouseLocation)
    public void chooseHouseLocationClick() {
        iPostHouseForRentPresenter.handleChooseHouseLocationClick();
    }

    ////// Implement IView
    @Override
    public void showDialogPostSuccess(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener) {
        postDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        postDialog.setCancelable(false);
        postDialog.setTitleText(title).setContentText(content);
        postDialog.setConfirmClickListener(onSweetClickListener);
        postDialog.show();
    }

    @Override
    public void showDialogPostLoading(String title, String content) {
        postDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        postDialog.setCancelable(false);
        postDialog.setTitleText(title).setContentText(content);
        postDialog.show();
    }

    @Override
    public void showDialogPostFailue(String title, String content, SweetAlertDialog.OnSweetClickListener onSweetClickListener) {
        postDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
        postDialog.setCancelable(false);
        postDialog.setTitleText(title).setContentText(content);
        postDialog.setConfirmClickListener(onSweetClickListener);
        postDialog.show();
    }

    @Override
    public void hideDialogPost() {
        if (postDialog.isShowing())
            postDialog.dismiss();
    }

    @Override
    public void setImagePictureFirst(Uri uri) {
        Glide.with(this).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivHousePictureFirst) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivHousePictureFirst.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void setImagePictureSecond(Uri uri) {
        Glide.with(this).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivHousePictureSecond) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivHousePictureSecond.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void setImagePictureThird(Uri uri) {
        Glide.with(this).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivHousePictureThird) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivHousePictureThird.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void setImagePictureFourth(Uri uri) {
        Glide.with(this).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivHousePictureFourth) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivHousePictureFourth.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void setImagePictureFifth(Uri uri) {
        Glide.with(this).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivHousePictureFifth) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivHousePictureFifth.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void setImagePictureSixth(Uri uri) {
        Glide.with(this).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivHousePictureSixth) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivHousePictureSixth.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void loadSpinnerProvinces(ArrayList<Province> provinces) {
        this.provinces.clear();
        this.provinces.add(new Province("", "Chọn tỉnh", null));
        this.provinces.addAll(provinces);
        provincesAdapter.notifyDataSetChanged();
        sProvinces.setSelection(0);
    }

    @Override
    public void loadSpinnerTowns(ArrayList<Town> towns) {
        this.towns.clear();
        this.towns.add(new Town("", "Chọn huyện"));
        if (towns != null)
            this.towns.addAll(towns);
        townsAdapter.notifyDataSetChanged();
        sTowns.setSelection(0);
    }

    @Override
    public void initMapView(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void addHouseMarker(LatLng latLng) {
        currentHouseMarker = map.addMarker(new MarkerOptions().position(latLng));
        View iconMarker = LayoutInflater.from(this).inflate(R.layout.item_house_marker, null);
        currentHouseMarker.setIcon(BitmapDescriptorFactory.fromBitmap(BitmapUltils.loadBitmapFromView(this, iconMarker)));
        currentHouseMarker.setTitle("Vị trí phòng trọ");
        currentHouseMarker.showInfoWindow();
    }

    @Override
    public void removeHouseMarker() {
        if (currentHouseMarker != null)
            currentHouseMarker.remove();
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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(false);
        iPostHouseForRentPresenter.handleGetCurrentHouseLocation(true, false, 14);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    ////// Override
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        iPostHouseForRentPresenter.handleOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}