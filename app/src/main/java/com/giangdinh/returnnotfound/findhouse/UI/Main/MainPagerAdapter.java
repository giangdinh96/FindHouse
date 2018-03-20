package com.giangdinh.returnnotfound.findhouse.UI.Main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.giangdinh.returnnotfound.findhouse.UI.Main.Map.MapFragment;
import com.giangdinh.returnnotfound.findhouse.UI.Main.News.NewsFragment;
import com.giangdinh.returnnotfound.findhouse.UI.Main.Profile.ProfileFragment;

/**
 * Created by GiangDinh on 14/03/2018.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private Fragment[] fragments;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[]{
                new NewsFragment(),
                new MapFragment(),
                new ProfileFragment()
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
