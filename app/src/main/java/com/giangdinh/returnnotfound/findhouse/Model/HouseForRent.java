package com.giangdinh.returnnotfound.findhouse.Model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by GiangDinh on 25/01/2018.
 */

public class HouseForRent extends News implements Serializable {
    private String firstPictureUrl;
    private HashMap<String, Object> pictures;

    public HouseForRent() {
        this.firstPictureUrl = null;
        this.pictures = new HashMap<>();
    }

    public String getFirstPictureUrl() {
        return firstPictureUrl;
    }

    public void setFirstPictureUrl(String firstPictureUrl) {
        this.firstPictureUrl = firstPictureUrl;
    }

    public HashMap<String, Object> getPictures() {
        return pictures;
    }

    public void setPictures(HashMap<String, Object> pictures) {
        this.pictures = pictures;
    }


    ////// Exclude
    @Exclude
    public void putPicture(String name) {
        pictures.put(name, "News/HouseForRent" + "/" + getId() + "/" + name);
    }

    @Exclude
    public ArrayList<String> getPicturesList() {
        ArrayList<String> picturesPath = new ArrayList<>();
        if (this.pictures.get("pictureFirst") != null) {
            picturesPath.add(this.pictures.get("pictureFirst").toString());
        }
        if (this.pictures.get("pictureSecond") != null) {
            picturesPath.add(this.pictures.get("pictureSecond").toString());
        }
        if (this.pictures.get("pictureThird") != null) {
            picturesPath.add(this.pictures.get("pictureThird").toString());
        }
        if (this.pictures.get("pictureFourth") != null) {
            picturesPath.add(this.pictures.get("pictureFourth").toString());
        }
        if (this.pictures.get("pictureFifth") != null) {
            picturesPath.add(this.pictures.get("pictureFifth").toString());
        }
        if (this.pictures.get("pictureSixth") != null) {
            picturesPath.add(this.pictures.get("pictureSixth").toString());
        }
        return picturesPath;
    }

    @Exclude
    public void copyFrom(HouseForRent houseForRent) {
        setUsersLike(houseForRent.getUsersLike());
    }
}