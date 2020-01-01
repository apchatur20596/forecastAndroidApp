package com.example.weatherapp;

import android.widget.ImageView;

public class WeatherUIUtils {
    public static void setWeatherIcon(String icon, ImageView currentWeatherIcon) {
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
}
