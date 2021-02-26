package com.gift.house;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gift.house.adapters.ProductsAdapter;
import com.gift.house.models.Order;
import com.gift.house.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {


    private Order order;
    private TextView tv_order_id, tv_phone, tv_del_address, tv_total_amount;
    private RecyclerView rv_products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        tv_order_id = findViewById(R.id.tv_order_id);
        tv_phone = findViewById(R.id.tv_phone);
        tv_del_address = findViewById(R.id.tv_del_address);
        rv_products = findViewById(R.id.rv_products_home);
        tv_total_amount = findViewById(R.id.tv_total_amount);
//        rv_products = findViewById(R.id.rv_categories);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            order = (Order) bundle.getSerializable("order");
            setData();
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        tv_order_id.setText("Order ID: " + order.getOrder_number());
        tv_phone.setText(order.getUser_phone());
        tv_del_address.setText(order.getDelAddress());
        tv_total_amount.setText("Total Amount: ₹" + order.getAmount());

        ArrayList<Product> products = new ArrayList<>();

        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Product>>() {
            }.getType();
            products.addAll(gson.fromJson(order.getProducts(), listType));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ProductsAdapter productsAdapter = new ProductsAdapter(this, products, true);
        rv_products.setLayoutManager(new LinearLayoutManager(this));
        rv_products.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv_products.setAdapter(productsAdapter);
    }
}