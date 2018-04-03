package com.giangdinh.returnnotfound.findhouse.UI.Main.Filter;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giangdinh.returnnotfound.findhouse.Adapter.FilterPagerAdapter;
import com.giangdinh.returnnotfound.findhouse.CustomView.NonSwipeableViewPager;
import com.giangdinh.returnnotfound.findhouse.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by GiangDinh on 02/04/2018.
 */

public class FilterFragment extends Fragment {
    @BindView(R.id.tlFilter)
    TabLayout tlFilter;
    @BindView(R.id.nsvpFilter)
    NonSwipeableViewPager nsvpFilter;

    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        unbinder = ButterKnife.bind(this, view);

        initPager();

        return view;
    }

    private void initPager() {
        FilterPagerAdapter filterPagerAdapter = new FilterPagerAdapter(getFragmentManager());
        nsvpFilter.setAdapter(filterPagerAdapter);
        nsvpFilter.setOffscreenPageLimit(4);
        tlFilter.setupWithViewPager(nsvpFilter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }
}
