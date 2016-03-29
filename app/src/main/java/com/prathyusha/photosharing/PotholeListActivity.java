package com.prathyusha.photosharing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PotholeListActivity extends AppCompatActivity {

    public final static String POTHOLE_ID ="PotholeID";
    public final static String ITEM_ID ="ItemID";

    private int itemId;
    private ArrayAdapter<String> adapterArray;
    private ArrayList<String> itemsArray;
    private ListView mListView;
    private ArrayList<String> mPotholeIdValue;
    private String LOG_TAG="PotholeListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pothole_list);
        setTitle("Pothole List");

        mListView = (ListView)findViewById(R.id.list_view);
        itemsArray = new ArrayList<String>();
        mPotholeIdValue = new ArrayList<String>();
        adapterArray = new ArrayAdapter(this,R.layout.rowlayout,R.id.TextLabel,itemsArray);
        mListView.setAdapter(adapterArray);
        DisplayList();


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemId = (int)id;
                Log.d(LOG_TAG, " setOnItemClickListener ,mPotholeIdValue[" + itemId + "]= " + mPotholeIdValue.get(itemId));
                Intent detailActivity = new Intent(PotholeListActivity.this, PotholeDetailActivity.class);
                detailActivity.putExtra(ITEM_ID, itemId);
                detailActivity.putExtra(POTHOLE_ID,  mPotholeIdValue.get(itemId));
                startActivity(detailActivity);
            }
        });
    }

    public void DisplayList(){
        // Create request queue
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        //  Create json array request
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST,"http://bismarck.sdsu.edu/city/batch?type=street&user=rew",new Response.Listener<JSONArray>(){
            public void onResponse(JSONArray jsonArray){
                // Successfully download json
                // So parse it and populate the listview
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        itemsArray.add("Pothole " + i);
                        mPotholeIdValue.add(jsonObject.getString("id"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterArray.notifyDataSetChanged();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getTitle().equals("Home"))
        {
            Intent homeIntent = new Intent(this,MainActivity.class);
            startActivity(homeIntent);
        }
        else if(item.getTitle().equals("Add New Pothole"))
        {
            Intent AddNewIntent = new Intent(this,Add_New_Pothole.class);
            startActivity(AddNewIntent);
        }
        else if(item.getTitle().equals("Map View"))
        {
            Intent MapViewIntent = new Intent(this,MapViewActivity.class);
            startActivity(MapViewIntent);
        }
        return false;
    }
}
