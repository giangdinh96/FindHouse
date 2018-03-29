package com.giangdinh.returnnotfound.findhouse.UI.Main.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.giangdinh.returnnotfound.findhouse.UI.Main.Map.FindHouse.FindHouseFragment;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Map.HouseForRent.HouseForRentFragment;

/**
 * Created by GiangDinh on 14/03/2018.
 */

public class MapPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments;
    private String[] titles;

    public MapPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[]{
                new HouseForRentFragment(),
                new FindHouseFragment()
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
