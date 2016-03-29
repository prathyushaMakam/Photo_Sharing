package com.prathyusha.photosharing;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Double> mLatitude;
    private ArrayList<Double> mLongitude;
    private ArrayList<String> mPotholeIdValue;
    private String LOG_TAG="MapView";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        Log.d(LOG_TAG, "Indide OnCreate");

        mLatitude = new ArrayList<Double>();
        mLongitude = new ArrayList<Double>();
        mPotholeIdValue = new ArrayList<String>();

        GetAllLatLng();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }

    public void GetAllLatLng()
    {
        Log.d(LOG_TAG,"Inside GetAllLatlng");
        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        Log.d(LOG_TAG, "After RequestQueue");

        //  Create json array request
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST, "http://bismarck.sdsu.edu/city/batch?type=street&user=rew",new Response.Listener<JSONArray>(){
            public void onResponse(JSONArray jsonArray){
                Log.d(LOG_TAG,"After Json Array Request n B4 For loop");

                // Successfully download json
                // So parse it and populate the listview
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        Log.d(LOG_TAG,"Inside Try in for loop");

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        Log.d(LOG_TAG,"After Initialising json Objects");

                        mPotholeIdValue.add(jsonObject.getString("id"));
                        Log.d(LOG_TAG, "Id value:"+mPotholeIdValue.get(i));

                        mLatitude.add(jsonObject.getDouble("latitude"));
                        Log.d(LOG_TAG, "Lat value:" + mLatitude.get(i));

                        mLongitude.add(jsonObject.getDouble("longitude"));
                        Log.d(LOG_TAG, "Lng value:" + mLongitude.get(i));



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error", "Unable to parse json array");
            }
        });
        // add json array request to the request queue
        requestQueue.add(jsonArrayRequest);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(LOG_TAG, "Inside MapReady, Lat.Size: "+mLatitude.size());

        for(int i=0; i<mLatitude.size(); i++) {
            Log.d(LOG_TAG, "Lat + Lng :" + mLatitude.get(i) + ": " + mLongitude.get(i) + ", ID: " + mPotholeIdValue.get(i));
        }
        mMap = googleMap;

        // Add a marker's in Map and move the camera
            LatLng coordinates = new LatLng(32.888, -117.888);
            mMap.addMarker(new MarkerOptions().position(coordinates).title("Pothole "));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinates));

    }

}
