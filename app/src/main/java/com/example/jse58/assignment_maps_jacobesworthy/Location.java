package com.example.jse58.assignment_maps_jacobesworthy;

public class Location
{
    String locationID;
    String latitude;
    String location;
    String longitude;

    public Location()
    {

    }

    public Location(String locID, String lat, String loc, String lon) {
        this.locationID = locID;
        this.latitude = lat;
        this.location = loc;
        this.longitude = lon;
    }

    public String getLocationID() {
        return locationID;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLocation() {
        return location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
