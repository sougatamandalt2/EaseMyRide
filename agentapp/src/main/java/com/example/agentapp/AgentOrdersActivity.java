package com.example.agentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AgentOrdersActivity extends AppCompatActivity {

    private RecyclerView rv_orders;
    private ImageView iv_back;
    private ArrayList<modelOrderAdv> orderList;

    private AdapterOrderAdv adapterOrderAdv;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_orders);

        rv_orders=findViewById(R.id.rv_orders);
        iv_back=findViewById(R.id.iv_back);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);

        loadOrders();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AgentOrdersActivity.this,AgentHomepageActivity.class));
            }
        });
    }

    private void loadOrders() {

        orderList=new ArrayList<>();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("All Orders");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    String uid=""+ds.getRef().getKey();

                    DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("All Orders").child("Orders");
                    reference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        for(DataSnapshot ds:snapshot.getChildren()){
                                            modelOrderAdv modelOrderAdv=ds.getValue(modelOrderAdv.class);

                                            orderList.add(modelOrderAdv);
                                        }
                                        adapterOrderAdv= new AdapterOrderAdv(AgentOrdersActivity.this,orderList);
                                        rv_orders.setAdapter(adapterOrderAdv);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}