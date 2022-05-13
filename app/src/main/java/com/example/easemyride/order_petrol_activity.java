package com.example.easemyride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class order_petrol_activity extends AppCompatActivity {

    private TextInputLayout textField,menu;
    private TextInputEditText textstyle;
    private AutoCompleteTextView dropdown_menu;
    private TextView petrol_price,delivery_price,total_price;
    private LinearLayout ll_orderDetails;
    private ImageView iv_back;
    private Button btn_submit;

    public Double allTotalPrice=0.00;

    Location currentLoc;
    FusedLocationProviderClient fusedLocationProviderClient;

    private static final int LOCATION_REQUEST_CODE = 100;

    private String[] locationPermissions;

    private String latitude, longitude;

    private LocationManager locationManager;

    private ArrayList<modelOrders> orderItemList;

    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_petrol);

        textField=findViewById(R.id.textField);
        menu=findViewById(R.id.menu);
        textstyle=findViewById(R.id.textstyle);
        dropdown_menu=findViewById(R.id.dropdown_menu);
        petrol_price=findViewById(R.id.petrol_price);
        iv_back=findViewById(R.id.iv_back);
        delivery_price=findViewById(R.id.delivery_price);
        total_price=findViewById(R.id.total_price);
        btn_submit=findViewById(R.id.btn_submit);
        ll_orderDetails=findViewById(R.id.ll_orderDetails);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("please wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        ll_orderDetails.setVisibility(View.GONE);

        btn_submit.setEnabled(false);

        Intent intent=getIntent();
        latitude=intent.getStringExtra("lat");
        longitude=intent.getStringExtra("long");

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        String[] items={"150","300","450","950","1450"};
        ArrayAdapter<String> itemadapter=new ArrayAdapter<>(order_petrol_activity.this,R.layout.list_item,items);
        dropdown_menu.setAdapter((itemadapter));
        dropdown_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                btn_submit.setEnabled(true);
                ll_orderDetails.setVisibility(View.VISIBLE);
                String petrol_pr=items[i];
                Integer tot_pr=Integer.parseInt(petrol_pr)+50;
                petrol_price.setText(petrol_pr);
                delivery_price.setText("50");
                total_price.setText(String.valueOf(tot_pr));
            }
        });
        String reg_no=textField.toString().trim();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(reg_no)){
                    Toast.makeText(order_petrol_activity.this, "Enter a valid Registration Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(petrol_price.getText().toString().trim().isEmpty()){
                    Toast.makeText(order_petrol_activity.this, "Select a valid order", Toast.LENGTH_SHORT).show();
                    return;
                }
                String title="Fuel:Petrol";
                String finalPrice=total_price.getText().toString().trim().replace("$","");
                String delPrice=delivery_price.getText().toString().trim().replace("$","");
                String petrolPrice=petrol_price.getText().toString().trim().replace("$","");
                String regNo=textstyle.getText().toString().trim().replace("$","");
                addToCart(title,petrolPrice,delPrice,finalPrice,regNo);

                new AlertDialog.Builder(order_petrol_activity.this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Placing Order").setMessage("Are you sure you want to place the order?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                makeOrder(view);

                                submitOrder();
                            }
                        }).setNegativeButton("No", null).show();
            }
        });

    }

    private int orderID=0;
    private void addToCart(String title, String petrolPrice, String delPrice, String finalPrice,String regNo) {

        EasyDB easyDB=EasyDB.init(order_petrol_activity.this,"ITEMS_DB")
                .addColumn(new Column("ORDER_ID",new String[]{"text","unique"}))
                .addColumn(new Column("ITEM_PID",new String[]{"text","not null"}))
                .addColumn(new Column("ORDER_Name",new String[]{"text","not null"}))
                .addColumn(new Column("ORDER_petrolPrice",new String[]{"text","not null"}))
                .addColumn(new Column("ORDER_regNo",new String[]{"text","not null"}))
                .addColumn(new Column("Order_totalPrice",new String[]{"text","not null"}))
                .addColumn(new Column("ORDER_delPrice",new String[]{"text","not null"}))
                .doneTableColumn();

        orderID++;
        boolean b=easyDB.addData("ORDER_ID",orderID)
                .addData("ORDER_Name",title)
                .addData("ORDER_petrolPrice",petrolPrice)
                .addData("ORDER_delPrice",delPrice)
                .addData("Order_totalPrice",finalPrice)
                .addData("ORDER_regNo",regNo)
                .doneDataAdding();

    }

    private void makeOrder(View view) {
        orderItemList=new ArrayList<>();

        AlertDialog.Builder builder=new AlertDialog.Builder(order_petrol_activity.this);
        builder.setView(view);

        EasyDB easyDB=EasyDB.init(order_petrol_activity.this,"ORDERS_DB")
                .addColumn(new Column("ORDER_ID",new String[]{"text","unique"}))
                .addColumn(new Column("PRODUCT_PID",new String[]{"text","not null"}))
                .addColumn(new Column("PRODUCT_Name",new String[]{"text","not null"}))
                .addColumn(new Column("PRODUCT_regNo",new String[]{"text","not null"}))
                .addColumn(new Column("PRODUCT_TotalPrice",new String[]{"text","not null"}))
                .doneTableColumn();

        Cursor res=easyDB.getAllData();
        while(res.moveToNext()) {
            String id = res.getString(1);
            String pId = res.getString(2);
            String name = res.getString(3);
            String regNo = res.getString(4);
            String totalCost = res.getString(5);

            allTotalPrice=allTotalPrice+Double.parseDouble(totalCost);

            modelOrders modelOrders=new modelOrders(""+id,""+pId,""+name,""+regNo,""+totalCost);

            orderItemList.add(modelOrders);

            AlertDialog dialog=builder.create();
            dialog.show();

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    allTotalPrice=0.00;
                }
            });

            if(orderItemList.size()==0){
                Toast.makeText(order_petrol_activity.this, "No items in Cart", Toast.LENGTH_SHORT).show();
                return;
            }
            submitOrder();

        }

    }
    //
    private void submitOrder() {

        progressDialog.setMessage("Placing Order");
        progressDialog.show();

        String timeStamp=""+System.currentTimeMillis();

        String totalPrice=total_price.getText().toString().trim().replace("$","");

        String lat=String.valueOf(latitude);
        String lon=String.valueOf(longitude);

        HashMap<String,String > hashMap=new HashMap<>();
        hashMap.put("orderID",timeStamp);
        hashMap.put("orderTime",timeStamp);
        hashMap.put("orderStatus","In Progress");
        hashMap.put("orderTitle","Petrol");
        hashMap.put("orderCost",totalPrice);
        hashMap.put("orderBy",firebaseAuth.getUid());
        hashMap.put("orderTo","Agent01");
        hashMap.put("latitude",lat);
        hashMap.put("longitude",lon);

        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Customer").child(firebaseAuth.getUid()).child("Orders");
        reference.child(timeStamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        for(int i=0;i<orderItemList.size();i++){
                            String pId=orderItemList.get(i).getPid();
                            String name=orderItemList.get(i).getName();
                            String regNo=orderItemList.get(i).getRegNo();
                            String cost=orderItemList.get(i).getTotalCost();

                            HashMap<String,String > hashMap1=new HashMap<>();
                            hashMap1.put("pId",pId);
                            hashMap1.put("name",name);
                            hashMap1.put("regNo",regNo);
                            hashMap1.put("cost",cost);

                            reference.child(timeStamp).child("items").child(pId).setValue(hashMap1);
                        }
                        progressDialog.dismiss();
                        Toast.makeText(order_petrol_activity.this, "Order Placed....", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(order_petrol_activity.this, OrdersListActivity.class));

//                        Intent intent=new Intent(petrolActivity.this,OrderDetailsActivity.class);
//                        intent.putExtra("orderTo",shopuid);
//                        intent.putExtra("orderID",timeStamp);
//                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(order_petrol_activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

}