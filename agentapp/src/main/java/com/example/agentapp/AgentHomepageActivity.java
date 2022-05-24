package com.example.agentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AgentHomepageActivity extends AppCompatActivity {

    private Button btn_go,btn_orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_homepage);

        btn_go=findViewById(R.id.btn_go);
        btn_orders=findViewById(R.id.btn_orders);

        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AgentHomepageActivity.this,AgentMapActivity.class));
            }
        });

        btn_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AgentHomepageActivity.this,AgentOrdersActivity.class));
            }
        });

    }
}