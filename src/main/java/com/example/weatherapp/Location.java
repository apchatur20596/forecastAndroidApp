package com.example.weatherapp;

public class Location {
    private String city="", stateCode="", CountryCode="";
    private double latitude=0, longitude=0;

    public Location(String city, String stateCode, String CountryCode, double latitude, double longitude) {
        this.city = city;
        this.stateCode = stateCode;
    }
}
