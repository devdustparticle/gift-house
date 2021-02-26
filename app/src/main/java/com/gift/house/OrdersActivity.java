package com.gift.house;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gift.house.adapters.OrdersAdapter;
import com.gift.house.models.Order;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    Context context;
    RecyclerView rvOrders;
    OrdersAdapter ordersAdapter;
    DBHelper database;
    ArrayList<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        context = this;
        database = new DBHelper(context);
        orders = new ArrayList<>();
        ArrayList<Order> orders_ = database.getAllOrders();
        for (Order order : orders_) {
            orders.add(0, order);
        }
        rvOrders = findViewById(R.id.rv_orders);
        ordersAdapter = new OrdersAdapter(context, orders);
        rvOrders.setLayoutManager(new LinearLayoutManager(context));
        rvOrders.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rvOrders.setAdapter(ordersAdapter);
    }
}