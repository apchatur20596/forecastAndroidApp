package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static CustomFragmentPageAdaptor customPageAdaptor;
    AutoSuggestAdapter autoSuggestAdapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setTitle("WeatherApp");

        customPageAdaptor = new CustomFragmentPageAdaptor(getSupportFragmentManager());
        customPageAdaptor.addFragment(new LocationSearchFragment());

        sharedPreferences = getSharedPreferences("favs",
                Context.MODE_PRIVATE);
//        sharedPreferences.edit().clear().commit();
        Map<String,?> keys = sharedPreferences.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",entry.getKey() + ": " +
                    entry.getValue().toString());

            String key = entry.getKey();
            //create a new fragment
            Bundle args = new Bundle();
            args.putString("address", key);
            args.putBoolean("isFavt", true);
            LocationSearchFragment locationSearchFragment = new LocationSearchFragment();
            locationSearchFragment.setArguments(args);

            customPageAdaptor.addFragment(locationSearchFragment);
        }

        ViewPager viewPager = findViewById(R.id.main_container);
        viewPager.setAdapter(customPageAdaptor);

        autoSuggestAdapter = new AutoSuggestAdapter(this,
                android.R.layout.simple_dropdown_item_1line);


        CirclePageIndicator circleIndicator = findViewById(R.id.titles);
        circleIndicator.setViewPager(viewPager);
        circleIndicator.setCurrentItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) menuItem.getActionView();
        int linlayId = getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchPlate.setBackgroundResource(R.color.colorSecondary);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            String address = "";
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("queryText",query);
                //Create new Intent, add location details to the intent
                Intent intent = new Intent(MainActivity.this, WeatherSearchResults.class);
                intent.putExtra("address", query);

                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("queryText",newText);
                final SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                searchAutoComplete.setAdapter(autoSuggestAdapter);

                String url1 = "http://weather-droid.us-east-2.elasticbeanstalk.com/weatherforecast/suggestions?userplace=" + newText;
                makeApiCall(url1);
                searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                        String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                        Log.d("SELECTED!!!!", queryString);
                        searchView.setQuery(queryString, false);
                        address = queryString.trim();
                    }
                });
                return false;
            }


        });
        searchView.setIconified(false);
        return super.onCreateOptionsMenu(menu);
    }

    private void makeApiCall(String url) {
        ApiCall.make(this, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                List<String> list = new ArrayList<>();
                try {
                    JSONArray responseObject = new JSONArray(response);
                    System.out.println(responseObject);
                    for (int i=0; i<responseObject.length(); i++) {
                        list.add(responseObject.getString(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                autoSuggestAdapter.setData(list);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

}
