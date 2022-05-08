package com.example.easemyride;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class fuelActivity extends AppCompatActivity {
    private TextView tv_petrol,tv_diesel;
    private CardView cv_petrol,cv_diesel;
    private ImageView iv_petrol,iv_diesel,iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel);
        tv_petrol=findViewById(R.id.tv_petrol);
        tv_diesel=findViewById(R.id.tv_diesel);

        cv_petrol=findViewById(R.id.cv_petrol);
        cv_diesel=findViewById(R.id.cv_diesel);

        iv_petrol=findViewById(R.id.iv_petrol);
        iv_diesel=findViewById(R.id.iv_diesel);

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
                startActivity(new Intent(fuelActivity.this, map_petrol_Activity.class));
            }
        });

        cv_diesel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(fuelActivity.this, map_diesel_activity.class));
            }
        });

    }
}