package com.giangdinh.returnnotfound.findhouse.Model;

import com.google.firebase.database.Exclude;

/**
 * Created by GiangDinh on 05/04/2018.
 */

public class FindHouse extends News {
    @Exclude
    public void copyFrom(FindHouse findHouse) {
        setUsersLike(findHouse.getUsersLike());
    }
}
