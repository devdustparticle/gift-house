package com.gift.house;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gift.house.models.Category;

public class AddCategory extends AppCompatActivity {

    private EditText cat_name;
    private DBHelper database;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        database = new DBHelper(this);
        cat_name = findViewById(R.id.cat_name);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = (Category) bundle.get("category");
            cat_name.setText(category.getName());
        }
    }

    public void onClick(View view) {
        if (category == null) {
            insert();
        } else {
            update(category);
        }
    }

    private void insert() {
        Category cat = new Category();
        cat.setName(cat_name.getText().toString().trim());

        if (cat.getName().isEmpty()) {
            Toast.makeText(this, "Enter Category", Toast.LENGTH_LONG).show();
            cat_name.setError("Enter Category");
            return;
        }

        database.insertCategory(cat);
        Toast.makeText(this, "Category Added Successfully", Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    private void update(Category category) {

        if (cat_name.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Enter Category", Toast.LENGTH_LONG).show();
            cat_name.setError("Enter Category");
            return;
        }

        category.setName(cat_name.getText().toString().trim());
        database.updateCategory(category);
        Toast.makeText(this, "Updated Successfully", Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_category, menu);
        if (category == null)
            menu.findItem(R.id.action_delete).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                //TODO add menu action here
                delete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void delete() {
        database.deleteCategory(category.getId());
        finish();
    }
}