package com.giangdinh.returnnotfound.findhouse.UI.Main.Map;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.giangdinh.returnnotfound.findhouse.CustomView.NonSwipeableViewPager;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Map.FindHouse.MapFindHouseFragment;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Map.HouseForRent.MapHouseForRentFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    @BindView(R.id.tlMap)
    TabLayout tlMap;
    @BindView(R.id.nsvpMap)
    NonSwipeableViewPager nsvpMap;
    private MapPagerAdapter mapPagerAdapter;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, view);

        initViews();
        initEvents();
        return view;
    }

    ////// Init views
    private void initViews() {
        initPager();
    }

    private void initPager() {
        mapPagerAdapter = new MapPagerAdapter(getFragmentManager());
        nsvpMap.setAdapter(mapPagerAdapter);
        nsvpMap.setOffscreenPageLimit(4);
        tlMap.setupWithViewPager(nsvpMap);
    }

    ////// Init events
    private void initEvents() {
        tlMap.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        MapHouseForRentFragment mapHouseForRentFragment = (MapHouseForRentFragment) mapPagerAdapter.getFragment(0);
                        mapHouseForRentFragment.getPresenter().handleRefresh();
                        break;
                    case 1:
                        MapFindHouseFragment mapFindHouseFragment = (MapFindHouseFragment) mapPagerAdapter.getFragment(1);
                        mapFindHouseFragment.getPresenter().handleRefresh();
                        break;
                }
            }
        });

        nsvpMap.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                refreshUI();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    ////// Override
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    public void refreshUI() {
        if (nsvpMap.getCurrentItem() == 0) {
            if (MapHouseForRentFragment.IS_NEED_LOAD) {
                MapHouseForRentFragment mapHouseForRentFragment = (MapHouseForRentFragment) mapPagerAdapter.getFragment(0);
                mapHouseForRentFragment.getPresenter().handleRefresh();
            }
        } else {
            if (MapFindHouseFragment.IS_NEED_LOAD) {
                MapFindHouseFragment mapFindHouseFragment = (MapFindHouseFragment) mapPagerAdapter.getFragment(1);
                mapFindHouseFragment.getPresenter().handleRefresh();
            }
        }
    }
}
