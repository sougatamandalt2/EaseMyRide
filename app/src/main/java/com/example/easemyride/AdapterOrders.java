package com.example.easemyride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

public class AdapterOrders extends RecyclerView.Adapter<AdapterOrders.Holder>{

    private Context context;
    private ArrayList<modelOrders> orders;

    public AdapterOrders(Context context, ArrayList<modelOrders> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_orders,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        modelOrders modelOrders=orders.get(position);

        String id=modelOrders.getId();
        String pid=modelOrders.getPid();
        String name=modelOrders.getName();
        String regNo=modelOrders.getRegNo();
        String totalCost=modelOrders.getTotalCost();

        holder.tv_title.setText(name);
        holder.tv_fuelPrice.setText(regNo);
        holder.tv_delPrice.setText("50");
        holder.tv_totalPrice.setText(totalCost);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class Holder extends RecyclerView.ViewHolder{

        private TextView tv_title,tv_fuelPrice,tv_delPrice,tv_totalPrice,
                tv_regNo;
        public Holder(@NonNull View itemView) {
            super(itemView);

            tv_title=itemView.findViewById(R.id.tv_title);
            tv_fuelPrice=itemView.findViewById(R.id.tv_fuelPrice);
            tv_delPrice=itemView.findViewById(R.id.tv_delPrice);
            tv_totalPrice=itemView.findViewById(R.id.tv_totalPrice);
            tv_regNo=itemView.findViewById(R.id.tv_regNo);
        }
    }
}

