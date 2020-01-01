package com.example.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DailyWeatherTableAdapter extends
        RecyclerView.Adapter<DailyWeatherTableAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    private List<DailyWeather> dailyWeatherList;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView minTemp;
        public TextView maxTemp;
        public ImageView weatherIcon;



        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.day);
            minTemp =  itemView.findViewById(R.id.dayMinTemp);
            maxTemp = itemView.findViewById(R.id.dayMaxTemp);
            weatherIcon = itemView.findViewById(R.id.dayWeatherIcon);
        }
    }

    public DailyWeatherTableAdapter(List<DailyWeather> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }

    @Override
    public DailyWeatherTableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View dailyWeatherView = inflater.inflate(R.layout.daily_weather_layout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(dailyWeatherView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DailyWeatherTableAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        DailyWeather dailyWeather = this.dailyWeatherList.get(position);

        // Set item views based on your views and data model
        TextView dateTextView = viewHolder.date;
        dateTextView.setText(dailyWeather.getDate());
        TextView minTempTextView = viewHolder.minTemp;
        minTempTextView.setText(dailyWeather.getMinTemperature());
        TextView maxTempTextView = viewHolder.maxTemp;
        maxTempTextView.setText(dailyWeather.getMaxTemperature());
        ImageView climateIconView = viewHolder.weatherIcon;
        getWeatherIcon(dailyWeather.getWeatherSummary(), climateIconView);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return dailyWeatherList.size();
    }

    private void getWeatherIcon(String icon, ImageView currentWeatherIcon) {
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
