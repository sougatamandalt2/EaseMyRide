package com.example.easemyride;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {
    private RelativeLayout rl_login;
    private ImageView iv_logo,iv_gimg,iv_fimg;
    private TextView tv_label,tv_loginby,tv_existUser;
    private EditText edt_name,edt_email,edt_password,edt_confPassword,edt_phone;
    private Button btn_register;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        rl_login=findViewById((R.id.rl_login));
        iv_logo=findViewById((R.id.iv_logo));
        tv_label=findViewById((R.id.tv_label));
        edt_name=findViewById((R.id.edt_name));
        edt_email=findViewById((R.id.edt_email));
        edt_password=findViewById((R.id.edt_password));
        edt_confPassword=findViewById((R.id.edt_confPassword));
        btn_register=findViewById((R.id.btn_register));

        tv_existUser=findViewById((R.id.tv_existUser));
        edt_phone=findViewById((R.id.edt_phone));

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });

        tv_existUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });
    }
    private String name,email,password,confpassword,phone;
    private void inputData() {
        name=edt_name.getText().toString().trim();
        email=edt_email.getText().toString().trim();
        password=edt_password.getText().toString().trim();
        confpassword=edt_confPassword.getText().toString().trim();
        phone=edt_phone.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(RegistrationActivity.this, "Name field is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(RegistrationActivity.this, "Phone number field is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(RegistrationActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<8){
            Toast.makeText(RegistrationActivity.this, "Password should contain at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!confpassword.equals(password)){
            Toast.makeText(RegistrationActivity.this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegistrationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        Intent i=new Intent(RegistrationActivity.this,HomepageActivity.class);
                        i.putExtra("name",name);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}