package com.example.easemyride;

//importing packages

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class petrolActivity extends FragmentActivity implements OnMapReadyCallback{

    private EditText edt_address;
    private TextView tv_total,tv_fuelPrice, tv_delivery,tv_delPrice, tv_amtPrice,edt_quantity;
    private ImageButton btn_loc;
    private Button btn_confirm,btn_map,btn_checkout;
    private Spinner sp_menu;
    private FloatingActionButton btn_orders;

    public Double allTotalPrice=0.00;

    Location currentLoc;
    FusedLocationProviderClient fusedLocationProviderClient;

    private static final int LOCATION_REQUEST_CODE = 100;

    private String[] locationPermissions;

    private double latitude, longitude;

    private LocationManager locationManager;

    private ArrayList<modelOrders> orderItemList;

    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    public String sp_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petrol);

        edt_address=findViewById(R.id.edt_address);
        edt_quantity=findViewById(R.id.edt_quantity);
        sp_menu=findViewById(R.id.sp_menu);
        tv_total=findViewById(R.id.tv_total);
        tv_delivery=findViewById(R.id.tv_delivery);
        tv_fuelPrice=findViewById(R.id.tv_fuelPrice);
        tv_delPrice=findViewById(R.id.tv_delPrice);
        tv_amtPrice=findViewById(R.id.tv_amtPrice);
        btn_confirm=findViewById(R.id.btn_confirm);
        btn_orders=findViewById(R.id.btn_orders);
//        btn_checkout=findViewById(R.id.btn_checkout);

        firebaseAuth=FirebaseAuth.getInstance();

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLoc();

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.quantities,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        sp_menu.setAdapter(adapter);

        tv_delPrice.setText("100");

        sp_menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sp_value=String.valueOf(adapterView.getItemAtPosition(i));

                int fuel_price=Integer.parseInt(sp_value)*100;
                tv_fuelPrice.setText(String.valueOf(fuel_price));

                int amt=Integer.parseInt(tv_fuelPrice.getText().toString().trim())+Integer.parseInt(tv_delPrice.getText().toString().trim());
                tv_amtPrice.setText(String.valueOf(amt));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title="Fuel:Petrol";
                String finalPrice=tv_amtPrice.getText().toString().trim().replace("$","");
                String delPrice=tv_delPrice.getText().toString().trim().replace("$","");
                String quantity=sp_value;
                addToCart(title,delPrice,quantity,finalPrice);

//                makeOrder(view);


            }
        });

        btn_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeOrder(view);

                submitOrder();

                startActivity(new Intent(petrolActivity.this,OrdersActivity.class));
            }
        });

    }

    private int orderID=0;
    private void addToCart(String title, String delPrice, String quantity, String finalPrice) {

        EasyDB easyDB=EasyDB.init(petrolActivity.this,"ITEMS_DB")
                .addColumn(new Column("ORDER_ID",new String[]{"text","unique"}))
                .addColumn(new Column("ITEM_PID",new String[]{"text","not null"}))
                .addColumn(new Column("ORDER_Name",new String[]{"text","not null"}))
                .addColumn(new Column("ORDER_Rate",new String[]{"text","not null"}))
                .addColumn(new Column("Order_totalPrice",new String[]{"text","not null"}))
                .addColumn(new Column("ORDER_quantity",new String[]{"text","not null"}))
                .doneTableColumn();

        orderID++;
        boolean b=easyDB.addData("ORDER_ID",orderID)
                .addData("ORDER_Name",title)
                .addData("Order_totalPrice",finalPrice)
                .addData("ORDER_quantity",quantity)
                .doneDataAdding();

        Toast.makeText(petrolActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();

    }

    private void makeOrder(View view) {
        orderItemList=new ArrayList<>();

        AlertDialog.Builder builder=new AlertDialog.Builder(petrolActivity.this);
        builder.setView(view);

        EasyDB easyDB=EasyDB.init(petrolActivity.this,"ORDERS_DB")
                .addColumn(new Column("ORDER_ID",new String[]{"text","unique"}))
                .addColumn(new Column("PRODUCT_PID",new String[]{"text","not null"}))
                .addColumn(new Column("PRODUCT_Name",new String[]{"text","not null"}))
                .addColumn(new Column("PRODUCT_Rate",new String[]{"text","not null"}))
                .addColumn(new Column("PRODUCT_TotalPrice",new String[]{"text","not null"}))
                .addColumn(new Column("PRODUCT_quantity",new String[]{"text","not null"}))
                .doneTableColumn();

        Cursor res=easyDB.getAllData();
        while(res.moveToNext()) {
            String id = res.getString(1);
            String pId = res.getString(2);
            String name = res.getString(3);
            String rate = res.getString(4);
            String totalCost = res.getString(5);
            String quantity = res.getString(6);

            allTotalPrice=allTotalPrice+Double.parseDouble(totalCost);

            modelOrders modelOrders=new modelOrders(""+id,""+pId,""+name,""+rate,""+totalCost,""+quantity);

            orderItemList.add(modelOrders);

            AlertDialog dialog=builder.create();
            dialog.show();

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    allTotalPrice=0.00;
                }
            });

            btn_checkout.setVisibility(View.VISIBLE);

            btn_checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(orderItemList.size()==0){
                        Toast.makeText(petrolActivity.this, "No items in Cart", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    submitOrder();
                }
            });

        }

    }
//
    private void submitOrder() {

        progressDialog.setMessage("Placing Order");
        progressDialog.show();

        String timeStamp=""+System.currentTimeMillis();

        String totalPrice=tv_amtPrice.getText().toString().trim().replace("$","");

        String lat=String.valueOf(latitude);
        String lon=String.valueOf(longitude);

        HashMap<String,String > hashMap=new HashMap<>();
        hashMap.put("orderID",timeStamp);
        hashMap.put("orderTime",timeStamp);
        hashMap.put("orderStatus","In Progress");
        hashMap.put("orderTitle","Petrol");
        hashMap.put("orderCost",totalPrice);
        hashMap.put("orderBy",firebaseAuth.getUid());
        hashMap.put("orderTo","Agent01");
        hashMap.put("latitude",lat);
        hashMap.put("longitude",lon);

        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Customer").child(firebaseAuth.getUid()).child("Orders");
        reference.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        for(int i=0;i<orderItemList.size();i++){
                            String pId=orderItemList.get(i).getPid();
                            String name=orderItemList.get(i).getName();
                            String price=orderItemList.get(i).getRate();
                            String cost=orderItemList.get(i).getTotalCost();
                            String quantity=orderItemList.get(i).getQuantity();

                            HashMap<String,String > hashMap1=new HashMap<>();
                            hashMap1.put("pId",pId);
                            hashMap1.put("name",name);
                            hashMap1.put("price",price);
                            hashMap1.put("cost",cost);
                            hashMap1.put("quantity",quantity);

                            reference.child(timeStamp).child("items").child(pId).setValue(hashMap1);
                        }
                        progressDialog.dismiss();
                        Toast.makeText(petrolActivity.this, "Order Placed....", Toast.LENGTH_SHORT).show();

//                        Intent intent=new Intent(petrolActivity.this,OrderDetailsActivity.class);
//                        intent.putExtra("orderTo",shopuid);
//                        intent.putExtra("orderID",timeStamp);
//                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(petrolActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

            edt_address.setText(address);
        }
        catch(Exception e){
            Toast.makeText(this, "Error"+e.getMessage(), Toast.LENGTH_LONG).show();
        }

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
                    supportMapFragment.getMapAsync(petrolActivity.this);
                }
            }
        });

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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng latLng=new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions().position(latLng).title("Here");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        googleMap.addMarker(markerOptions);

    }
}