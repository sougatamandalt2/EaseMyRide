package com.example.easemyride;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomepageActivity extends AppCompatActivity {

    private RelativeLayout rl_heading,rl_main;
    private ImageView iv_back,iv_profile,iv_fuel,iv_mechanic,iv_towtruck,iv_medical;
    private TextView tv_name,tv_help,tv_fuel,tv_mechanic,tv_towtruck,tv_medical;
    private LinearLayout ll_options,ll_options2;
    private CardView cv_fuel,cv_mechanic,cv_tow_truck,cv_medical;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ImageView iv_cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        rl_heading=findViewById(R.id.rl_heading);
        iv_back=findViewById(R.id.iv_back);
        tv_name=findViewById(R.id.tv_name);
        iv_profile=findViewById(R.id.iv_profile);
        rl_main=findViewById(R.id.rl_main);
        tv_help=findViewById(R.id.tv_help);
        ll_options=findViewById(R.id.ll_options);
        cv_fuel=findViewById(R.id.cv_fuel);
        iv_fuel=findViewById(R.id.iv_fuel);
        tv_fuel=findViewById(R.id.tv_fuel);
        cv_mechanic=findViewById(R.id.cv_mechanic);
        iv_mechanic=findViewById(R.id.iv_mechanic);
        tv_mechanic=findViewById(R.id.tv_mechanic);
        ll_options2=findViewById(R.id.ll_options2);
        cv_tow_truck=findViewById(R.id.cv_tow_truck);
        iv_towtruck=findViewById(R.id.iv_towtruck);
        tv_towtruck=findViewById(R.id.tv_towtruck);
        cv_medical=findViewById(R.id.cv_medical);
        iv_medical=findViewById(R.id.iv_medical);
        tv_medical=findViewById(R.id.tv_medical);
        iv_cart=findViewById(R.id.iv_cart);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        checkuser();

//        Bundle bundle=getIntent().getExtras();
//        String name=bundle.getString("name");

//        tv_name.setText("Hi, "+name);

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Customers");

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this,profileActivity.class));
            }
        });

        cv_tow_truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this,tow_truckActivity.class));
            }
        });

        cv_medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this,FirstaidActivity.class));
            }
        });

        iv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(HomepageActivity.this, OrdersListActivity.class));
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cv_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this,fuelActivity.class));
            }
        });

        cv_mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomepageActivity.this,mechanicActivity.class));
            }
        });

    }
    private void checkuser() {

        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(HomepageActivity.this,LoginActivity.class));
            finish();
        }
        else{
            loadInfo();
        }

    }

    private void loadInfo() {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Customer");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren()){
                            String name=""+ds.child("name").getValue();

                            tv_name.setText(name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}