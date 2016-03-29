package com.prathyusha.photosharing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mnewPotHole;
    private Button mlistView;
    private Button mmapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mnewPotHole = (Button) this.findViewById(R.id.NewItemButton);
        mlistView = (Button) this.findViewById(R.id.ListViewButton);
        mmapView = (Button) this.findViewById(R.id.MapViewButton);
    }

    public void NewItem(View view) {
        Intent newItem = new Intent(this,Add_New_Pothole.class);
        startActivity(newItem);
    }

    public void ListView(View view) {
        Intent potholeList = new Intent(this,PotholeListActivity.class);
        startActivity(potholeList);
    }

    public void MapView(View view) {
        Intent MapViewIntent = new Intent(this,MapViewActivity.class);
        startActivity(MapViewIntent);
    }


}
