package com.giangdinh.returnnotfound.findhouse.UI.Main.News;

import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.Utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by GiangDinh on 22/03/2018.
 */

public class NewsPresenter implements INewsPresenter {
    private INewsView iNewsView;

    public NewsPresenter(INewsView iNewsView) {
        this.iNewsView = iNewsView;
    }

    @Override
    public void handlePostClick() {
        if (!FirebaseUtils.isSignIn()) {
            iNewsView.showMessage("Bạn cần phải đăng nhập trước khi đăng tin!");
        } else {
            FirebaseUtils.getDatabase().getReference().child("users").child(FirebaseUtils.getCurrentUserId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot == null || !dataSnapshot.exists() || !dataSnapshot.hasChildren())
                                return;
                            User user = dataSnapshot.getValue(User.class);
                            if (user.getRole().equals("1")) {
                                iNewsView.navigateToPostHouseNewFist();
                            } else if (user.getRole().equals("2")) {

                            } else {
                                // Not
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }
}
