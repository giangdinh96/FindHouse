package com.giangdinh.returnnotfound.findhouse.UI.Profile.Posted;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.giangdinh.returnnotfound.findhouse.Adapter.PostedPagerAdapter;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.R;
import com.giangdinh.returnnotfound.findhouse.Utils.VariableGlobal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PostedFragment extends Fragment implements IPostedView {
    private IPostedPresenter iPostedPresenter;
    private User user;
    private PostedPagerAdapter postedPagerAdapter;

    @BindView(R.id.tlPosted)
    TabLayout tlPosted;
    @BindView(R.id.vpPosted)
    ViewPager vpPosted;

    private Unbinder unbinder;

    public static PostedFragment newInstance(User user) {
        PostedFragment fragment = new PostedFragment();
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
        View view = inflater.inflate(R.layout.fragment_posted, container, false);
        unbinder = ButterKnife.bind(this, view);
        iPostedPresenter = new PostedPresenter(this);

        initViews();
        initEvents();

        return view;
    }

    private void initViews() {
        initPager();
    }

    private void initPager() {
        postedPagerAdapter = new PostedPagerAdapter(getFragmentManager(), user);
        vpPosted.setOffscreenPageLimit(2);
        vpPosted.setAdapter(postedPagerAdapter);
        tlPosted.setupWithViewPager(vpPosted);
    }

    private void initEvents() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }
}
