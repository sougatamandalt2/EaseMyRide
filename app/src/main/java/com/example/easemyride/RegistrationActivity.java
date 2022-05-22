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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {
    private RelativeLayout rl_login;
    private ImageView iv_logo,iv_gimg,iv_fimg;
    private TextView tv_label,tv_loginby,tv_existUser;
    private EditText edt_name,edt_email,edt_password,edt_confPassword,edt_phone,edt_otp;
    private Button btn_register,btn_genOtp;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    String name,email,password,confpassword,phone;
    private String verificationId;
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

                name=edt_name.getText().toString().trim();
                email=edt_email.getText().toString().trim();
                password=edt_password.getText().toString().trim();
                confpassword=edt_confPassword.getText().toString().trim();

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

                Intent i=new Intent(RegistrationActivity.this,MobileVerificationActivity.class);
                i.putExtra("name",name);
                i.putExtra("email",email);
                i.putExtra("password",password);
                i.putExtra("confpassword",confpassword);
                i.putExtra("phone",phone);
                startActivity(i);

            }
        });

        tv_existUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
            }
        });
    }
}