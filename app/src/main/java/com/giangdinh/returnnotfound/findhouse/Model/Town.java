package com.giangdinh.returnnotfound.findhouse.Model;

import java.io.Serializable;

/**
 * Created by GiangDinh on 02/02/2018.
 */

public class Town implements Serializable {
    private String id;
    private String name;

    public Town() {

    }

    public Town(String id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Town town = (Town) o;

        return id != null ? id.equals(town.id) : town.id == null;
    }
}
