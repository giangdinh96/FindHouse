package com.giangdinh.returnnotfound.findhouse.UI.Main;

import android.content.Context;

import com.giangdinh.returnnotfound.findhouse.R;

/**
 * Created by GiangDinh on 14/03/2018.
 */

public class MainPresenter implements IMainPresenter {
    private Context context;
    private IMainView iMainView;

    public MainPresenter(IMainView iMainView) {
        this.context = (Context) iMainView;
        this.iMainView = iMainView;
    }

    @Override
    public void handleBottomNavigationTabSelected(int position, boolean wasSelected) {
        iMainView.setCurrentTab(position);
        switch (position) {
            case 0:
                iMainView.setTitle(context.getResources().getString(R.string.titleTabNews));
                break;
            case 1:
                iMainView.setTitle(context.getResources().getString(R.string.titleTabMap));
                break;
            case 2:
                iMainView.setTitle(context.getResources().getString(R.string.titleTabFilter));
                break;
            case 3:
                iMainView.setTitle(context.getResources().getString(R.string.titleTabProfile));
                break;
        }
    }
}
