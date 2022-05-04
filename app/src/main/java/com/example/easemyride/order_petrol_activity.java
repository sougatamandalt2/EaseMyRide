package com.example.easemyride;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class order_petrol_activity extends AppCompatActivity {

    private TextInputLayout textField,menu;
    private TextInputEditText textstyle;
    private AutoCompleteTextView dropdown_menu;
    private TextView petrol_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_petrol);

        textField=findViewById(R.id.textField);
        menu=findViewById(R.id.menu);
        textstyle=findViewById(R.id.textstyle);
        dropdown_menu=findViewById(R.id.dropdown_menu);
        petrol_price=findViewById(R.id.petrol_price);

        String[] items={"150","300","450","950","1450"};
        ArrayAdapter<String> itemadapter=new ArrayAdapter<>(order_petrol_activity.this,R.layout.list_item,items);
        dropdown_menu.setAdapter((itemadapter));
        dropdown_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                petrol_price.setText("250");
            }
        });

    }


}