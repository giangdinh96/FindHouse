package com.giangdinh.returnnotfound.findhouse.UI.Main.News;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.giangdinh.returnnotfound.findhouse.UI.PostHouseForRent.PostHouseForRentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by GiangDinh on 14/03/2018.
 */

public class NewsFragment extends Fragment implements INewsView {
    private INewsPresenter iNewsPresenter;

    @BindView(R.id.tlNews)
    TabLayout tlNews;
    @BindView(R.id.vpNews)
    ViewPager vpNews;

    @BindView(R.id.fabPost)
    FloatingActionButton fabPost;

    Unbinder unbinder;

    public NewsFragment() {
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        iNewsPresenter = new NewsPresenter(this);

        initView();

        return view;
    }

    ////// Init views
    private void initView() {
        initPager();
    }

    private void initPager() {
        NewsPagerAdapter newsPagerAdapter = new NewsPagerAdapter(getFragmentManager());
        vpNews.setOffscreenPageLimit(4);
        vpNews.setAdapter(newsPagerAdapter);
        tlNews.setupWithViewPager(vpNews);
    }

    ////// Init events
    @OnClick(R.id.fabPost)
    public void postClick() {
        iNewsPresenter.handlePostClick();
    }


    ////// Override
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_news, menu);
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

    @Override
    public void navigateToPostHouseNewFist() {
        Intent intentPostHouseForRent = new Intent(getContext(), PostHouseForRentActivity.class);
        startActivity(intentPostHouseForRent);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }
}
