package com.example.easemyride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Locale;

public class map_petrol_Activity extends FragmentActivity implements OnMapReadyCallback {
    private ImageView iv_back,btn_loc;
    private TextView tv_address,tv_city,tv_state,tv_country,tv_fullAddress;
    private Button btn_next;
    private LinearLayout ll_address;


    Location currentLoc;
    FusedLocationProviderClient fusedLocationProviderClient;

    public String sp_value;

    private static final int LOCATION_REQUEST_CODE = 100;
    private String[] locationPermissions;
    private double latitude, longitude;
    private LocationManager locationManager;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_petrol);

        ll_address=findViewById(R.id.ll_address);
        iv_back=findViewById(R.id.iv_back);
        btn_next=findViewById(R.id.btn_next);
        tv_address=findViewById(R.id.tv_address);
        btn_loc=findViewById(R.id.btn_loc);
        tv_city=findViewById(R.id.tv_city);
        tv_state=findViewById(R.id.tv_state);
//        tv_fullAddress=findViewById(R.id.tv_fullAddress);
        btn_next=findViewById(R.id.btn_next);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLoc();

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(map_petrol_Activity.this,order_petrol_activity.class);
                intent.putExtra("lat",String.valueOf(latitude));
                intent.putExtra("long",String.valueOf(longitude));
                startActivity(intent);
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLoc();
                Toast.makeText(map_petrol_Activity.this, "Updating Location", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getLastLoc() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            },LOCATION_REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    currentLoc=location;
                    latitude=currentLoc.getLatitude();
                    longitude=currentLoc.getLongitude();
                    updateUI();
                    SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fr_map);
                    supportMapFragment.getMapAsync(map_petrol_Activity.this);
                }
            }
        });

    }

    private void updateUI() {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder=new Geocoder(this, Locale.getDefault());

        try{
            addresses=geocoder.getFromLocation(latitude,longitude,1);

            String address=addresses.get(0).getAddressLine(0);
            String city=addresses.get(0).getLocality();
            String state=addresses.get(0).getAdminArea();

            tv_address.setText(address);
//            tv_fullAddress.setText(address);
            tv_city.setText(city);
            tv_state.setText(state);
        }
        catch(Exception e){
            Toast.makeText(this, "Error"+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case LOCATION_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean locationAccepted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    if(locationAccepted){
                        //allowed
                        getLastLoc();
                    }
                    else{
                        //denied
                        Toast.makeText(this, "Location Permission is Necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng=new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("Here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        googleMap.addMarker(markerOptions);

    }
}