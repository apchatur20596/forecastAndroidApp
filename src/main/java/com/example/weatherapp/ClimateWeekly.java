package com.example.weatherapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ClimateWeekly extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;

    private LineChart chart;
    private TextView tvX, tvY;

    // TODO: Rename and change typesof parameters
    private String mParam1;
    private String mParam2;

    public ClimateWeekly() {
        // Required empty public constructor
    }

    public static ClimateWeekly newInstance(String param1, String param2) {
        ClimateWeekly fragment = new ClimateWeekly();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_climate_weekly, container, false);
        populateWeeklyWeatherTab(((DetailedWeather) getActivity()).getDarkSkyData());
        addWeeklyWeatherGraphs();
        return view;
    }

    private void addWeeklyWeatherGraphs() {
        int orange_color = ContextCompat.getColor(getActivity(), R.color.orange);
        int purple_color = ContextCompat.getColor(getActivity(), R.color.purple);
        int white_color = ContextCompat.getColor(getActivity(), R.color.white);

        chart = view.findViewById(R.id.chart1);
        LineDataSet lineDataSet1= new LineDataSet(minTempValues, "Minimum Temperature");
        lineDataSet1.setColor(purple_color);

        LineDataSet lineDataSet2= new LineDataSet(maxTempValues, "Maximum Temperature");
        lineDataSet2.setColor(orange_color);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet1);
        dataSets.add(lineDataSet2);

        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setTextColor(white_color); // left y-axis
        chart.getXAxis().setTextColor(white_color);
        chart.getAxisRight().setTextColor(white_color);
        chart.getLegend().setTextColor(white_color);
        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.invalidate();
    }
    ArrayList<Entry> minTempValues;
    ArrayList<Entry> maxTempValues;
    ArrayList<Integer> daysOfWeek;
    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range) - 30;
            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.star)));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
    }

    private void populateWeeklyWeatherTab(String darkSkyData) {
        minTempValues = new ArrayList<>();
        maxTempValues = new ArrayList<>();
        daysOfWeek = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(darkSkyData);
            JSONObject daily = (JSONObject) jsonObject.get("daily");
            String summary = daily.getString("summary");
            ((TextView) view.findViewById(R.id.weeklySummaryText)).setText(summary);
            ImageView weeklyIcon = (ImageView) view.findViewById(R.id.weeklyClimateIcon);
            WeatherUIUtils.setWeatherIcon(daily.getString("icon"), weeklyIcon);
            JSONArray dailyWeatherArray = (JSONArray) daily.getJSONArray("data");
            ArrayList<DailyWeather> dailyWeatherArrayList = new ArrayList<DailyWeather>();
            for(int i = 0; i < dailyWeatherArray.length(); i++) {
                JSONObject dayWeather = dailyWeatherArray.getJSONObject(i);
                minTempValues.add(new Entry(i, dayWeather.getInt("temperatureLow")));
                maxTempValues.add(new Entry(i, dayWeather.getInt("temperatureHigh")));
                daysOfWeek.add(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
