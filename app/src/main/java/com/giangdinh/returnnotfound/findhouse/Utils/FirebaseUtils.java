package com.giangdinh.returnnotfound.findhouse.Utils;

import android.content.res.Resources;
import android.util.Log;

import com.giangdinh.returnnotfound.findhouse.Model.Province;
import com.giangdinh.returnnotfound.findhouse.Model.Town;
import com.giangdinh.returnnotfound.findhouse.Model.User;
import com.giangdinh.returnnotfound.findhouse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by GiangDinh on 04/02/2018.
 */

public class FirebaseUtils {
    private static FirebaseDatabase database;

    public static FirebaseDatabase getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }

    public static boolean isSignIn() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser() == null ? false : true;
    }

    public static String getCurrentUserId() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser().getUid();
    }

    public static String getCurrentUserName() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser().getDisplayName();
    }

    public static String getCurrentUserPhotoUrl() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser().getPhotoUrl().toString();
    }

    public static String getCurrentUserEmail() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser().getEmail();
    }

    public static void putCurrentUser() {
        putCurrentUsername();
        putCurrentUserPhotoUrl();
        putCurrentUserEmail();
    }

    public static void putCurrentUser(User user) {
        FirebaseDatabase firebaseDatabase = FirebaseUtils.getDatabase();
        firebaseDatabase.getReference().child("users").child(user.getId()).setValue(user);
    }

    public static void putCurrentUsername() {
        FirebaseDatabase firebaseDatabase = FirebaseUtils.getDatabase();
        firebaseDatabase.getReference().child("users").child(getCurrentUserId()).child("name").setValue(getCurrentUserName());
    }

    public static void putCurrentUserPhotoUrl() {
        FirebaseDatabase firebaseDatabase = FirebaseUtils.getDatabase();
        firebaseDatabase.getReference().child("users").child(getCurrentUserId()).child("photoUrl").setValue(getCurrentUserPhotoUrl());
    }

    public static void putCurrentUserEmail() {
        FirebaseDatabase firebaseDatabase = FirebaseUtils.getDatabase();
        firebaseDatabase.getReference().child("users").child(getCurrentUserId()).child("email").setValue(getCurrentUserEmail());
    }

    public static ArrayList<Province> getProvinces(DataSnapshot dataSnapshot) {
        ArrayList<Province> provinces = new ArrayList<>();

        for (DataSnapshot dataSnapshotProvince : dataSnapshot.getChildren()) {
            HashMap<String, Object> hashMapProvince = (HashMap<String, Object>) dataSnapshotProvince.getValue();

            Province province = new Province();
            province.setId(dataSnapshotProvince.getKey());
            province.setName((String) hashMapProvince.get("name"));

            HashMap<String, Object> hashMapTowns = (HashMap<String, Object>) hashMapProvince.get("towns");
            ArrayList<Town> towns = new ArrayList<>();
            for (Map.Entry<String, Object> entryTown : hashMapTowns.entrySet()) {
                HashMap<String, Object> hashMapTown = (HashMap<String, Object>) entryTown.getValue();
                String townId = entryTown.getKey();

                Town town = new Town();
                town.setId(townId);
                town.setName((String) hashMapTown.get("name"));
                towns.add(town);
            }

            province.setTowns(towns);
            provinces.add(province);
        }
        return provinces;
    }

    public static void setupProvinces(Resources resources) {
        ArrayList<String> provinceList = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.province_arr)));
        ArrayList<String> townList = new ArrayList<>(Arrays.asList(resources.getStringArray(R.array.town_arr)));

        Set<String> set = new HashSet<>(provinceList);
        ArrayList<String> provinces = new ArrayList(set);

        FirebaseDatabase fd = FirebaseUtils.getDatabase();
        DatabaseReference dr = fd.getReference().child("provinces");
        dr.removeValue();
        int count = 0;
        for (int i = 0; i < provinces.size(); i++) {
            DatabaseReference databaseReferenceProvince = dr.push();
            databaseReferenceProvince.child("name").setValue(provinces.get(i));
            for (int j = 0; j < provinceList.size(); j++) {
                if (provinceList.get(j).equals(provinces.get(i))) {
                    databaseReferenceProvince.child("towns").push().child("name").setValue(townList.get(j));
                    provinceList.remove(j);
                    townList.remove(j);
                    j--;
                    count++;
                }
            }
        }
        Log.d("Test", count + "");
    }
}
