package com.gift.house;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gift.house.adapters.ProductsAdapter;
import com.gift.house.models.Product;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {

    Context context;
    RecyclerView rvProducts;
    ProductsAdapter productsAdapter;
    DBHelper database;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        context = this;
        database = new DBHelper(context);

        rvProducts = findViewById(R.id.rv_products_home);

    }

    @Override
    protected void onResume() {
        super.onResume();
        products = new ArrayList<>();

        products.addAll(database.getAllProducts());
        productsAdapter = new ProductsAdapter(context, products);
        rvProducts.setLayoutManager(new LinearLayoutManager(context));
        rvProducts.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rvProducts.setAdapter(productsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add:
                //TODO add menu action here
                startActivity(new Intent(context, AddProduct.class));
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

    }

}