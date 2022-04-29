package com.example.easemyride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView iv_back,iv_logout;
    private TextView tv_name;
    private EditText edt_editName,edt_editPhone;
    private Button btn_edit;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        iv_back=findViewById(R.id.iv_back);
        tv_name=findViewById(R.id.tv_name);
        iv_logout=findViewById(R.id.iv_logout);
        edt_editName=findViewById(R.id.edt_editName);
        edt_editPhone=findViewById(R.id.edt_editPhone);
        btn_edit=findViewById(R.id.btn_edit);

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

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Logging Out....");

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Customers");

                firebaseAuth.signOut();
                startActivity(new Intent(EditProfileActivity.this,LoginActivity.class));
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeData();
            }
        });
    }

    private String username,userphone;
    private void takeData() {

        username=edt_editName.getText().toString().trim();
        userphone=edt_editPhone.getText().toString().trim();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(EditProfileActivity.this, "Enter a name....", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userphone)){
            Toast.makeText(EditProfileActivity.this, "Enter a phone number....", Toast.LENGTH_SHORT).show();
            return;
        }
        if(userphone.length()!=10){
            Toast.makeText(EditProfileActivity.this, "Enter a valid phone number....", Toast.LENGTH_SHORT).show();
            return;
        }
        submitData();

    }

    private void submitData() {

        progressDialog.setMessage("Updating account...");
        progressDialog.show();

        HashMap<String,Object> hashMap=new HashMap<>();

        hashMap.put("uid",""+firebaseAuth.getUid());
        hashMap.put("name",""+username);
        hashMap.put("phoneNumber",""+userphone);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Customer");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Profile Updated....", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(EditProfileActivity.this, "Cannot Update....", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}