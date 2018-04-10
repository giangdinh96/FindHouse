package com.giangdinh.returnnotfound.findhouse.UI.Main.News.FindHouse;

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

import com.giangdinh.returnnotfound.findhouse.Adapter.NewsFindHouseAdapter;
import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.PreCachingLayoutManager;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewsFindHouseFragment extends Fragment implements INewsFindHouseView {

    INewsFindHousePresenter iNewsFindHousePresenter;

    @BindView(R.id.ptrvFindHouse)
    PullToRefreshView ptrvFindHouse;
    @BindView(R.id.rvFindHouse)
    RecyclerView rvFindHouse;

    private NewsFindHouseAdapter newsFindHouseAdapter;
    public static boolean IS_NEED_LOAD = true;
    Unbinder unbinder;

    public INewsFindHousePresenter getPresenter() {
        return iNewsFindHousePresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_find_house, container, false);
        unbinder = ButterKnife.bind(this, view);
        iNewsFindHousePresenter = new NewsFindHousePresenter(this);

        initViews();
        initEvents();
        return view;
    }

    public void initViews() {
        initNews();
    }

    public void initNews() {
        newsFindHouseAdapter = new NewsFindHouseAdapter(getContext());
        PreCachingLayoutManager preCachingLayoutManager = new PreCachingLayoutManager(getContext());
        preCachingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        preCachingLayoutManager.setExtraLayoutSpace(2 * getContext().getResources().getDisplayMetrics().heightPixels);
        rvFindHouse.getItemAnimator().setChangeDuration(0);
        rvFindHouse.setLayoutManager(preCachingLayoutManager);
        rvFindHouse.setItemViewCacheSize(1);
        rvFindHouse.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvFindHouse.setAdapter(newsFindHouseAdapter);
        iNewsFindHousePresenter.handleGetNews();
    }

    public void initEvents() {
        ptrvFindHouse.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                iNewsFindHousePresenter.handleRefresh();
            }
        });
    }

    @Override
    public void refreshList() {
        newsFindHouseAdapter.removeAllItemHouse();
        newsFindHouseAdapter = new NewsFindHouseAdapter(getContext());
        rvFindHouse.setAdapter(newsFindHouseAdapter);
    }

    @Override
    public void showRefresh(boolean show) {
        ptrvFindHouse.setRefreshing(show);
    }

    @Override
    public void addItemHouse(FindHouse findHouse) {
        newsFindHouseAdapter.addItemHouse(findHouse);
    }

    @Override
    public void addItemHouse(int position, FindHouse findHouse) {
        newsFindHouseAdapter.addItemHouse(position, findHouse);
    }

    @Override
    public void changeItemHouse(FindHouse findHouse) {
        newsFindHouseAdapter.changeItemHouse(findHouse);
    }

    @Override
    public void removeAllItemHouse() {
        newsFindHouseAdapter.removeAllItemHouse();
    }

    @Override
    public void loadNews(ArrayList<FindHouse> findHouses) {
        newsFindHouseAdapter.addAllItemHouse(findHouses);
    }

    @Override
    public void showNotification(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder noti = new Notification.Builder(getContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Có tin mới về nhà trọ!")
                .setContentTitle(title)
                .setContentText(content);
        notificationManager.notify(1, noti.build());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
        iNewsFindHousePresenter.handleDestroy();
    }
}
