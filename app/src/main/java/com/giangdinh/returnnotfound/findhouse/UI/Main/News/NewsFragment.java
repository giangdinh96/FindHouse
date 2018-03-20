package com.giangdinh.returnnotfound.findhouse.UI.Main.News;

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
import android.widget.Toast;

import com.giangdinh.returnnotfound.findhouse.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GiangDinh on 14/03/2018.
 */

public class NewsFragment extends Fragment {

    @BindView(R.id.tlNews)
    TabLayout tlNews;
    @BindView(R.id.vpNews)
    ViewPager vpNews;

    public NewsFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        initPager();

        return view;
    }

    private void initPager() {
        NewsPagerAdapter newsPagerAdapter = new NewsPagerAdapter(getFragmentManager());
        vpNews.setOffscreenPageLimit(4);
        vpNews.setAdapter(newsPagerAdapter);
        tlNews.setupWithViewPager(vpNews);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_news, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mnNewsFilter:
                Toast.makeText(getContext(), "Click item filter news: " + vpNews.getCurrentItem(), Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
