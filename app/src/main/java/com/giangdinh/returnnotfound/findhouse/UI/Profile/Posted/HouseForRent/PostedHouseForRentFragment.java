package com.giangdinh.returnnotfound.findhouse.UI.Profile.Posted.HouseForRent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giangdinh.returnnotfound.findhouse.Adapter.NewsHouseForRentAdapter;
import com.giangdinh.returnnotfound.findhouse.Model.HouseForRent;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.PreCachingLayoutManager;
import com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PostedHouseForRentFragment extends Fragment implements IPostedHouseForRentView {
    private IPostedHouseForRentPresenter iPostedHouseForRentPresenter;
    private User user;
    private NewsHouseForRentAdapter newsHouseForRentAdapter;

    @BindView(R.id.ptrvHouseForRent)
    PullToRefreshView ptrvHouseForRent;
    @BindView(R.id.rvHouseForRent)
    RecyclerView rvHouseForRent;

    private Unbinder unbinder;

    public static PostedHouseForRentFragment newInstance(User user) {
        PostedHouseForRentFragment fragment = new PostedHouseForRentFragment();
        Bundle args = new Bundle();
        args.putSerializable(VariableGlobal.EXTRA_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (User) getArguments().getSerializable(VariableGlobal.EXTRA_USER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posted_house_for_rent, container, false);
        unbinder = ButterKnife.bind(this, view);
        iPostedHouseForRentPresenter = new PostedHouseForRentPresenter(this, user);

        initViews();
        initEvents();

        return view;
    }

    public void initViews() {
        initNews();
    }

    public void initNews() {
        newsHouseForRentAdapter = new NewsHouseForRentAdapter(getContext());
        PreCachingLayoutManager preCachingLayoutManager = new PreCachingLayoutManager(getContext());
        preCachingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        preCachingLayoutManager.setExtraLayoutSpace(2 * getContext().getResources().getDisplayMetrics().heightPixels);
        rvHouseForRent.getItemAnimator().setChangeDuration(0);
        rvHouseForRent.setLayoutManager(preCachingLayoutManager);
        rvHouseForRent.setItemViewCacheSize(1);
        rvHouseForRent.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rvHouseForRent.setAdapter(newsHouseForRentAdapter);
        iPostedHouseForRentPresenter.handleGetNews();
    }

    public void initEvents() {
        ptrvHouseForRent.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                iPostedHouseForRentPresenter.handleRefresh();
            }
        });
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
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
        iPostedHouseForRentPresenter.handleDestroy();
    }
}
