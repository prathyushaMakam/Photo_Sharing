package com.prathyusha.photosharing;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class Add_New_Pothole extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    private ImageView mdisplayPhoto;
    private Button mphotoButton;
    private EditText mpotholeInfo;
    private Button muploadButton;
    private Button mgpsButton;

    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__pothole);

        mdisplayPhoto = (ImageView) this.findViewById(R.id.DisplayPhoto);
        mphotoButton = (Button) this.findViewById(R.id.PhotoButton);
        mpotholeInfo = (EditText) this.findViewById(R.id.PotholeInfo);
        muploadButton = (Button) this.findViewById(R.id.UploadButton);
        mgpsButton = (Button) this.findViewById(R.id.GPSButton);
        

    }

    public void PhotoButton(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mdisplayPhoto.setImageBitmap(photo);
        }
    }

    public void GPSButton(View view) {
        // create class object
        gps = new GPSTracker(Add_New_Pothole.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
}
