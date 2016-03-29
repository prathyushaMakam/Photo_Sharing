package com.prathyusha.photosharing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PotholeDetailActivity extends AppCompatActivity {

    private String LOG_TAG ="PotholeDetailActivity";
    private String mPotholeId;
    private int mItemId;
    private String mPotholeUserId;
    private String mPotholeDescription;
    private String mImageType;
    private TextView mDescription;
    private ImageView mPotholeImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pothole_detail);

        Intent intent = getIntent();
        mPotholeId = intent.getStringExtra(PotholeListActivity.POTHOLE_ID);
        mItemId  = intent.getIntExtra(PotholeListActivity.ITEM_ID, 0);
        mDescription =(TextView)findViewById(R.id.PotholeDescription);
        mPotholeImage= (ImageView)findViewById(R.id.PotholeImage);

        getJsonObjects();

    }

    public void getJsonObjects()
    {
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        //  Create json array request
        Log.d(LOG_TAG, "After Json requestQueue");
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST, "http://bismarck.sdsu.edu/city/batch?type=street&user=rew",new Response.Listener<JSONArray>(){
            public void onResponse(JSONArray jsonArray){
                Log.d(LOG_TAG,"Inside onResponse");

                // Successfully download json
                // So parse it and populate the listview
                    try {
                        JSONObject Id = jsonArray.getJSONObject(mItemId);
                        JSONObject Description=jsonArray.getJSONObject(mItemId);
                        JSONObject ImageType=jsonArray.getJSONObject(mItemId);

                        mPotholeUserId = Id.getString("id");
                        mPotholeDescription=Description.getString("description");
                        mImageType = ImageType.getString("imagetype");

                        mDescription.setText(mPotholeDescription);
                        if(!mImageType.equals("none") )
                        {
                            Log.d(LOG_TAG,"Image type is not None");
                            DisplayImage();
                        }
                        Log.d(LOG_TAG, "mPotholeIdValue[" + mItemId + "]= "+ mPotholeUserId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                      }
            }

           // }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Error", "Unable to parse json array");
            }
        });
        // add json array request to the request queue
        requestQueue.add(jsonArrayRequest);
    }
    public void DisplayImage() {
        Response.Listener<Bitmap> success = new Response.Listener<Bitmap>() {
            public void onResponse(Bitmap response) {
                mPotholeImage.setImageBitmap(response);

            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
            }
        };
        String url = "http://bismarck.sdsu.edu/city/image?id="+mPotholeId;
        ImageRequest ir = new ImageRequest(url,
                success, 0, 0, ImageView.ScaleType.CENTER_INSIDE, null, failure);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(ir);
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
        else if(item.getTitle().equals("List View"))
        {
            Intent ListViewIntent = new Intent(this,PotholeListActivity.class);
            startActivity(ListViewIntent);
        }
        else if(item.getTitle().equals("Map View"))
        {
            Intent MapViewIntent = new Intent(this,MapViewActivity.class);
            startActivity(MapViewIntent);
        }
        return false;
    }

}
