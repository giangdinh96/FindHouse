package com.giangdinh.returnnotfound.findhouse.UI.Profile.Favorite.FindHouse;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giangdinh.returnnotfound.findhouse.Adapter.NewsFindHouseAdapter;
import com.giangdinh.returnnotfound.findhouse.Model.FindHouse;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.PreCachingLayoutManager;
import com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoriteFindHouseFragment extends Fragment implements IFavoriteFindHouseView {
    private IFavoriteFindHousePresenter iFavoriteFindHousePresenter;
    private User user;
    private NewsFindHouseAdapter newsFindHouseAdapter;

    @BindView(R.id.ptrvFindHouse)
    PullToRefreshView ptrvFindHouse;
    @BindView(R.id.rvFindHouse)
    RecyclerView rvFindHouse;

    private Unbinder unbinder;

    public static FavoriteFindHouseFragment newInstance(User user) {
        FavoriteFindHouseFragment fragment = new FavoriteFindHouseFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorite_find_house, container, false);
        unbinder = ButterKnife.bind(this, view);
        iFavoriteFindHousePresenter = new FavoriteFindHousePresenter(this, user);

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
        iFavoriteFindHousePresenter.handleGetNews();
    }

    public void initEvents() {
        ptrvFindHouse.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                iFavoriteFindHousePresenter.handleRefresh();
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
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
        iFavoriteFindHousePresenter.handleDestroy();
    }
}
