package com.example.jse58.assignment_maps_jacobesworthy;

public class Location
{
    Double locationID;
    Double latitude;
    String location;
    Double longitude;

    public Location()
    {

    }

    public Location(Double lat, String loc, Double lon) {
        this.latitude = lat;
        this.location = loc;
        this.longitude = lon;
    }

    public Location(Double locationID, Double latitude, String location, Double longitude) {
        this.locationID = locationID;
        this.latitude = latitude;
        this.location = location;
        this.longitude = longitude;
    }

    public Double getLocationID() {
        return locationID;
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
