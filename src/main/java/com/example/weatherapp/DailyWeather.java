package com.example.weatherapp;

import java.util.Date;

public class DailyWeather {
    String date;
    String minTemperature;
    String maxTemperature;
    String weatherSummary;

    public DailyWeather(String date, String minTemperature, String maxTemperature, String weatherSummary){
        this.date = date;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.weatherSummary = weatherSummary;
    }
    public String getWeatherSummary(){
        return weatherSummary;
    }

    public void setWeatherSummary(String weatherSummary) {
        this.weatherSummary = weatherSummary;
    }

    public String getDate() {
        return date;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
