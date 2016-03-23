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

      /*  String url = "http://i.imgur.com/Nwk25LA.jpg";
        mImageView = (ImageView) findViewById(R.id.image);

        ImageRequest imgRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        mImageView.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, Bitmap.Config.ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mImageView.setBackgroundColor(Color.parseColor("#ff0000"));
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(this).add(imgRequest);*/


    }

    public void NewItem(View view) {
        Intent newItem = new Intent(this,Add_New_Pothole.class);
        startActivity(newItem);
    }

    public void ListView(View view) {
    }

    public void MapView(View view) {
    }


}
