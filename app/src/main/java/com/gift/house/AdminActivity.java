package com.gift.house;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private Button view_orders, category_master, product_master, all_users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        view_orders = findViewById(R.id.view_orders);
        category_master = findViewById(R.id.category_master);
        product_master = findViewById(R.id.product_master);
        all_users = findViewById(R.id.all_users);

        view_orders.setOnClickListener(this);
        category_master.setOnClickListener(this);
        product_master.setOnClickListener(this);
        all_users.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("GiftHouse",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("admin","");
        editor.apply();
        startActivity(new Intent(this,LoginActivity.class));
        finishAffinity();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.view_orders:
                startActivity(new Intent(this, OrdersActivity.class));
                break;

            case R.id.category_master:
                startActivity(new Intent(this, CategoriesActivity.class));
                break;

            case R.id.product_master:
                startActivity(new Intent(this, ProductsActivity.class));
                break;

            case R.id.all_users:
                startActivity(new Intent(this, UsersActivity.class));
                break;
        }

    }
}