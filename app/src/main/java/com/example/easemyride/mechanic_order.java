package com.example.easemyride;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class mechanic_order extends AppCompatActivity {

    private AutoCompleteTextView dropdown_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_order);

        String[] items={"2 Wheeler / Bike","3 Wheeler / Auto","4 Wheeler / Car","Bus / Truck"};
        ArrayAdapter<String> itemadapter=new ArrayAdapter<>(mechanic_order.this,R.layout.list_item,items);
        dropdown_menu.setAdapter((itemadapter));

        dropdown_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}