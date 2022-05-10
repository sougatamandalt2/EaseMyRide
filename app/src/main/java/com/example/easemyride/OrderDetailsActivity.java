package com.example.easemyride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {

    private String orderTo,orderID,orderDate;

    private ImageView btn_back,iv_order;
    private TextView tv_orderId,tv_date,tv_orderStatus, tv_amount,tv_address,tv_orderTitle;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Intent intent=getIntent();
        orderID=intent.getStringExtra("orderID");
        orderTo=intent.getStringExtra("orderTo");
        orderDate=intent.getStringExtra("orderDate");

        btn_back=findViewById(R.id.btn_back);
        tv_orderId=findViewById(R.id.tv_orderId);
        tv_date=findViewById(R.id.tv_date);
        tv_orderStatus=findViewById(R.id.tv_orderStatus);
        tv_amount=findViewById(R.id.tv_amount);
        tv_address=findViewById(R.id.tv_address);
        tv_orderTitle=findViewById(R.id.tv_orderTitle);
        iv_order=findViewById(R.id.iv_order);

        firebaseAuth=FirebaseAuth.getInstance();

        loadOrderDetails();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void loadOrderDetails() {

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Customer");
        reference.child(orderTo).child("Orders").child(orderID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String orderBy=""+snapshot.child("orderBy").getValue();
                        String orderCost=""+snapshot.child("orderCost").getValue();
                        String orderID=""+snapshot.child("orderID").getValue();
                        String orderStatus=""+snapshot.child("orderStatus").getValue();
                        String orderTo=""+snapshot.child("orderTo").getValue();
                        String orderTitle=""+snapshot.child("orderTitle").getValue();
                        String latitude=""+snapshot.child("latitude").getValue();
                        String longitude=""+snapshot.child("longitude").getValue();

                        if(orderStatus.equals("In Progress")){
                            tv_orderStatus.setTextColor(getResources().getColor(R.color.teal_200));
                        }
                        else if(orderStatus.equals("Completed")){
                            tv_orderStatus.setTextColor(getResources().getColor(R.color.green));
                        }
                        else if(orderStatus.equals("Cancelled")){
                            tv_orderStatus.setTextColor(getResources().getColor(R.color.red));
                        }

                        tv_orderId.setText(orderID);
                        tv_date.setText(orderDate);
                        tv_amount.setText(orderCost);
                        tv_orderStatus.setText(orderStatus);
                        tv_orderTitle.setText(orderTitle);

                        if(orderTitle.equals("Petrol")){
                            iv_order.setImageResource(R.drawable.petrol);
                        }
                        else if(orderTitle.equals("diesel")){
                            iv_order.setImageResource(R.drawable.diesel);
                        }
                        else if(orderTitle.equals("Mechanic")){
                            iv_order.setImageResource(R.drawable.mechanic);
                        }
                        else if(orderTitle.equals("CNG")){
                            iv_order.setImageResource(R.drawable.cng);
                        }

                        findAddress(latitude,longitude);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void findAddress(String latitude, String longitude) {
        double lat=Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);

        Geocoder geocoder;
        List<Address> addresses;

        geocoder=new Geocoder(this, Locale.getDefault());

        try{
            addresses=geocoder.getFromLocation(lat,lon,1);

            String address=addresses.get(0).getAddressLine(0);
            tv_address.setText(address);
        }
        catch (Exception e){

        }
    }

}