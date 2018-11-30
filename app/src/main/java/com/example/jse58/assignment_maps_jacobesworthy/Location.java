package com.example.jse58.assignment_maps_jacobesworthy;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Location implements Serializable
{
    Double latitude;
    String location;
    Double longitude;

    public Location()
    {

    }

    public LatLng customLocation(Double lat, Double lon)
    {
        this.latitude = lat;
        this.longitude = lon;
        return new LatLng(lat,lon);
    }


    public Location(String loc, Double lat, Double lon)
    {
        this.latitude = lat;
        this.location = loc;
        this.longitude = lon;
    }

    public LatLng getCoordinates()
    {
        LatLng currentCoordinates = new LatLng(getLatitude(), getLongitude());
        return currentCoordinates;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getLocation() {
        return location;
    }
}
