package com.example.agentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AgentOrderDetailsActivity extends AppCompatActivity {

    private String orderTo,orderID,orderDate;

    String ob;

    private ImageView btn_back,iv_order;
    private TextView tv_orderId,tv_date,tv_orderStatus, tv_amount,tv_address,tv_orderTitle;
    private Button btn_accept,btn_decline;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_order_details);

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
        btn_accept=findViewById(R.id.btn_accept);
        btn_decline=findViewById(R.id.btn_decline);

        firebaseAuth=FirebaseAuth.getInstance();

        loadOrderDetails();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currtimeStamp=""+System.currentTimeMillis();

                HashMap<String,String > hashMap=new HashMap<>();
                hashMap.put("orderID",tv_orderId.getText().toString().trim());
                hashMap.put("acceptTime",currtimeStamp);
                hashMap.put("orderStatus",tv_orderStatus.getText().toString().trim());
                hashMap.put("orderTitle",tv_orderTitle.getText().toString().trim());
                hashMap.put("orderCost",tv_amount.getText().toString().trim());
                hashMap.put("orderBy",ob);
                hashMap.put("orderTo",firebaseAuth.getCurrentUser().getUid());
                hashMap.put("orderAddress",tv_address.getText().toString().trim());
                DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Agent").child(firebaseAuth.getCurrentUser().getUid()).child("Accepted Orders");

                reference.child(currtimeStamp).setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AgentOrderDetailsActivity.this, "Order accepted successfully.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AgentOrderDetailsActivity.this, "Order failed to accept.", Toast.LENGTH_SHORT).show();
                            }
                        });
                DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("Customer").child(ob).child("Orders").child(orderID);
                HashMap<String,String > hashMap1=new HashMap<>();
                hashMap1.put("orderAccepted","Yes");
                reference1.child("OrderAccepted").setValue(hashMap1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AgentOrderDetailsActivity.this, "Order is accepted.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AgentOrderDetailsActivity.this, "Order failed to accept.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void loadOrderDetails() {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("All Orders");
        reference.child("Orders").child(orderID)
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

                        ob=orderBy;

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
                        else if(orderTitle.equals("JumpStart")){
                            iv_order.setImageResource(R.drawable.mechanic);
                        }
                        else if(orderTitle.equals("Tyre Puncture")){
                            iv_order.setImageResource(R.drawable.tyre);
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