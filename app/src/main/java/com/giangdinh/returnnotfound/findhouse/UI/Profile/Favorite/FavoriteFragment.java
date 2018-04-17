package com.giangdinh.returnnotfound.findhouse.UI.Profile.Favorite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giangdinh.returnnotfound.findhouse.Adapter.FavoritePagerAdapter;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FavoriteFragment extends Fragment implements IFavoriteView {
    private IFavoritePresenter iFavoritePresenter;
    private User user;
    private FavoritePagerAdapter favoritePagerAdapter;

    @BindView(R.id.tlFavorite)
    TabLayout tlFavorite;
    @BindView(R.id.vpFavorite)
    ViewPager vpFavorite;

    private Unbinder unbinder;

    public static FavoriteFragment newInstance(User user) {
        FavoriteFragment fragment = new FavoriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        unbinder = ButterKnife.bind(this, view);
        iFavoritePresenter = new FavoritePresenter(this);

        initViews();
        initEvents();

        return view;
    }

    private void initViews() {
        initPager();
    }

    private void initPager() {
        favoritePagerAdapter = new FavoritePagerAdapter(getFragmentManager(), user);
        vpFavorite.setOffscreenPageLimit(2);
        vpFavorite.setAdapter(favoritePagerAdapter);
        tlFavorite.setupWithViewPager(vpFavorite);
    }

    private void initEvents() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }
}
