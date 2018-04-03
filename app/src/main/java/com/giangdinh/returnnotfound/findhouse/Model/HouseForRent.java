package com.giangdinh.returnnotfound.findhouse.Model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by GiangDinh on 25/01/2018.
 */

public class HouseForRent implements Serializable {
    private Address address;
    private String description;
    private String email;
    private String firstPictureUrl;
    private double latitude;
    private double longitude;
    private String phone;
    private HashMap<String, Object> pictures;
    private long price;
    private long pubDate;
    private double stretch;
    private String userId;
    private HashMap<String, Object> usersLike;

    public HouseForRent() {
        this.address = new Address();
        this.description = "";
        this.email = "";
        this.firstPictureUrl = null;
        this.latitude = 0;
        this.longitude = 0;
        this.phone = "";
        this.pictures = new HashMap<>();
        this.price = 0;
        this.pubDate = 0;
        this.stretch = 0;
        this.userId = "";
        this.usersLike = new HashMap<>();
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstPictureUrl() {
        return firstPictureUrl;
    }

    public void setFirstPictureUrl(String firstPictureUrl) {
        this.firstPictureUrl = firstPictureUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public HashMap<String, Object> getPictures() {
        return pictures;
    }

    public void setPictures(HashMap<String, Object> pictures) {
        this.pictures = pictures;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getPubDate() {
        return pubDate;
    }

    public void setPubDate(long pubDate) {
        this.pubDate = pubDate;
    }

    public double getStretch() {
        return stretch;
    }

    public void setStretch(double stretch) {
        this.stretch = stretch;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashMap<String, Object> getUsersLike() {
        return usersLike;
    }

    public void setUsersLike(HashMap<String, Object> usersLike) {
        this.usersLike = usersLike;
    }

    ////// Exclude
    private String id;
    private String userPicture;
    private String userName;
    private String fullAddress;

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public String getUserPicture() {
        return userPicture;
    }

    @Exclude
    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }

    @Exclude
    public String getUserName() {
        return userName;
    }

    @Exclude
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Exclude
    public String getFullAddress() {
        return fullAddress;
    }

    @Exclude
    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    @Exclude
    public boolean isDataLoadComplete() {
        return userPicture != null && userName != null & fullAddress != null;
    }


    @Exclude
    public void putPicture(String name) {
        pictures.put(name, "News/HouseForRent" + "/" + id + "/" + name);
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
        this.usersLike = houseForRent.usersLike;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HouseForRent houseForRent = (HouseForRent) o;
        return id.equals(houseForRent.id);
    }
}