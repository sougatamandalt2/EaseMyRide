package com.example.easemyride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class MobileVerificationActivity extends AppCompatActivity {

    private EditText edt_otp,edt_phone,edt_1,edt_2,edt_3,edt_4,edt_5,edt_6;
    private Button btn_submitOTP,btn_generate;
    private TextView tv_resend;
    private LinearLayout ll_1;

    String otpCode;
    String name,email,password,confpassword,phone;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_verification);

        btn_submitOTP=findViewById(R.id.btn_submitOTP);
        edt_phone=findViewById(R.id.edt_phone);
        btn_generate=findViewById(R.id.btn_generate);
        edt_1=findViewById(R.id.edt_1);
        edt_2=findViewById(R.id.edt_2);
        edt_3=findViewById(R.id.edt_3);
        edt_4=findViewById(R.id.edt_4);
        edt_5=findViewById(R.id.edt_5);
        edt_6=findViewById(R.id.edt_6);
        tv_resend=findViewById(R.id.tv_resend);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        btn_submitOTP.setVisibility(View.GONE);

        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+91" + edt_phone.getText().toString();
                sendVerificationCode(phone);
//                ll_1.setVisibility(View.VISIBLE);
            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+91" + edt_phone.getText().toString();
                sendVerificationCode(phone);

                btn_submitOTP.setVisibility(View.VISIBLE);
                btn_generate.setVisibility(View.GONE);
            }
        });

        btn_submitOTP.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                if(!edt_1.getText().toString().trim().isEmpty() && !edt_2.getText().toString().trim().isEmpty() &&
                        !edt_3.getText().toString().trim().isEmpty() && !edt_4.getText().toString().trim().isEmpty() &&
                        !edt_5.getText().toString().trim().isEmpty() && !edt_6.getText().toString().trim().isEmpty() ) {

                    String myOTP = edt_1.getText().toString().trim() +
                            edt_2.getText().toString().trim() +
                            edt_3.getText().toString().trim() +
                            edt_4.getText().toString().trim() +
                            edt_5.getText().toString().trim() +
                            edt_6.getText().toString().trim();

                    verifyCode(myOTP);

                }
                else{
                    Toast.makeText(MobileVerificationActivity.this, "Enter all the numbers", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        moveEdt();

    }

    private void moveEdt() {

        edt_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    edt_2.requestFocus();
                    edt_2.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    edt_3.requestFocus();
                    edt_3.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    edt_4.requestFocus();
                    edt_4.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    edt_5.requestFocus();
                    edt_5.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    edt_6.requestFocus();
                    edt_6.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                            Toast.makeText(MobileVerificationActivity.this, "", Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(MobileVerificationActivity.this, "", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            inputData();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(MobileVerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
            btn_generate.setVisibility(View.GONE);
            btn_submitOTP.setVisibility(View.VISIBLE);
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
            Toast.makeText(MobileVerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
    }

    private void inputData() {

        name=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        password=getIntent().getStringExtra("password");
        confpassword=getIntent().getStringExtra("confpassword");
        phone=edt_phone.getText().toString().trim();

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