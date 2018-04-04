package com.giangdinh.returnnotfound.findhouse.UI.Main.News.HouseForRent;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giangdinh.returnnotfound.findhouse.Adapter.NewsHouseForRentAdapter;
import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.PreCachingLayoutManager;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewsHouseForRentFragment extends Fragment implements INewsHouseForRentView {
    INewsHouseForRentPresenter iNewsHouseForRentPresenter;

    @BindView(R.id.ptrvHouseForRent)
    PullToRefreshView ptrvHouseForRent;
    @BindView(R.id.rvHouseForRent)
    RecyclerView rvHouseForRent;

    private NewsHouseForRentAdapter newsHouseForRentAdapter;
    public static boolean isNeedLoad = true;
    Unbinder unbinder;

    public INewsHouseForRentPresenter getPresenter() {
        return iNewsHouseForRentPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_house_for_rent, container, false);
        unbinder = ButterKnife.bind(this, view);
        iNewsHouseForRentPresenter = new NewsHouseForRentPresenter(this);
        initViews(view);

        initNews();
        initEvents();
        return view;
    }

    public void initViews(View view) {
    }

    public void initEvents() {
        ptrvHouseForRent.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                iNewsHouseForRentPresenter.handleRefresh();
            }
        });
    }

    @Override
    public void initNews() {
        newsHouseForRentAdapter = new NewsHouseForRentAdapter(getContext());
        PreCachingLayoutManager preCachingLayoutManager = new PreCachingLayoutManager(getContext());
        preCachingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        preCachingLayoutManager.setExtraLayoutSpace(2 * getContext().getResources().getDisplayMetrics().heightPixels);
        rvHouseForRent.getItemAnimator().setChangeDuration(0);
        rvHouseForRent.setLayoutManager(preCachingLayoutManager);
        rvHouseForRent.setItemViewCacheSize(1);
        rvHouseForRent.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvHouseForRent.setAdapter(this.newsHouseForRentAdapter);
        iNewsHouseForRentPresenter.handleGetNews();
    }

    @Override
    public void refreshList() {
        newsHouseForRentAdapter.removeAllItemHouse();
        newsHouseForRentAdapter = new NewsHouseForRentAdapter(getContext());
        rvHouseForRent.setAdapter(newsHouseForRentAdapter);
    }

    @Override
    public void showRefresh(boolean show) {
        ptrvHouseForRent.setRefreshing(show);
    }

    @Override
    public void addItemHouse(HouseForRent houseForRent) {
        newsHouseForRentAdapter.addItemHouse(houseForRent);
    }

    @Override
    public void addItemHouse(int position, HouseForRent houseForRent) {
        newsHouseForRentAdapter.addItemHouse(position, houseForRent);
    }

    @Override
    public void changeItemHouse(HouseForRent houseForRent) {
        newsHouseForRentAdapter.changeItemHouse(houseForRent);
    }

    @Override
    public void removeAllItemHouse() {
        newsHouseForRentAdapter.removeAllItemHouse();
    }

    @Override
    public void loadNews(ArrayList<HouseForRent> houseForRents) {
        newsHouseForRentAdapter.addAllItemHouse(houseForRents);
    }

    @Override
    public void showNotification(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder noti = new Notification.Builder(getContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Có tin mới về nhà trọ!")
                .setContentTitle(title)
                .setContentText(content);
        notificationManager.notify(0, noti.build());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
        iNewsHouseForRentPresenter.handleDestroy();
    }
}
