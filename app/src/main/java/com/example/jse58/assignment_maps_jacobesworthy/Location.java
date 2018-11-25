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

    public Location(Double locID, Double lat, String loc, Double lon) {
        this.locationID = locID;
        this.latitude = lat;
        this.location = loc;
        this.longitude = lon;
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
