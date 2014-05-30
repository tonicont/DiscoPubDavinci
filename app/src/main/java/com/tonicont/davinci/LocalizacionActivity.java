package com.tonicont.davinci;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



/**
 * Created by Toni on 7/03/14.
 */
public class LocalizacionActivity extends Activity implements View.OnClickListener {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localizacion);
        ImageButton btn_atras = (ImageButton)findViewById(R.id.btn_atras);
        btn_atras.setOnClickListener(this);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.64536710004887, -3.907343629497518))
                .title("Disco Pub Davinci"));

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_atras:{
                finish();
                /*
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                */
            }
        }
    }
}
