package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WeatherSearchResults extends AppCompatActivity {
    private String address;
    private android.support.v4.app.FragmentManager manager = null;
    private LocationSearchFragment locationSearchFragment = null;
    private android.support.v4.app.FragmentTransaction ft;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_search_results);
        //From intent, get location name
        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setTitle(address);

        // create new fragment
        if(manager == null) {
            manager = getSupportFragmentManager();
        }

        if(manager.findFragmentById(R.id.flcontainer) == null) {
            locationSearchFragment = new LocationSearchFragment();
            ft = manager.beginTransaction();
            ft.add(R.id.flcontainer, locationSearchFragment).commit();

        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
