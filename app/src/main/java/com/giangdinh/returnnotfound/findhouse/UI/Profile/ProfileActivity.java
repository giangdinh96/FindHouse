package com.giangdinh.returnnotfound.findhouse.UI.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.giangdinh.returnnotfound.findhouse.Adapter.ProfilePagerAdapter;
import com.giangdinh.returnnotfound.findhouse.CustomView.NonSwipeableViewPager;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileActivity extends AppCompatActivity implements IProfileView {
    private IProfilePresenter iProfilePresenter;
    private User user;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nsvpProfile)
    NonSwipeableViewPager nsvpProfile;

    @BindView(R.id.ahbnProfile)
    AHBottomNavigation ahbnProfile;

    private ProfilePagerAdapter profilePagerAdapter;
    Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        unbinder = ButterKnife.bind(this);
        iProfilePresenter = new ProfilePresenter(this);

        receiveUser();

        initViews();
        initEvents();
    }

    private void receiveUser() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra(VariableGlobal.EXTRA_USER);
    }

    ////// Init views
    private void initViews() {
        initToolbar();
        initPager();
        initBottomNavigation();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.titleTabIntro));
    }

    private void initPager() {
        profilePagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager(), user);
        nsvpProfile.setAdapter(profilePagerAdapter);
        nsvpProfile.setOffscreenPageLimit(3);
    }

    private void initBottomNavigation() {
        AHBottomNavigationItem tabIntro = new AHBottomNavigationItem(R.string.titleTabNews, R.drawable.ic_intro, R.color.colorTabNews);
        AHBottomNavigationItem tabPost = new AHBottomNavigationItem(R.string.titleTabMap, R.drawable.ic_posted, R.color.colorTabMap);
        AHBottomNavigationItem tabFavorite = new AHBottomNavigationItem(R.string.titleTabFilter, R.drawable.ic_favorite, R.color.colorTabFilter);

        ahbnProfile.addItem(tabIntro);
        ahbnProfile.addItem(tabPost);
        ahbnProfile.addItem(tabFavorite);

        ahbnProfile.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        ahbnProfile.setAccentColor(getResources().getColor(R.color.colorTabAccent));
        ahbnProfile.setInactiveColor(getResources().getColor(R.color.colorTabInactive));

        ahbnProfile.setDefaultBackgroundColor(getResources().getColor(R.color.colorBackgroundBottomNavigation));
    }

    ////// Init events
    private void initEvents() {
        ahbnProfile.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                iProfilePresenter.handleBottomNavigationTabSelected(position, wasSelected);
                return true;
            }
        });
    }

    ////// Implement view
    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void setCurrentTab(int position) {
        nsvpProfile.setCurrentItem(position);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
