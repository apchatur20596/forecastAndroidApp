package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class LocationSearchFragment extends Fragment implements View.OnClickListener {
    private StringRequest mStringRequest;
    private View view;
    private static final String TAG = "SampleActivity";
    private String darkSkyResponse;
    private double latitude;
    private double longitude;
    private String city;
    private String state;
    private String country;
    private String timezone;
    private String summary;
    private String icon;
    private int temperature;
    private double humidity;
    private double visibility;
    private double pressure;
    private double windSpeed;
    private ProgressBar progressBar;
    private TextView progressBarText;
    private RelativeLayout homePageResults;
    private boolean isfavt = false;
    private String address;

    private static final String serverURL="http://umbrella-weather.us-east-2.elasticbeanstalk.com";
    private static DecimalFormat df = new DecimalFormat("0.00");
    private LinearLayoutManager layoutManager;

    ArrayList<DailyWeather> dailyWeatherArrayList;
    public static final String EXTRA_MESSAGE = "com.example.weatherapp.MESSAGE";
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.location_search, container, false);
        sharedPreferences = getActivity().getSharedPreferences("favs",
                Context.MODE_PRIVATE);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBarText = (TextView) view.findViewById(R.id.progressBarMessage);
        homePageResults = (RelativeLayout) view.findViewById(R.id.homePageResults);
        populateWeatherCards();
        this.layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        CardView weatherCard = view.findViewById(R.id.WeatherCard_1);
        addOnClickeListenerToWeatherCard(weatherCard);
        setIsFavt();
        addFavButton();
        addOnClickListenerToFavouritesButton();
        return view;
    }

    private void setIsFavt() {
        if (getArguments() != null) {
            if(getArguments().getBoolean("isFavt")) {
                boolean val = getArguments().getBoolean("isFavt");
                isfavt = val;
            }
        }
    }

    private void addFavButton() {
        if(getActivity().getLocalClassName().equals("MainActivity")) {
            if(getArguments() == null) {
                view.findViewById(R.id.floatingActionButton).setVisibility(View.GONE);
                isfavt = false;
            }
        }
        if(isfavt) {
            ((FloatingActionButton)view.findViewById(R.id.floatingActionButton)).setImageResource(R.drawable.map_marker_minus);
        }

    }

    private void addOnClickListenerToFavouritesButton() {

        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isfavt) {
                    isfavt = false;
                    //go to MainActivity
                    //delete from SharesPreferences
                    deleteFromFavourites();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    Toast.makeText(getContext(),
                            "Deleted "+ address+ " From favourites", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } else {
                    addToFavourites(v);
                    Toast.makeText(getContext(),
                            "Added "+ address + " to favourites", Toast.LENGTH_LONG).show();
                    ((FloatingActionButton) view.findViewById(R.id.floatingActionButton)).setImageResource(R.drawable.map_marker_minus);
                }
            }
        });
    }

    private void populateWeatherCards() {
        if(getActivity().getLocalClassName().equals("MainActivity")) {
            if(getArguments() == null) {
                populateCurrentLocation();
            } else {
                address = getArguments().getString("address");
                populateSearchedLocation(address);
            }
        } else {
            address = ((WeatherSearchResults) getActivity()).getAddress();
            populateSearchedLocation(address);
        }
    }

    public static String DARK_SKY_RESPONSE = "darkSkyData";
    private void addOnClickeListenerToWeatherCard(CardView weatherCard) {
        weatherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test Cheb", "onClickListener ist gestartet");

                //Create new activity
                Intent intent = new Intent(getActivity(), DetailedWeather.class);
                intent.putExtra(DARK_SKY_RESPONSE, darkSkyResponse);
                intent.putExtra("address", address);
                intent.putExtra("temperature", temperature);
                startActivity(intent);
            }
        });
    }



    private void populateCurrentLocation() {
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String ip_api_url = "http://ip-api.com/json";
        mStringRequest = new StringRequest(Request.Method.GET,
                ip_api_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            latitude = jsonObject.getDouble("lat");
                            longitude = jsonObject.getDouble("lon");
                            timezone = jsonObject.getString("timezone");
                            city = jsonObject.getString("city");
                            state = jsonObject.getString("region");
                            country = jsonObject.getString("countryCode");
                            address = city + ", "+state + ", " + country;
                            /**
                             * 1. Create URL with params
                             * 2. Create a new Volley request
                             */

//                            String darkSkyQueryUrl = "http://10.0.2.2:3000/mock/darksky";

                            String darkSkyQueryUrl = "http://weather-droid.us-east-2.elasticbeanstalk.com/darksky/current?location=%7B%22latitude%22:"+latitude+",%22longitude%22:"+longitude+"%7D";
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                    Request.Method.GET, darkSkyQueryUrl, null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject darkSkyJsonResponse) {
                                            Log.i(TAG, "DarkSkyResponse: " + darkSkyJsonResponse.toString());
                                            try {
                                                darkSkyResponse = darkSkyJsonResponse.toString();

                                                //Process data for current activity
                                                timezone = (String) darkSkyJsonResponse.get("timezone");
                                                JSONObject currently = (JSONObject) darkSkyJsonResponse.get("currently");
                                                summary = (String )currently.get("summary");
                                                icon = (String) currently.get("icon");
                                                temperature = currently.getInt("temperature");

                                                /*Set the correct weather icon*/
                                                ImageView currentWeatherIcon = (ImageView) view.findViewById(R.id.currentWeatherIcon);
                                                setWeatherIcon(icon, currentWeatherIcon);
                                                setTemperature(temperature);
                                                setWeatherSummary(summary);
                                                setTimeZone(timezone);

                                                /*  Parse values for second card */
                                                humidity = 0; pressure = 0;
                                                windSpeed = 0;visibility = 0;

                                                if(currently.has("humidity") && !currently.isNull("humidity")) {
                                                    humidity = Math.round(10000.0 * currently.getDouble("humidity")) / 100.0;
                                                }
                                                if(currently.has("pressure") && !currently.isNull("pressure")) {
                                                    pressure = Math.round(100.0 * currently.getDouble("pressure")) / 100.0;
                                                }
                                                if(currently.has("visibility") && !currently.isNull("visibility")) {
                                                    visibility = Math.round(100.0 * currently.getDouble("visibility")) / 100.0;
                                                }
                                                if(currently.has("windSpeed") && !currently.isNull("windSpeed")) {
                                                    windSpeed = Math.round(100.0 * currently.getDouble("windSpeed")) / 100.0;
                                                }
                                                setWeatherCard_2(humidity, pressure, visibility, windSpeed);

                                                /**
                                                 * Parse Daily Table data
                                                 */
                                                JSONObject daily = (JSONObject) darkSkyJsonResponse.get("daily");
                                                JSONArray dailyData = (JSONArray) daily.getJSONArray("data");

                                                setDailyWeatherData(dailyData);
                                                RecyclerView dailyWeatherRecyclerView = (RecyclerView) view.findViewById(R.id.DailyWeatherDataList);
                                                DailyWeatherTableAdapter dailyWeatherTableAdapter = new DailyWeatherTableAdapter(dailyWeatherArrayList);
                                                dailyWeatherRecyclerView.setAdapter(dailyWeatherTableAdapter);

                                                dailyWeatherRecyclerView.setLayoutManager(layoutManager);

                                                progressBar.setVisibility(View.GONE);
                                                progressBar.setVisibility(View.GONE);
                                                homePageResults.setVisibility(View.VISIBLE);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //do something here
                                    Log.i(TAG,"Cheb Error :" + error.toString());
                                }
                            });
                            requestQueue.add(jsonObjectRequest);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),
                        "No Response!", Toast.LENGTH_LONG).show();
                Log.i(TAG,"Cheb Error :" + error.toString());

            }
        });
        requestQueue.add(mStringRequest);

    }

    private void populateSearchedLocation(String address) {
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        address = address.trim().replace(" ","+");
//        String url = "http://10.0.2.2:3000/geocoding/coords?address="+address;
        String url = "http://weather-droid.us-east-2.elasticbeanstalk.com/geocoding/coords?address="+address;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject darkSkyJsonResponse) {
                        try {
                            darkSkyResponse = darkSkyJsonResponse.toString();
                            timezone = (String) darkSkyJsonResponse.get("timezone");
                            JSONObject currently = (JSONObject) darkSkyJsonResponse.get("currently");
                            summary = (String )currently.get("summary");
                            icon = (String) currently.get("icon");
                            temperature = currently.getInt("temperature");

                            /*Set the correct weather icon*/
                            ImageView currentWeatherIcon = (ImageView) view.findViewById(R.id.currentWeatherIcon);
                            setWeatherIcon(icon, currentWeatherIcon);
                            setTemperature(temperature);
                            setWeatherSummary(summary);
                            setTimeZone(timezone);

                            /*  Parse values for second card */
                            humidity = Math.round(10000.0 * currently.getDouble("humidity"))/100.0;
                            pressure = Math.round(100.0 * currently.getDouble("pressure"))/100.0;
                            visibility = Math.round(100.0 * currently.getDouble("visibility"))/100.0;
                            windSpeed = Math.round(100.0 * currently.getDouble("windSpeed"))/100.0;
                            setWeatherCard_2(humidity, pressure, visibility, windSpeed);

                            /**
                             * Parse Daily Table data
                             */
                            JSONObject daily = (JSONObject) darkSkyJsonResponse.get("daily");
                            JSONArray dailyData = (JSONArray) daily.getJSONArray("data");

                            setDailyWeatherData(dailyData);
                            RecyclerView dailyWeatherRecyclerView = (RecyclerView) view.findViewById(R.id.DailyWeatherDataList);
                            DailyWeatherTableAdapter dailyWeatherTableAdapter = new DailyWeatherTableAdapter(dailyWeatherArrayList);
                            dailyWeatherRecyclerView.setAdapter(dailyWeatherTableAdapter);

                            dailyWeatherRecyclerView.setLayoutManager(layoutManager);

                            progressBar.setVisibility(View.GONE);
                            progressBarText.setVisibility(View.GONE);
                            homePageResults.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG,"Cheb Error :" + error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    private void setWeatherCard_2(double humidity, double pressure, double visibility, double windSpeed) {
        //All numbers are already rounded off to the required format
        ((TextView) view.findViewById(R.id.humidityCard)).setText(String.format("%s%%", String.valueOf(humidity)));
        ((TextView) view.findViewById(R.id.pressureCard)).setText(String.format("%s mb", String.valueOf(pressure)));
        ((TextView) view.findViewById(R.id.visibilityCard)).setText(String.format("%s km", String.valueOf(visibility)));
        ((TextView) view.findViewById(R.id.windSpeedCard)).setText(String.format("%s mph", String.valueOf(windSpeed)));
    }

    private void setTimeZone(String timezone) {
        ((TextView) view.findViewById(R.id.timezone)).setText(address);
    }

    private void setWeatherSummary(String summary) {
        ((TextView) view.findViewById(R.id.summary)).setText(summary);
    }

    private void setTemperature(int temperature) {
        TextView tempTextView = view.findViewById(R.id.temperature);
        tempTextView.setText(String.format("%s℉", String.valueOf(temperature)));
    }

    private void setWeatherIcon(String icon, ImageView currentWeatherIcon) {
        icon = icon.toLowerCase();
        switch(icon) {
            case "clear-night":
                currentWeatherIcon.setImageResource(R.drawable.weather_night);
                break;

            case "rain":
                currentWeatherIcon.setImageResource(R.drawable.weather_rainy);
                break;

            case "sleet":
                currentWeatherIcon.setImageResource(R.drawable.weather_snowy_rainy);
                break;

            case "snow":
                currentWeatherIcon.setImageResource(R.drawable.weather_snowy);
                break;

            case "wind" :
                currentWeatherIcon.setImageResource(R.drawable.weather_windy_variant);
                break;

            case "fog":
                currentWeatherIcon.setImageResource(R.drawable.weather_fog_white);
                break;

            case "cloudy":
                currentWeatherIcon.setImageResource(R.drawable.weather_cloudy);
                break;

            case "partly-cloudy-night":
                currentWeatherIcon.setImageResource(R.drawable.weather_night_partly_cloudy);
                break;

            case "partly-cloudy-day":
                currentWeatherIcon.setImageResource(R.drawable.weather_partly_cloudy);
                break;

            default:
                currentWeatherIcon.setImageResource(R.drawable.weather_sunny);
                break;
        }
    }

    private void setDailyWeatherData(JSONArray dailyWeatherData) {
        this.dailyWeatherArrayList = new ArrayList<DailyWeather>();
        for (int i = 0; i < dailyWeatherData.length(); i++) {
            try {
                JSONObject dayWeather = dailyWeatherData.getJSONObject(i);
                String minTempValue = "N/A", maxTempValue = "N/A";
                if (dayWeather.has("temperatureLow") && !dayWeather.isNull("temperatureLow")){
                    minTempValue = String.valueOf(dayWeather.getInt("temperatureLow"));
                }
                if (dayWeather.has("temperatureHigh") && !dayWeather.isNull("temperatureHigh")) {
                    maxTempValue = String.valueOf(dayWeather.getInt("temperatureHigh"));
                }
                Date dateVal = new Date(dayWeather.getLong("time") * 1000);
                String pattern = "MM/dd/yyyy";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String dateStringVal = simpleDateFormat.format(dateVal);
                String climateSummary = dayWeather.getString("icon");
                dailyWeatherArrayList.add(new DailyWeather(dateStringVal, minTempValue, maxTempValue, climateSummary));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setNewactivity(View view) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.weatherBoxMain:
                Log.i(TAG,"Clicked on weather Box main!!! Cheb");
        }
    }

    public void addToFavourites(View view) {
//        MainActivity.customPageAdaptor.addFragment(locationSearchFragment);
//        MainActivity.customPageAdaptor.notifyDataSetChanged();
        SharedPreferences.Editor sharedpreferencesEditor = sharedPreferences.edit();
        String key = address;
        sharedpreferencesEditor.putString(key, key);
        sharedpreferencesEditor.commit();
        //change button to marker-map-minus.
    }

    public void deleteFromFavourites() {
        SharedPreferences.Editor sharedprefEditor = sharedPreferences.edit();
        String key = address;
        sharedprefEditor.remove(key);
        sharedprefEditor.apply();
    }
}
