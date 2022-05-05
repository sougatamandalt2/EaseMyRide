package com.example.easemyride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MobileVerificationActivity extends AppCompatActivity {

    private EditText edt_otp;
    private Button btn_submitOTP;

    String otpCode;
    String name,email,password,confpassword,phone;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);

        edt_otp=findViewById(R.id.edt_otp);
        btn_submitOTP=findViewById(R.id.btn_submitOTP);

        otpCode=getIntent().getStringExtra("otp");

        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        password=getIntent().getStringExtra("password");
        confpassword=getIntent().getStringExtra("confpassword");
        phone=getIntent().getStringExtra("number");

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        btn_submitOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userOTP=edt_otp.getText().toString().trim();

                if(userOTP.equals(otpCode)){
                    inputData();
                }
                else{
                    Intent intent=new Intent(MobileVerificationActivity.this,RegistrationActivity.class);
                    intent.putExtra("verified","0");
                    startActivity(intent);
                }

                PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(otpCode,userOTP);
            }
        });
    }

    private void inputData() {

        if(TextUtils.isEmpty(name)){
            Toast.makeText(MobileVerificationActivity.this, "Name field is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(MobileVerificationActivity.this, "Phone number field is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(MobileVerificationActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<8){
            Toast.makeText(MobileVerificationActivity.this, "Password should contain at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!confpassword.equals(password)){
            Toast.makeText(MobileVerificationActivity.this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }

        createAccount();
    }

    private void createAccount() {

        progressDialog.setMessage("Creating account....");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        saveFirebaseData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MobileVerificationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void saveFirebaseData() {

        progressDialog.setMessage("Saving info....");
        String timeStamp=""+System.currentTimeMillis();

        HashMap<String,Object> hashmap=new HashMap<>();

        hashmap.put("uid",firebaseAuth.getUid());
        hashmap.put("name",""+name);
        hashmap.put("email",""+email);
        hashmap.put("phoneNumber",""+phone);
        hashmap.put("timeStamp",""+timeStamp);
        hashmap.put("accountType","user");

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Customer");
        reference.child(firebaseAuth.getUid()).setValue(hashmap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Intent i=new Intent(MobileVerificationActivity.this,HomepageActivity.class);
                        i.putExtra("name",name);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MobileVerificationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}