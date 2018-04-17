package com.giangdinh.returnnotfound.findhouse.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.UI.Profile.Favorite.FindHouse.FavoriteFindHouseFragment;
import com.giangdinh.returnnotfound.findhouse.UI.Profile.Favorite.HouseForRent.FavoriteHouseForRentFragment;

/**
 * Created by GiangDinh on 14/03/2018.
 */

public class FavoritePagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private Fragment[] fragments;

    public FavoritePagerAdapter(FragmentManager fm, User user) {
        super(fm);
        fragments = new Fragment[]{
                FavoriteHouseForRentFragment.newInstance(user),
                FavoriteFindHouseFragment.newInstance(user)
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
