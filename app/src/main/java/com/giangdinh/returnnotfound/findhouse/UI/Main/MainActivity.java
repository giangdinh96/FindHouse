package com.giangdinh.returnnotfound.findhouse.UI.Main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.giangdinh.returnnotfound.findhouse.Adapter.MainPagerAdapter;
import com.giangdinh.returnnotfound.findhouse.CustomView.NonSwipeableViewPager;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Map.MapFragment;
import com.giangdinh.returnnotfound.findhouse.UI.Main.News.NewsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements IMainView {
    IMainPresenter iMainPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nsvpMain)
    NonSwipeableViewPager nsvpMain;

    @BindView(R.id.ahbnMain)
    AHBottomNavigation ahbnMain;

    private MainPagerAdapter mainPagerAdapter;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        iMainPresenter = new MainPresenter(this);

        initViews();

        initEvents();
    }

    private void initViews() {
        initToolbar();
        initPager();
        initBottomNavigation();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.titleTabNews));
    }

    private void initPager() {
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        nsvpMain.setAdapter(mainPagerAdapter);
        nsvpMain.setOffscreenPageLimit(4);
    }

    ////// Init views
    private void initBottomNavigation() {
        AHBottomNavigationItem tabNews = new AHBottomNavigationItem(R.string.titleTabNews, R.drawable.ic_news, R.color.colorTabNews);
        AHBottomNavigationItem tabMap = new AHBottomNavigationItem(R.string.titleTabMap, R.drawable.ic_map, R.color.colorTabMap);
        AHBottomNavigationItem tabFilter = new AHBottomNavigationItem(R.string.titleTabFilter, R.drawable.ic_filter, R.color.colorTabFilter);
        AHBottomNavigationItem tabProfile = new AHBottomNavigationItem(R.string.titleTabProfile, R.drawable.ic_profile, R.color.colorTabProfile);

        ahbnMain.addItem(tabNews);
        ahbnMain.addItem(tabMap);
        ahbnMain.addItem(tabFilter);
        ahbnMain.addItem(tabProfile);

        ahbnMain.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        ahbnMain.setAccentColor(getResources().getColor(R.color.colorTabAccent));
        ahbnMain.setInactiveColor(getResources().getColor(R.color.colorTabInactive));

        ahbnMain.setDefaultBackgroundColor(getResources().getColor(R.color.colorBackgroundBottomNavigation));
    }

    ////// Init events
    private void initEvents() {
        ahbnMain.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                iMainPresenter.handleBottomNavigationTabSelected(position, wasSelected);
                switch (position) {
                    case 0:
                        NewsFragment newsFragment = (NewsFragment) mainPagerAdapter.getFragment(0);
                        newsFragment.refreshUI();
                        break;
                    case 1:
                        MapFragment mapFragment = (MapFragment) mainPagerAdapter.getFragment(1);
                        mapFragment.refreshUI();
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
                return true;
            }
        });
    }

    ////// IMainView override
    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void setCurrentTab(int position) {
        nsvpMain.setCurrentItem(position);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
