package com.example.easemyride;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterOrderAdv extends RecyclerView.Adapter<AdapterOrderAdv.Holder> {

    private ArrayList<modelOrderAdv> orderList;
    private Context context;

    public AdapterOrderAdv(Context context,ArrayList<modelOrderAdv> orderList) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_order_adv,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        modelOrderAdv modelOrderAdv=orderList.get(position);

        String orderID=modelOrderAdv.getOrderID();
        String orderTime=modelOrderAdv.getOrderTime();
        String orderStatus=modelOrderAdv.getOrderStatus();
        String orderCost=modelOrderAdv.getOrderCost();
        String orderBy=modelOrderAdv.getOrderBy();
        String orderTo=modelOrderAdv.getOrderTo();
        String orderType=modelOrderAdv.getOrderTitle();

//        loadShopInfo(modelOrderAdv,holder);

        holder.tv_ordersID.setText(orderID);
        holder.tv_orderDate.setText(orderTime);
        holder.tv_orderStatus.setText(orderStatus);
        holder.tv_orderAmount.setText(orderCost);
        holder.tv_orderType.setText(orderType);
        if(orderStatus.equals("In Progress")){
            holder.tv_orderStatus.setTextColor(context.getResources().getColor(R.color.green));
        }
        else if(orderStatus.equals("Completed")){
            holder.tv_orderStatus.setTextColor(context.getResources().getColor(R.color.teal_200));
        }
        else if(orderStatus.equals("Cancelled")){
            holder.tv_orderStatus.setTextColor(context.getResources().getColor(R.color.red));
        }
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(orderTime));
        String formatDate= DateFormat.format("dd/MM/yyyy",calendar).toString();

        holder.tv_orderDate.setText(formatDate);

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(context,OrderDetailsUserActivity.class);
//                intent.putExtra("orderTo",orderTo);
//                intent.putExtra("orderID",orderID);
//                context.startActivity(intent);
//            }
//        });
    }

    private void loadShopInfo(modelOrderAdv modelOrderUser, Holder holder) {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Customer");
        reference.child(modelOrderUser.getOrderTo()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type=""+snapshot.child("orderTitle").getValue();
                holder.tv_orderType.setText(type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        private TextView tv_ordersID,tv_orderDate,tv_orderType,tv_orderAmount,tv_orderStatus;
        private ImageView iv_next;

        public Holder(@NonNull View itemView) {
            super(itemView);

            tv_ordersID=itemView.findViewById(R.id.tv_ordersID);
            tv_orderDate=itemView.findViewById(R.id.tv_orderDate);
            tv_orderType=itemView.findViewById(R.id.tv_orderType);
            tv_orderAmount=itemView.findViewById(R.id.tv_orderAmount);
            tv_orderStatus=itemView.findViewById(R.id.tv_orderStatus);
            iv_next=itemView.findViewById(R.id.iv_next);
        }
    }

}
