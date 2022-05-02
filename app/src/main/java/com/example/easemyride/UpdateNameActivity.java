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

public class UpdateNameActivity extends AppCompatActivity {
    private Button button_name_update;
    private EditText edt_update_name;
    private ImageView iv_back;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);

        button_name_update=findViewById(R.id.button_name_update);
        edt_update_name=findViewById(R.id.edt_update_name);
        iv_back=findViewById(R.id.iv_back);

        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        button_name_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeData();
            }
        });
    }

    private String username,userphone;
    private void takeData() {

        username=button_name_update.getText().toString().trim();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(UpdateNameActivity.this, "Enter a name....", Toast.LENGTH_SHORT).show();
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


        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Customer");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateNameActivity.this, "Profile Updated....", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateNameActivity.this, "Cannot Update....", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}