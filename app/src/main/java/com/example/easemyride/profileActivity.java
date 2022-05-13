package com.example.easemyride;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileActivity extends AppCompatActivity {

    private ImageView iv_logout,iv_edit,iv_back;
    private TextView tv_orders,tv_username,tv_useremail,tv_userphone,tv_settings,tv_contacts,tv_aboutUs;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        iv_logout=findViewById(R.id.iv_logout);
        tv_orders=findViewById(R.id.tv_orders);
        tv_username=findViewById(R.id.tv_username);
        tv_useremail=findViewById(R.id.tv_useremail);
        tv_userphone=findViewById(R.id.tv_userphone);
        iv_edit=findViewById(R.id.iv_edit);
        iv_back=findViewById(R.id.iv_back);
        tv_settings=findViewById(R.id.tv_settings);
        tv_contacts=findViewById(R.id.tv_contacts);
        tv_aboutUs=findViewById(R.id.tv_aboutUs);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        String useruid=firebaseAuth.getCurrentUser().getUid();

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Customer");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren()){
                            String name=""+ds.child("name").getValue();
                            String email=""+ds.child("email").getValue();
                            String phone=""+ds.child("phoneNumber").getValue();

                            tv_username.setText(name);
                            tv_useremail.setText(email);
                            tv_userphone.setText(phone);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Logging Out....");

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Customer");

                firebaseAuth.signOut();
                startActivity(new Intent(profileActivity.this,LoginActivity.class));
            }
        });

        tv_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Showing your orders....");
                startActivity(new Intent(profileActivity.this, OrdersListActivity.class));
            }
        });


        tv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profileActivity.this,SettingsActivity.class));
            }
        });
    }
}