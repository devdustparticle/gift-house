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

import com.gift.house.adapters.CategoriesAdapter;
import com.gift.house.models.Category;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {

    Context context;
    RecyclerView rvCategories;
    CategoriesAdapter categoriesAdapter;
    DBHelper database;
    ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        context = this;
        database = new DBHelper(context);

    }

    @Override
    protected void onResume() {
        super.onResume();
        categories = new ArrayList<>();
        categories.addAll(database.getCategories());
        rvCategories = findViewById(R.id.rv_categories);
        categoriesAdapter = new CategoriesAdapter(context, categories);
        rvCategories.setLayoutManager(new LinearLayoutManager(context));
        rvCategories.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rvCategories.setAdapter(categoriesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_add_cat:
                //TODO add menu action here
                startActivity(new Intent(context, AddCategory.class));
                return true;

            default:

                return super.onOptionsItemSelected(item);
        }

    }

}