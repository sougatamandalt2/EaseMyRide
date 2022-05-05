package com.example.easemyride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UpdateMobileNoActivity extends AppCompatActivity {

    private Button button_update;
    private EditText edt_update_mob;
    private ImageView iv_back;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_mobile_no);


        button_update=findViewById(R.id.button_update);
        edt_update_mob=findViewById(R.id.edt_update_mob);
        iv_back=findViewById(R.id.iv_back);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeData();
            }
        });
    }

    private String userphone;
    private void takeData() {

        userphone=edt_update_mob.getText().toString().trim();

        if(userphone.length()!=10){
            Toast.makeText(UpdateMobileNoActivity.this, "Enter a valid phone number....", Toast.LENGTH_SHORT).show();
            return;
        }
        submitData();

    }

    private void submitData() {

        progressDialog.setMessage("Updating account...");
        progressDialog.show();

        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("uid",""+firebaseAuth.getUid());
        hashMap.put("phoneNumber",""+userphone);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Customer");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateMobileNoActivity.this, "Profile Updated....", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateMobileNoActivity.this, "Cannot Update....", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}