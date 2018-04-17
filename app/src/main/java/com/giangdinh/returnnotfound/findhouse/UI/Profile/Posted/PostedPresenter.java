package com.giangdinh.returnnotfound.findhouse.UI.Profile.Posted;

public class PostedPresenter implements IPostedPresenter{
    private IPostedView iPostedView;

    public PostedPresenter(IPostedView iPostedView) {
        this.iPostedView = iPostedView;
    }
}
