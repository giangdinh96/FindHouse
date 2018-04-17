package com.giangdinh.returnnotfound.findhouse.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.UI.Profile.Posted.FindHouse.PostedFindHouseFragment;
import com.giangdinh.returnnotfound.findhouse.UI.Profile.Posted.HouseForRent.PostedHouseForRentFragment;

/**
 * Created by GiangDinh on 14/03/2018.
 */

public class PostedPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private Fragment[] fragments;

    public PostedPagerAdapter(FragmentManager fm, User user) {
        super(fm);
        fragments = new Fragment[]{
                PostedHouseForRentFragment.newInstance(user),
                PostedFindHouseFragment.newInstance(user)
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
