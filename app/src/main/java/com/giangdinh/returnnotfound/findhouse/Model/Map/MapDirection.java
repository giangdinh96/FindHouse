package com.giangdinh.returnnotfound.findhouse.Model.Map;

/**
 * Created by GiangDinh on 05/03/2018.
 */

public class MapDirection {
    private Route[] routes;

    public MapDirection() {

    }

    public Route[] getRoutes() {
        return routes;
    }

    public Route getRoute(int position) {
        return routes[position];
    }

    public void setRoutes(Route[] routes) {
        this.routes = routes;
    }
}
