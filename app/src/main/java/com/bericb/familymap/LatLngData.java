package com.bericb.familymap;

import com.google.android.gms.maps.model.LatLng;

public class LatLngData {

    public LatLng getCurrLoc() {
        return currLoc;
    }

    public void setCurrLoc(LatLng currLoc) {
        this.currLoc = currLoc;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private LatLng currLoc;
    private LatLng latLng;
    private int level;
    public LatLngData(LatLng currLoc, LatLng latLng, int level) {
        this.currLoc = currLoc;
        this.latLng = latLng;
        this.level = level;
    }
}
