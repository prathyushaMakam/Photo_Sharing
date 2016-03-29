package com.prathyusha.photosharing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;


public class Add_New_Pothole extends AppCompatActivity {

    String LOG_TAG = "Uploading";
    private File file;
    private Bitmap bitmapPhoto;
    private String mimgString;
    private String mimgType;


    private static final int CAMERA_REQUEST = 1;
    private ImageView mdisplayPhoto;
    private EditText mpotholeInfo;
    static Uri capturedImageUri = null;
    public double latitude;
    public double longitude;


    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__pothole);

        mdisplayPhoto = (ImageView) this.findViewById(R.id.DisplayPhoto);
        mpotholeInfo = (EditText) this.findViewById(R.id.PotholeInfo);
    }

    public void PhotoButton(View view) {
        Calendar calender = Calendar.getInstance();
        file = new File(Environment.getExternalStorageDirectory(), (calender.getTimeInMillis() + ".jpg"));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        capturedImageUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,capturedImageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
        Log.d(LOG_TAG, "Image Captured:(its Uri) " + capturedImageUri);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                bitmapPhoto = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), capturedImageUri);
                 Bitmap resizedPhoto = Bitmap.createScaledBitmap(
                        bitmapPhoto, 700, 676, false);
                bitmapPhoto = resizedPhoto;
                mdisplayPhoto.setImageBitmap(bitmapPhoto);

            }catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }
    public void GPScoordinates() {
        // create class object
        gps = new GPSTracker(Add_New_Pothole.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }else {
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    public void UploadButton(View view) {
        GPScoordinates();
        UploadReport();
        Toast.makeText(getApplicationContext(),"Pothole Information is Uploaded.",Toast.LENGTH_SHORT).show();
        mpotholeInfo.setText("");
        mdisplayPhoto.setImageDrawable(null);
    }
    private void UploadReport()
    {
        String postUrl = "http://bismarck.sdsu.edu/city/report";
        Log.d(LOG_TAG, "before lat request: " + postUrl);

        if(bitmapPhoto != null)
        {
            mimgType = "jpg";
            convertImageToBase64();


        }
        else {
            mimgType = "none";
            mimgString = null;
            Log.d(LOG_TAG,"no image");
        }
        JSONObject jObj = new JSONObject();
        try {
            jObj.put("type", "street");
            jObj.put("latitude", latitude);
            jObj.put("longitude", longitude);
            jObj.put("user", "rew");
            jObj.put("imagetype", mimgType);
            jObj.put("description", mpotholeInfo.getText().toString());
            jObj.put("image",mimgString);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, postUrl, jObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(LOG_TAG, "Uploading onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_TAG, "Uploading errorResponse: "+ error.getMessage() + ", tostring: " + error.toString());
                        Toast.makeText(Add_New_Pothole.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private String convertImageToBase64(){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapPhoto.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        mimgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return mimgString;

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
