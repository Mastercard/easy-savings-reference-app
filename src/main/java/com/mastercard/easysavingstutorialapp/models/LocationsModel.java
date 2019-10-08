package com.mastercard.easysavingstutorialapp.models;

import java.util.LinkedList;
import java.util.List;

public class LocationsModel {
    private String pageOffset = "0";
    private String pageLength = "10";
    private String city = "New York";
    private String latitude = "20";
    private String longitude = "20";
    private String radius = "20";

    public String getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(String pageOffset) {
        this.pageOffset = pageOffset;
    }

    public String getPageLength() {
        return pageLength;
    }

    public void setPageLength(String pageLength) {
        this.pageLength = pageLength;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public static List<String> getCities() {
        List<String> cities = new LinkedList<>();
        cities.add("All");
        cities.add("New York");
        cities.add("Portland");
        cities.add("Dayton");
        return cities;
    }
}
