package com.giangdinh.returnnotfound.findhouse.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by GiangDinh on 02/02/2018.
 */

public class Province implements Serializable {
    private String id;
    private String name;
    private ArrayList<Town> towns;

    public Province() {

    }

    public Province(String id, String name, ArrayList<Town> towns) {
        this.id = id;
        this.name = name;
        this.towns = towns;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Town> getTowns() {
        return towns;
    }

    public void setTowns(ArrayList<Town> towns) {
        this.towns = towns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Province province = (Province) o;

        return id != null ? id.equals(province.id) : province.id == null;
    }
}
