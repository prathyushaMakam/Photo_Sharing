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
import android.view.View;
import android.widget.Button;
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

    private static final String URL = "http://bismarck.sdsu.edu/city/report?type=street";
    String LOG_TAG = "Uploading";
    private File file;
    private Bitmap bitmapPhoto;
    private String mimgString;
    private String mimgType;


    private static final int CAMERA_REQUEST = 1;
    private ImageView mdisplayPhoto;
    private Button mphotoButton;
    private EditText mpotholeInfo;
    private Button muploadButton;
    private Button mgpsButton;
    String mCurrentPhotoPath;
    static Uri capturedImageUri = null;
    public double latitude;
    public double longitude;

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__pothole);

        mdisplayPhoto = (ImageView) this.findViewById(R.id.DisplayPhoto);
        //mphotoButton = (Button) this.findViewById(R.id.PhotoButton);
        mpotholeInfo = (EditText) this.findViewById(R.id.PotholeInfo);
        muploadButton = (Button) this.findViewById(R.id.UploadButton);
       // mgpsButton = (Button) this.findViewById(R.id.GPSButton);

        GPScoordinates();

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
           //   Bitmap photo = (Bitmap) data.getExtras().get("data");
           //   mdisplayPhoto.setImageBitmap(photo);
            try {
                bitmapPhoto = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), capturedImageUri);
                int width = bitmapPhoto.getWidth();
                int height = bitmapPhoto.getHeight();
                Log.d(LOG_TAG, "Width & HEight: " + width + " x " + height);
                 Bitmap resizedPhoto = Bitmap.createScaledBitmap(
                        bitmapPhoto, 700, 676, false);
                bitmapPhoto = resizedPhoto;
                Log.d(LOG_TAG, "NewWidth & NewHEight: " + bitmapPhoto.getWidth() + " x " + bitmapPhoto.getHeight());
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

          //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else {
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

  /*  public void GPSButton(View view) {
        // create class object
        gps = new GPSTracker(Add_New_Pothole.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

             latitude = gps.getLatitude();
             longitude = gps.getLongitude();

            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else {
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }*/

    public void UploadButton(View view) {
        UploadReport();
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
        Log.d(LOG_TAG, "json obj: " + jObj.toString());

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, postUrl, jObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(LOG_TAG, "inside lat onResponse: " + response.toString());
                        Toast.makeText(Add_New_Pothole.this,"Report about Pothole is uploaded",Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_TAG, "inside lat errorResponse: "+ error.getMessage() + ", tostring: " + error.toString());
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

}
