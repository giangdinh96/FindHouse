package com.giangdinh.returnnotfound.findhouse.UI.Profile;

import android.content.Context;

import com.giangdinh.returnnotfound.findhouse.R;

public class ProfilePresenter implements IProfilePresenter {
    private Context context;
    private IProfileView iProfileView;

    public ProfilePresenter(IProfileView iProfileView) {
        this.context = (Context) iProfileView;
        this.iProfileView = iProfileView;
    }

    @Override
    public void handleBottomNavigationTabSelected(int postion, boolean wasSelected) {
        iProfileView.setCurrentTab(postion);
        switch (postion) {
            case 0:
                iProfileView.setTitle(context.getResources().getString(R.string.titleTabIntro));
                break;
            case 1:
                iProfileView.setTitle(context.getResources().getString(R.string.titleTabPosted));
                break;
            case 2:
                iProfileView.setTitle(context.getResources().getString(R.string.titleTabFavorite));
                break;
        }
    }
}
