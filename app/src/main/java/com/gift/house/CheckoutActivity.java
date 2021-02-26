package com.gift.house;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gift.house.models.Order;

public class CheckoutActivity extends AppCompatActivity {

    private TextView address;
    private Order order;
    private DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        address = findViewById(R.id.address);

        order = (Order) getIntent().getSerializableExtra("cart");
        database = new DBHelper(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Order placed");
        builder.setMessage("Your order has been placed successfully");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("View order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(222);
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();

        findViewById(R.id.checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (address.getText().toString().trim().isEmpty()) {
                    Toast.makeText(CheckoutActivity.this, "Please enter delivery address", Toast.LENGTH_LONG).show();
                } else {
                    order.setDelAddress(address.getText().toString().trim());
                    if (database.insertOrder(order) && database.deleteAllCart()) {
                        alertDialog.show();
                    }
                }
            }
        });
    }
}