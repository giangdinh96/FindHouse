package com.giangdinh.returnnotfound.findhouse.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.FindHouse.FilterFindHouseFragment;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Filter.HouseForRent.FilterHouseForRentFragment;

/**
 * Created by GiangDinh on 14/03/2018.
 */

public class FilterPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments;
    private String[] titles;

    public FilterPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[]{
                new FilterHouseForRentFragment(),
                new FilterFindHouseFragment()
        };
        titles = new String[]{
                "Phòng cho thuê",
                "Tìm phòng"
        };
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public Fragment getFragment(int position) {
        return fragments[position];
    }

    public Fragment[] getFragments() {
        return fragments;
    }
}
