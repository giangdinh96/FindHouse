package com.giangdinh.returnnotfound.findhouse.UI.Main.News;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.giangdinh.returnnotfound.findhouse.UI.Main.News.FindHouse.FindHouseFragment;
import com.giangdinh.returnnotfound.findhouse.UI.Main.News.HouseForRent.HouseForRentFragment;

/**
 * Created by GiangDinh on 14/03/2018.
 */

public class NewsPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private Fragment[] fragments;

    public NewsPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[]{
                new HouseForRentFragment(),
                new FindHouseFragment()
        };
        titles = new String[]{
                "Phòng cho thuê",
                "Tìm phòng",
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
