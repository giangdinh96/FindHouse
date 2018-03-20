package com.giangdinh.returnnotfound.findhouse.UI.Main.Map;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.giangdinh.returnnotfound.findhouse.CustomView.NonSwipeableViewPager;
import com.giangdinh.returnnotfound.findhouse.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    @BindView(R.id.tlMap)
    TabLayout tlMap;
    @BindView(R.id.nsvpMap)
    NonSwipeableViewPager nsvpMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        initPager();

        return view;
    }

    private void initPager() {
        MapPagerAdapter mapPagerAdapter = new MapPagerAdapter(getFragmentManager());
        nsvpMap.setAdapter(mapPagerAdapter);
        nsvpMap.setOffscreenPageLimit(4);
        tlMap.setupWithViewPager(nsvpMap);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_map, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnMapFilter:
                Toast.makeText(getContext(), "Click item filter map: " + nsvpMap.getCurrentItem(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.mnMapSearch:
                Toast.makeText(getContext(), "Click item search map: " + nsvpMap.getCurrentItem(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
