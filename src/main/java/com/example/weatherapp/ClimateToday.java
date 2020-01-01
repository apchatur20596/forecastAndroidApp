package com.example.weatherapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClimateToday.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClimateToday#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClimateToday extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;

//    private OnFragmentInteractionListener mListener;

    public ClimateToday() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClimateToday.
     */
    // TODO: Rename and change types and number of parameters
    public static ClimateToday newInstance(String param1, String param2) {
        ClimateToday fragment = new ClimateToday();
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
        view = inflater.inflate(R.layout.fragment_climate_today, container, false);
        populateWeatherTodayTab(((DetailedWeather)getActivity()).getDarkSkyData());
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    private void populateWeatherTodayTab(String darkSkyData) {
        //set progress bar visible
        try {
            JSONObject jsonObj = new JSONObject(darkSkyData);
            JSONObject currently = (JSONObject) jsonObj.get("currently");
            double humidity = 0;
            double pressure = 0;
            double visibility = 0;
            double windSpeed = 0;
            double temperature = 0;
            double ozone = 0;
            double cloudCover = 0;
            double precipitation = 0;

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
            if(currently.has("temperature") && !currently.isNull("temperature")) {
                temperature = currently.getInt("temperature");
            }
            if(currently.has("ozone") && !currently.isNull("ozone")) {
                ozone = Math.round(100.0 * currently.getDouble("ozone")) / 100.0;
            }
            if(currently.has("cloudCover") && !currently.isNull("cloudCover")) {
                cloudCover = Math.round(10000.0 * currently.getDouble("cloudCover")) / 100.0;
            }
            if(currently.has("precipIntensity") && !currently.isNull("precipIntensity")) {
                precipitation = Math.round(100.0 * currently.getDouble("precipIntensity")) / 100.0;
            }

            String summary = currently.getString("icon").replace("partly-", "");
            summary = summary.replace("-", " ");

            WeatherUIUtils.setWeatherIcon(currently.getString("icon"), (ImageView) view.findViewById(R.id.summaryIcon2));
            ((TextView)view.findViewById(R.id.windSpeedValue2)).setText(String.format("%s mph", String.valueOf(windSpeed)));
            ((TextView)view.findViewById(R.id.pressureValue2)).setText(String.format("%s mb", String.valueOf(pressure)));
            ((TextView)view.findViewById(R.id.precipitationValue2)).setText(String.format("%s mmph", String.valueOf(precipitation)));
            ((TextView)view.findViewById(R.id.temperatureValue2)).setText(String.format("%s℉", String.valueOf(temperature)));
            ((TextView)view.findViewById(R.id.humidityValue2)).setText(String.format("%s%%", String.valueOf(humidity)));
            ((TextView)view.findViewById(R.id.visibilityValue2)).setText(String.format("%s km", String.valueOf(visibility)));
            ((TextView)view.findViewById(R.id.cloudCoverValue2)).setText(String.format("%s%%", String.valueOf(cloudCover)));
            ((TextView)view.findViewById(R.id.ozoneValue2)).setText(String.format("%s DU", String.valueOf(ozone)));
            ((TextView)view.findViewById(R.id.summaryTextValue)).setText(String.format("%s", String.valueOf(summary)));

            //set progress bar invisibile
            ((ProgressBar)view.findViewById(R.id.progressBar)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.progressBarMessage)).setVisibility(View.GONE);
            ((LinearLayout) view.findViewById(R.id.detailedSearchResults)).setVisibility(View.VISIBLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
