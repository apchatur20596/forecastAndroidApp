package com.example.weatherapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ClimatePhotos extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View view;
    private String city;
    public ClimatePhotos() {
        // Required empty public constructor
    }

    public static ClimatePhotos newInstance(String param1, String param2) {
        ClimatePhotos fragment = new ClimatePhotos();
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
        view = inflater.inflate(R.layout.fragment_climate_photos, container, false);
        populatePhotosOfCity();
        return view;
    }
    private static final String TAG = "FragmentClimatePhotos";
    private ArrayList<PhotoItem> imagesUrl;

    private void populatePhotosOfCity() {
        /**
         * 1. Create volley request
         * 2. Parse the images links and store in an ArrayList
         * 3. Create your recycler view
         * 4.
         */
        String address = ((DetailedWeather)getActivity()).getAddress();
        city = address.split(",")[0];
//        String baseUrl = "http://10.0.2.2:3000";
        String baseUrl = "http://weather-droid.us-east-2.elasticbeanstalk.com";
        String photos_url = baseUrl + "/seal/" + city;
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, photos_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Gooogle photos response : " + response.toString());
                        try {
                            JSONArray imageObject = response.getJSONArray("items");
                            parseImageLinks(imageObject);
                            addPhotosToFragment();

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
        }
        );
        requestQueue.add(jsonObjectRequest);

    }

    private void addPhotosToFragment() {
        RecyclerView photosRecyclerView = (RecyclerView) view.findViewById(R.id.PhotosRecyclerView);
        PhotosAdapter photosAdapter = new PhotosAdapter(getActivity(), imagesUrl);
        photosRecyclerView.setAdapter(photosAdapter);
        photosRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void parseImageLinks(JSONArray imageObject) {
        this.imagesUrl = new ArrayList<>();
        for(int i = 0; i < imageObject.length(); i++ ) {
            try {
                JSONObject iObj = imageObject.getJSONObject(i);
                String url = iObj.getString("link");
                imagesUrl.add(new PhotoItem(url));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "ImagesUrl: " + imagesUrl.get(0).getPhotoUrl());
    }

}
