package com.example.easemyride;
//
////importing packages
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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

import java.util.List;
import java.util.Locale;

public class petrolActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    private EditText edt_address,edt_quantity;
//    private TextView tv_total,tv_delivery,tv_amtPrice;
//    private ImageButton btn_loc;
//    private Button btn_confirm;
//
//    Location currentLoc;
//    FusedLocationProviderClient fusedLocationProviderClient;
//
//    private static final int LOCATION_REQUEST_CODE = 100;
//
//    private String[] locationPermissions;
//
//    private double latitude, longitude;
//
//    private LocationManager locationManager;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_petrol);
//
//        edt_address=findViewById(R.id.edt_address);
//        btn_loc=findViewById(R.id.btn_loc);
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
//
//        btn_loc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(checkLocationPermission()){
//                    getLastLoc();
//                }
//                else{
//                    requestLocationPermission();
//                }
//            }
//        });
//    }
//
//    private void getLastLoc() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,new String[]
//                    {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if(location!=null){
//                    currentLoc=location;
//                    latitude=currentLoc.getLatitude();
//                    longitude=currentLoc.getLongitude();
//                    updateUI();
//                    Toast.makeText(petrolActivity.this, ""+currentLoc.getLatitude()+" "+currentLoc.getLongitude(), Toast.LENGTH_SHORT).show();
//                    SupportMapFragment supportMapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fr_map);
//                    supportMapFragment.getMapAsync(petrolActivity.this);
//                }
//            }
//
//        });
//
//    }
//
//    private void updateUI() {
//
//        Geocoder geocoder;
//        List<Address> addresses;
//        geocoder=new Geocoder(this, Locale.getDefault());
//
//        try{
//            addresses=geocoder.getFromLocation(latitude,longitude,1);
//
//            String address=addresses.get(0).getAddressLine(0);
//
//            edt_address.setText(address);
//        }
//        catch(Exception e){
//            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    private Boolean checkLocationPermission(){
//        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==(PackageManager.PERMISSION_GRANTED);
//        return result;
//    }
//
//    private void requestLocationPermission(){
//        ActivityCompat.requestPermissions(this,locationPermissions,LOCATION_REQUEST_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case LOCATION_REQUEST_CODE: {
//                if (grantResults.length > 0) {
//                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    if (locationAccepted) {
//                        //allowed
//                        getLastLoc();
//                    } else {
//                        //denied
//                        Toast.makeText(this, "Location Permission is Necessary...", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            break;
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//
//        LatLng latLng=new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude());
//        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("Here");
//        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));
//        googleMap.addMarker(markerOptions);
//
    }
//
}