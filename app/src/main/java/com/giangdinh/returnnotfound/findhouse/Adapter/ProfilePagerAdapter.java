package com.giangdinh.returnnotfound.findhouse.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.UI.Profile.Favorite.FavoriteFragment;
import com.giangdinh.returnnotfound.findhouse.UI.Profile.Intro.IntroFragment;
import com.giangdinh.returnnotfound.findhouse.UI.Profile.Posted.PostedFragment;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
    private Fragment[] fragments;

    public ProfilePagerAdapter(FragmentManager fm, User user) {
        super(fm);
        fragments = new Fragment[]{
                IntroFragment.newInstance(user),
                PostedFragment.newInstance(user),
                FavoriteFragment.newInstance(user)
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


    public Fragment getFragment(int position) {
        return fragments[position];
    }

    public Fragment[] getFragments() {
        return fragments;
    }
}
