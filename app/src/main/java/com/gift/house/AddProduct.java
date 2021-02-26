package com.gift.house;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.gift.house.models.Product;
import com.gift.house.utils.utils;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class AddProduct extends AppCompatActivity implements View.OnClickListener {

    Product product;
    private EditText name, price, description;
    private Spinner cat_name;
    private AppCompatImageView photo;
    private DBHelper database;
    private ArrayAdapter<String> categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        database = new DBHelper(this);
        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        cat_name = findViewById(R.id.cat_name);
        photo = findViewById(R.id.photo);

        findViewById(R.id.save).setOnClickListener(this);
        photo.setOnClickListener(this);
        selectCategories();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            product = (Product) bundle.getSerializable("product");
            setData(product);
            getSupportActionBar().setTitle("Edit Product");
        }

    }

    private void selectCategories() {
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, database.getAllCategories());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat_name.setAdapter(categoryAdapter);
    }

    private void setData(Product product) {
        name.setText(product.getName());
        price.setText(product.getPrice());
        description.setText(product.getDescription());
        cat_name.setSelection(categoryAdapter.getPosition(product.getCat_name()));
        if (product.getPhoto() != null) {
            photo.setImageBitmap(utils.getBitmap(product.getPhoto()));
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                if (product == null) {
                    insert();
                } else {
                    update(product);
                }
                break;
            case R.id.photo:
                pickPhoto();
                break;
        }

    }

    private void insert() {
        Product p = new Product();
        p.setName(name.getText().toString().trim());
        p.setPrice(price.getText().toString().trim());
        p.setDescription(description.getText().toString().trim());
        p.setCat_name(cat_name.getSelectedItem().toString());
        try {
            p.setPhoto(utils.getEncodedImageString(utils.getBitmap(photo)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!valid(p)) return;

        database.insertProduct(p);
        Toast.makeText(this, "Product Added Successfully", Toast.LENGTH_LONG).show();
        onBackPressed();

    }

    private void update(Product product) {
        product.setName(name.getText().toString().trim());
        product.setPrice(price.getText().toString().trim());
        product.setDescription(description.getText().toString().trim());
        product.setCat_name(cat_name.getSelectedItem().toString());
        try {
            product.setPhoto(utils.getEncodedImageString(utils.getBitmap(photo)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!valid(product)) return;

        database.updateProduct(product);
        Toast.makeText(this, "Updated Successfully", Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    private boolean valid(Product product) {

        if (product.getName().isEmpty()) {
            Toast.makeText(this, "Enter Product name", Toast.LENGTH_LONG).show();
            name.setError("Enter Product name");
            return false;
        }

        if (product.getPrice().isEmpty()) {
            Toast.makeText(this, "Enter Price", Toast.LENGTH_LONG).show();
            price.setError("Enter price");
            return false;
        }


        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_product, menu);
        if (product == null)
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
        database.deleteProduct(product.getId());
        finish();
    }

    public void pickPhoto() {
        ImagePicker.Companion.with(this)
                //.crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(512, 512)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri fileUri = null;
            if (data != null) {
                fileUri = data.getData();
                photo.setImageURI(fileUri);
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}