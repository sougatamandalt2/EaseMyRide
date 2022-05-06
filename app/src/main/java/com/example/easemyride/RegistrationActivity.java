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
        edt_otp=findViewById((R.id.edt_otp));
        btn_genOtp=findViewById((R.id.btn_genOtp));

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        name=edt_name.getText().toString().trim();
        email=edt_email.getText().toString().trim();
        password=edt_password.getText().toString().trim();
        confpassword=edt_confPassword.getText().toString().trim();
        phone=edt_phone.getText().toString().trim();

        btn_genOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+91" + edt_phone.getText().toString();
                sendVerificationCode(phone);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

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

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Toast.makeText(RegistrationActivity.this, "Account Created", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegistrationActivity.this, "Deletion Success", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                });
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        // this method is used for getting
        // OTP on user phone number.
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(number)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback method is called on Phone auth provider.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // initializing our callbacks for on
            // verification callback method.
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // below method is used when
        // OTP is sent from Firebase
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            // when we receive the OTP it
            // contains a unique id which
            // we are storing in our string
            // which we have already created.
            verificationId = s;
        }

        // this method is called when user
        // receive OTP from Firebase.
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                edt_otp.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // this method is called when firebase doesn't
        // sends our OTP code due to any error or issue.
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(RegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        Toast.makeText(this, ""+String.valueOf(credential), Toast.LENGTH_SHORT).show();

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

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