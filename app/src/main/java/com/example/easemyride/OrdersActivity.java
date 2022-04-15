package com.example.easemyride;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView rv_orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        rv_orders=findViewById(R.id.rv_orders);


    }
}