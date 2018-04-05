package com.giangdinh.returnnotfound.findhouse.Model;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by GiangDinh on 05/04/2018.
 */

public abstract class News implements Serializable {
    private Address address;
    private String description;
    private String email;
    private double latitude;
    private double longitude;
    private String phone;
    private long price;
    private long pubDate;
    private double stretch;
    private String userId;
    private HashMap<String, Object> usersLike;

    public News() {
        this.address = new Address();
        this.description = "";
        this.email = "";
        this.latitude = 0;
        this.longitude = 0;
        this.phone = "";
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return id.equals(news.id);
    }
}
