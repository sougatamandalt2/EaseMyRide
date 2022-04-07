package com.example.easemyride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class fuelActivity extends AppCompatActivity {
    private TextView tv_petrol,tv_diesel,tv_cng;
    private CardView cv_petrol,cv_diesel,cv_cng;
    private ImageView iv_petrol,iv_diesel,iv_cng,iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel);
        tv_petrol=findViewById(R.id.tv_petrol);
        tv_diesel=findViewById(R.id.tv_diesel);
        tv_cng=findViewById(R.id.tv_cng);
        cv_petrol=findViewById(R.id.cv_petrol);
        cv_diesel=findViewById(R.id.cv_diesel);
        cv_cng=findViewById(R.id.cv_cng);
        iv_petrol=findViewById(R.id.iv_petrol);
        iv_diesel=findViewById(R.id.iv_diesel);
        iv_cng=findViewById(R.id.iv_cng);
        iv_back=findViewById(R.id.iv_back);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        cv_petrol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(fuelActivity.this,petrolActivity.class));
            }
        });

        cv_diesel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(fuelActivity.this,dieselActivity.class));
            }
        });

        cv_cng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(fuelActivity.this,cngActivity.class));
            }
        });
    }
}