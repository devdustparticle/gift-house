package com.gift.house;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.gift.house.adapters.HomePagerAdapter;
import com.gift.house.adapters.OrdersAdapter;
import com.gift.house.adapters.ProductsAdapter;
import com.gift.house.models.Order;
import com.gift.house.models.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    DrawerLayout drawer;
    RecyclerView rvProducts;
    DBHelper database;
    Context context;
    private ArrayList<Product> products, cartProducts;
    private ProductsAdapter productsAdapter, cartAdapter;
    private OrdersAdapter myOrdersAdapter;
    private SharedPreferences sharedPreferences;
    private ViewPager viewPager;
    private HomePagerAdapter homePagerAdapter;
    private TextView tvTitle;
    private RecyclerView recyclerCart;
    private RelativeLayout noCartData;
    private LinearLayout viewCartItems;
    private TextView txt_totalQuan, txt_totalamount;
    private TextView btn_Checkout;
    private Order order;
    private ArrayList<Order> myOrders;
    private RecyclerView rv_my_orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        sharedPreferences = getSharedPreferences("GiftHouse", MODE_PRIVATE);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        navigationView = findViewById(R.id.side_nav);
        drawer = findViewById(R.id.drawer_layout);
        viewPager = findViewById(R.id.view_pager);
        tvTitle = findViewById(R.id.tv_title);
        txt_totalQuan = findViewById(R.id.txt_totalQuan);
        txt_totalamount = findViewById(R.id.txt_totalamount);
        btn_Checkout = findViewById(R.id.btn_Checkout);

        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(navigationView, true);
            }
        });

        findViewById(R.id.btn_ShopNOw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.home);
            }
        });

        database = new DBHelper(context);
        rvProducts = findViewById(R.id.rv_products_home);
        rv_my_orders = findViewById(R.id.rv_my_orders);

        View header = navigationView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.tv_header_name)).setText(sharedPreferences.getString("user_name", ""));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    /*case R.id.nav_my_profile:

                        break;*/
                    case R.id.nav_aboutus:

                        break;
                    case R.id.nav_share:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,
                                "Hey check out my app at: https://play.google.com/store/apps/details?id==" + BuildConfig.APPLICATION_ID);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                        break;
                    case R.id.nav_logout:
                        SharedPreferences sharedPreferences = getSharedPreferences("GiftHouse", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user_name", "");
                        editor.putString("user_phone", "");
                        editor.apply();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finishAffinity();
                        break;
                }
                drawer.closeDrawer(navigationView);
                return true;
            }
        });

        ArrayList<Integer> layouts = new ArrayList<>();
        layouts.add(R.id.rv_products_home);
        layouts.add(R.id.rv_my_orders);
        layouts.add(R.id.cart_layout);
        homePagerAdapter = new HomePagerAdapter(context, layouts);
        viewPager.setAdapter(homePagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.my_orders);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.cart);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        viewPager.setCurrentItem(0, true);
                        tvTitle.setText("Gifts");
                        setProductsData();
                        break;
                    case R.id.my_orders:
                        viewPager.setCurrentItem(1, true);
                        tvTitle.setText("My Orders");
                        setMyOrdersData();
                        break;
                    case R.id.cart:
                        viewPager.setCurrentItem(2, true);
                        tvTitle.setText("Cart");
                        setCartData();
                        break;
                }
                homePagerAdapter.notifyDataSetChanged();
                return true;
            }
        });

        recyclerCart = findViewById(R.id.recyclerCart);
        noCartData = findViewById(R.id.noData);
        viewCartItems = findViewById(R.id.viewCartItems);

        btn_Checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult((new Intent(context, CheckoutActivity.class)).putExtra("cart", order), 222);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setProductsData();
        setCartData();
        setMyOrdersData();
    }

    private void setProductsData() {
        products = new ArrayList<>();
        products.addAll(database.getAllProducts());
        productsAdapter = new ProductsAdapter(context, products, R.layout.row_layout_product, false);
        rvProducts.setLayoutManager(new LinearLayoutManager(context));
//        rvProducts.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
        rvProducts.setAdapter(productsAdapter);
    }

    private void setMyOrdersData() {
        myOrders = new ArrayList<>();
        try {
            ArrayList<Order> orders = database.getOrders(sharedPreferences.getString("user_phone", ""));
            for (Order order : orders) {
                myOrders.add(0, order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        myOrdersAdapter = new OrdersAdapter(context, myOrders);
        rv_my_orders.setLayoutManager(new LinearLayoutManager(context));
        rv_my_orders.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rv_my_orders.setAdapter(myOrdersAdapter);
    }

    @SuppressLint("SetTextI18n")
    private void setCartData() {
        cartProducts = new ArrayList<>();
        try {
            order = database.getCart(sharedPreferences.getString("user_phone", "")).get(0);
            cartProducts.addAll(getProducts(order.getProducts()));
            if (cartProducts.size() > 0) {
                noCartData.setVisibility(View.GONE);
                viewCartItems.setVisibility(View.VISIBLE);
            } else {
                viewCartItems.setVisibility(View.GONE);
                noCartData.setVisibility(View.VISIBLE);
            }
            cartAdapter = new ProductsAdapter(context, cartProducts, R.layout.row_layout_product, true);
            recyclerCart.setLayoutManager(new LinearLayoutManager(context));
//        rvProducts.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
            recyclerCart.setAdapter(cartAdapter);
            txt_totalQuan.setText("Items: " + cartProducts.size());
            txt_totalamount.setText("Amount: ₹" + order.getAmount());
        } catch (Exception e) {
            e.printStackTrace();
            viewCartItems.setVisibility(View.GONE);
            noCartData.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<Product> getProducts(String products_) {
        ArrayList<Product> products = new ArrayList<>();
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Product>>() {
            }.getType();
            products.addAll(gson.fromJson(products_, listType));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 222 && resultCode == 222) {
            bottomNavigationView.setSelectedItemId(R.id.my_orders);
        }
    }
}