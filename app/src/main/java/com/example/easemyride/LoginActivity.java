package com.example.easemyride;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private RelativeLayout rl_login;
    private ImageView iv_logo,iv_gimg,iv_fimg;
    private TextView tv_label,tv_loginby,tv_newuser,tv_forgetpassword;
    private EditText edt_email,edt_password;
    private Button btn_login;
//    private LoginButton login_button;
//    private CallbackManager callbackManager;

//    private GoogleSignInClient mGoogleSignInClient;
//    private static int RC_SIGN_IN=100;

    private static final String EMAIL = "email";

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        rl_login=findViewById(R.id.rl_login);
        iv_logo=findViewById(R.id.iv_logo);
        tv_label=findViewById(R.id.tv_label);
        edt_email=findViewById(R.id.edt_email);
        edt_password=findViewById(R.id.edt_password);
        btn_login=findViewById(R.id.btn_login);


        tv_newuser=findViewById(R.id.tv_newuser);
        tv_forgetpassword=findViewById(R.id.tv_forgetpassword);


        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Logging in....");
        progressDialog.setCanceledOnTouchOutside(false);

        SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
        String ue=preferences.getString("userEmail","");
        String up=preferences.getString("userPassword","");
        tv_forgetpassword.setPaintFlags(tv_forgetpassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if(!TextUtils.isEmpty(ue) && !TextUtils.isEmpty(up)){
            edt_email.setText(ue);
            edt_password.setText(up);
        }

//        String name=givename();

        tv_newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
            }
        });

        tv_forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String e=edt_email.getText().toString().trim();
                String p=edt_password.getText().toString().trim();

                SharedPreferences preferences1=getSharedPreferences("checkbox",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences1.edit();
                editor.putString("userEmail",e);
                editor.putString("userPassword",p);
                editor.apply();

                loginUserAccount();

            }
        });

    }

//    private String givename() {
//        String a="";
//        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Customer");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String name=snapshot.child("name");
//                name="";
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private FirebaseDatabase mRef;
    String email,password;
    private void loginUserAccount() {

        SharedPreferences preferences=getSharedPreferences("checkbox",MODE_PRIVATE);
        String ue=preferences.getString("userEmail","");
        String up=preferences.getString("userPassword","");

        if(!TextUtils.isEmpty(ue) && !TextUtils.isEmpty(up)){
            edt_email.setText(ue);
            edt_password.setText(up);
            email=ue;
            password=up;
        }
        else{
            email=edt_email.getText().toString().trim();
            password=edt_password.getText().toString().trim();
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(LoginActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.length()<8){
            Toast.makeText(LoginActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();

                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Customer");
                        reference.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    startActivity(new Intent(LoginActivity.this,HomepageActivity.class));
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "Incorrect email or password, try again", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Incorrect email or password, try again", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}