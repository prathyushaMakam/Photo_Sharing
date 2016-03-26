package com.prathyusha.photosharing;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Pothole_List_Activity extends AppCompatActivity {

    String LOG_TAG ="Pothole List Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pothole__list_);
        setTitle("Pothole List");

        if(savedInstanceState == null){
           // FragmentManager listManager = getSupportFragmentManager();

        }
    }
}
