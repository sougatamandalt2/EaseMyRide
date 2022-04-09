package com.example.easemyride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class petrolActivity extends AppCompatActivity {

    private EditText edt_address,edt_quantity;
    private TextView tv_total,tv_delivery,tv_amtPrice;
    private ImageButton btn_loc;
    private Button btn_confirm;

    private static final int LOCATION_REQUEST_CODE = 100;

    private String[] locationPermissions;

    private double latitude, longitude;

    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petrol);

        edt_address=findViewById(R.id.edt_address);
        btn_loc=findViewById(R.id.btn_loc);
        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

        btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(checkLocationPermission()){
//                    detectLocation();
//                }
//                else{
//                    requestLocationPermission();
//                }
            }
        });
    }

}