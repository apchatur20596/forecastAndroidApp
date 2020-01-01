package com.example.weatherapp;

public class PhotoItem {
    private String photoUrl;
    public PhotoItem(String url) {
        photoUrl = url;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
