package com.example.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailedWeather extends AppCompatActivity {
    private DetailedWeatherPageAdapter detailedWeatherPageAdapter;
    private String darkSkyData;
    private int temperature;
    private String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_weather);

        Intent intent = getIntent();
        darkSkyData = intent.getStringExtra(LocationSearchFragment.DARK_SKY_RESPONSE);
        address = intent.getStringExtra("address");
        temperature = intent.getIntExtra("temperature", 75);
        detailedWeatherPageAdapter = new DetailedWeatherPageAdapter(getSupportFragmentManager());
        detailedWeatherPageAdapter.addFragment(new ClimateToday());
        detailedWeatherPageAdapter.addFragment(new ClimateWeekly());
        detailedWeatherPageAdapter.addFragment(new ClimatePhotos());
        ViewPager viewPager = findViewById(R.id.detailedWeatherViewPager);
        viewPager.setAdapter(detailedWeatherPageAdapter);

        TabLayout tabLayout = findViewById(R.id.detailedWeatherTabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.calendar_today);
        tabLayout.getTabAt(1).setIcon(R.drawable.trending_up_dim);
        tabLayout.getTabAt(2).setIcon(R.drawable.google_photos_dim);
        tabLayout.setTabTextColors(Color.parseColor("#9F9F9F"), Color.parseColor("#ffffff"));

        String toolBarText = address;

        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(toolBarText);
        addTabLayoutListener(tabLayout);
    }

    private void addTabLayoutListener(final TabLayout tabLayout) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(R.drawable.calendar_today);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.trending_up);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.google_photos);
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(R.drawable.calendar_today_dim);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.trending_up_dim);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.google_photos_dim);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.twitter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case (R.id.twitterMenu):
                twitterShare();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void twitterShare() {
        String region = address;
        String tempF = String.format("%s℉", String.valueOf(temperature));
        String url = "https://twitter.com/intent/tweet?text=Check Out " + region + "'s Weather!"
                + " It is " + tempF + "! &hashtags=CSCI571WeatherSearch";
        Intent i = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
        startActivity(i);
    }

    public String getDarkSkyData(){
        return darkSkyData;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
