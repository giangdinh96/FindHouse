package com.giangdinh.returnnotfound.findhouse.UI.HouseForRentDetail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.giangdinh.returnnotfound.findhouse.Adapter.HousePicturePagerAdapter;
import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.BitmapUltils;
import com.giangdinh.returnnotfound.findhouse.Utils.TextUtils;
import com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class HouseForRentDetailActivity extends AppCompatActivity implements IHouseForRentDetailView, OnMapReadyCallback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.vpHousePicture)
    ViewPager vpHousePicture;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.ivUserPicture)
    ImageView ivUserPicture;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvPubDate)
    TextView tvPubDate;
    @BindView(R.id.tvFullAddress)
    TextView tvFullAddress;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvStretch)
    TextView tvStretch;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.tvCall)
    TextView tvCall;
    @BindView(R.id.tvLike)
    TextView tvLike;
    @BindView(R.id.tvShare)
    TextView tvShare;
    @BindView(R.id.vDivider3)
    View vDivider3;
    @BindView(R.id.tvUsersLike)
    TextView tvUsersLike;
    @BindView(R.id.vDivider4)
    View vDivider4;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.fabMapDetail)
    FloatingActionButton fabMapDetail;

    GoogleMap map;

    private HouseForRent houseForRent;
    private IHouseForRentDetailPresenter iHouseForRentDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_for_rent_detail);
        ButterKnife.bind(this);
        iHouseForRentDetailPresenter = new HouseForRentDetailPresenter(this);
        initViews();

        initMapView(savedInstanceState);

        initEvents();
    }

    private void receiveHouse() {
        houseForRent = (HouseForRent) getIntent().getSerializableExtra(VariableGlobal.EXTRA_HOUSE_FOR_RENT);
        iHouseForRentDetailPresenter.handleReceiveHouseForRent(houseForRent);
    }


    private void initViews() {
        receiveHouse();
        initToolbar();
        initHousePicturePager();
    }

    public void initToolbar() {
        setSupportActionBar(toolbar);
    }

    public void initHousePicturePager() {
        HousePicturePagerAdapter housePicturePagerAdapter = new HousePicturePagerAdapter(this, houseForRent);
        vpHousePicture.setAdapter(housePicturePagerAdapter);
        vpHousePicture.setOffscreenPageLimit(2);
        vpHousePicture.setPageTransformer(true, new AccordionTransformer());
        indicator.setViewPager(vpHousePicture);
    }

    private void initEvents() {
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHouseForRentDetailPresenter.handleCallClick(houseForRent);
            }
        });

        tvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHouseForRentDetailPresenter.handleLikeClick(houseForRent);
            }
        });

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHouseForRentDetailPresenter.handleShareClick(houseForRent);
            }
        });

        fabMapDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iHouseForRentDetailPresenter.handleMapDetailClick();
            }
        });
    }

    ////// Implement IView
    @Override
    public void setUserPicture(String userPicture) {
        Glide.with(this).load(userPicture).asBitmap().centerCrop().into(new BitmapImageViewTarget(ivUserPicture) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                ivUserPicture.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    @Override
    public void setUserName(String userName) {
        tvUserName.setText(userName);
    }

    @Override
    public void setTime(String time) {
        tvPubDate.setText(time);
    }

    @Override
    public void setAddress(String address) {
        tvFullAddress.setText(TextUtils.formatHtml("Địa chỉ", address, "#000000"));
    }

    @Override
    public void setPrice(String price) {
        tvPrice.setText(TextUtils.formatHtml("Giá phòng", TextUtils.formatPrice(price), "#000000"));
    }

    @Override
    public void setStretch(String stretch) {
        tvStretch.setText(TextUtils.formatHtml("Diện tích", TextUtils.formatStretch(stretch), "#000000"));
    }

    @Override
    public void setPhone(String phone) {
        tvPhone.setText(TextUtils.formatHtml("Điện thoại", phone, "#000000"));
    }

    @Override
    public void setEmail(String email) {
        tvEmail.setText(TextUtils.formatHtml("Email", email, "#000000"));
    }

    @Override
    public void setDescribe(String describe) {
        tvDescription.setText(TextUtils.formatHtml("Mô tả", describe, "#000000"));
    }

    @Override
    public void setLikeSelected(boolean likeSelected) {
        tvLike.setSelected(likeSelected);
    }

    @Override
    public void showDivider3() {
        vDivider3.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDivider3() {
        vDivider3.setVisibility(View.GONE);
    }

    @Override
    public void setUsersLike(String usersLike) {
        tvUsersLike.setText(usersLike);
    }

    @Override
    public void showUsersLike() {
        tvUsersLike.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideUsersLike() {
        tvUsersLike.setVisibility(View.GONE);
    }

    @Override
    public void showDivider4() {
        vDivider4.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDivider4() {
        vDivider4.setVisibility(View.GONE);
    }

    @Override
    public void initMapView(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    @Override
    public void addMarker(double latitude, double longitude) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewMarker = layoutInflater.inflate(R.layout.item_house_for_rent_marker, null);
        LatLng housePosition = new LatLng(latitude, longitude);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.addMarker(new MarkerOptions().position(housePosition).title("Vị trí phòng trọ").icon(BitmapDescriptorFactory.fromBitmap(BitmapUltils.loadBitmapFromView(this, viewMarker)))).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(housePosition, 13.5f));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setAllGesturesEnabled(false);
        addMarker(houseForRent.getLatitude(), houseForRent.getLongitude());
    }


    ////// Override
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
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        iHouseForRentDetailPresenter.handleDestroy();
    }
}