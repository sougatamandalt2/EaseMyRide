package com.example.easemyride;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputLayout;

public class mechanicActivity extends AppCompatActivity {

    private CardView cv_tyre,cv_start;
    String issue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic);

        cv_tyre=findViewById(R.id.cv_tyre);
        cv_start=findViewById(R.id.cv_start);

        cv_tyre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issue="0";
                Intent i = new Intent(mechanicActivity.this,mapActivity.class);
                i.putExtra("issue",issue);
                startActivity(i);
            }
        });

        cv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issue="1";
                Intent i = new Intent(mechanicActivity.this,mapActivity.class);
                i.putExtra("issue",issue);
                startActivity(i);
            }
        });

    }
}