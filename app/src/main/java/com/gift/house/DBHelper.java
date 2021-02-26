package com.gift.house;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gift.house.models.Category;
import com.gift.house.models.Order;
import com.gift.house.models.Product;
import com.gift.house.models.User;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    // Contacts Table Columns names
    private static final String USER = "user";
    private static final String CATEGORY = "category";
    private static final String PRODUCT = "product";
    private static final String ORDER = "orders";
    private static final String CART = "cart";
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MyDatabase.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + USER + "(id integer primary key, name text,phone text,address text,email text,password text)");
        sqLiteDatabase.execSQL("create table " + CATEGORY + "(id integer primary key, name text)");
        sqLiteDatabase.execSQL("create table " + PRODUCT + "(id integer primary key,cat_name text,name text, price text, description text, photo text)");
        sqLiteDatabase.execSQL("create table " + ORDER + "(id integer primary key,order_number text, user_phone text, amount text, products text, del_address text)");
        sqLiteDatabase.execSQL("create table " + CART + "(id integer primary key,order_number text, user_phone text, amount text, products text, del_address text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CATEGORY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PRODUCT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ORDER);
        // Create tables again
        onCreate(sqLiteDatabase);

    }

    public boolean userLogin(String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + USER + " where phone='" + phone + "' and password='" + password + "'", null);
        return res.getCount() > 0;
    }

    //Insert Query
    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getName());
        contentValues.put("phone", user.getPhone());
        contentValues.put("address", user.getAddress());
        contentValues.put("email", user.getEmail());
        contentValues.put("password", user.getPassword());
        db.insert(USER, null, contentValues);
        return true;
    }

    public boolean insertProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", product.getName());
        contentValues.put("cat_name", product.getCat_name());
        contentValues.put("description", product.getDescription());
        contentValues.put("price", product.getPrice());
        contentValues.put("photo", product.getPhoto());
        db.insert(PRODUCT, null, contentValues);
        return true;
    }

    public boolean insertCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", category.getName());
        db.insert(CATEGORY, null, contentValues);
        return true;
    }


    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getName());
        contentValues.put("phone", user.getPhone());
        contentValues.put("address", user.getAddress());
        contentValues.put("email", user.getEmail());
        contentValues.put("password", user.getPassword());
        db.update(USER, contentValues, "id = ? ", new String[]{Integer.toString(user.getId())});
        return true;
    }

    public boolean updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", product.getName());
        contentValues.put("cat_name", product.getCat_name());
        contentValues.put("description", product.getDescription());
        contentValues.put("price", product.getPrice());
        contentValues.put("photo", product.getPhoto());
        db.update(PRODUCT, contentValues, "id = ? ", new String[]{Integer.toString(product.getId())});
        return true;
    }

    public boolean updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", category.getName());
        db.update(CATEGORY, contentValues, "id = ? ", new String[]{Integer.toString(category.getId())});
        return true;
    }

    public boolean deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PRODUCT, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public boolean deleteCategory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CATEGORY, "id = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public User getUser(String userPhone) {
        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from " + USER + " where id=" + id + "", null);
        Cursor res = db.rawQuery("select * from " + USER + " where phone='" + userPhone + "'", null);
        User user = new User();
        user.setId(res.getInt(res.getColumnIndex("id")));
        user.setName(res.getString(res.getColumnIndex("name")));
        user.setPhone(res.getString(res.getColumnIndex("phone")));
        user.setAddress(res.getString(res.getColumnIndex("address")));
        user.setEmail(res.getString(res.getColumnIndex("email")));
        user.setPassword(res.getString(res.getColumnIndex("password")));
        return user;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + USER, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            User user = new User();
            user.setId(res.getInt(res.getColumnIndex("id")));
            user.setName(res.getString(res.getColumnIndex("name")));
            user.setPhone(res.getString(res.getColumnIndex("phone")));
            user.setAddress(res.getString(res.getColumnIndex("address")));
            user.setEmail(res.getString(res.getColumnIndex("email")));
            user.setPassword(res.getString(res.getColumnIndex("password")));
            users.add(user);
            res.moveToNext();
        }
        return users;
    }

    public Product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PRODUCT + " where id=" + id + "", null);
        Product product = new Product();
        product.setId(id);
        product.setName(res.getString(res.getColumnIndex("name")));
        product.setCat_name(res.getString(res.getColumnIndex("cat_name")));
        product.setDescription(res.getString(res.getColumnIndex("description")));
        product.setPrice(res.getString(res.getColumnIndex("price")));
        product.setPhoto(res.getString(res.getColumnIndex("photo")));
        return product;
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PRODUCT, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            Product product = new Product();
            product.setId(res.getInt(res.getColumnIndex("id")));
            product.setName(res.getString(res.getColumnIndex("name")));
            product.setCat_name(res.getString(res.getColumnIndex("cat_name")));
            product.setDescription(res.getString(res.getColumnIndex("description")));
            product.setPrice(res.getString(res.getColumnIndex("price")));
            product.setPhoto(res.getString(res.getColumnIndex("photo")));
            products.add(product);
            res.moveToNext();
        }
        return products;
    }

    public Category getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CATEGORY + " where id=" + id + "", null);
        Category category = new Category();
        category.setId(id);
        category.setName(res.getString(res.getColumnIndex("name")));
        return category;
    }

    public ArrayList<String> getAllCategories() {
        ArrayList<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CATEGORY, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            categories.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        return categories;
    }

    public ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CATEGORY, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            Category category = new Category();
            category.setId(res.getInt(res.getColumnIndex("id")));
            category.setName(res.getString(res.getColumnIndex("name")));
            categories.add(category);
            res.moveToNext();
        }
        return categories;
    }

    public ArrayList<Order> getAllOrders() {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ORDER, null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            Order order = new Order();
            order.setId(res.getInt(res.getColumnIndex("id")));
            order.setOrder_number(res.getString(res.getColumnIndex("order_number")));
            order.setUser_phone(res.getString(res.getColumnIndex("user_phone")));
            order.setAmount(res.getString(res.getColumnIndex("amount")));
            order.setProducts(res.getString(res.getColumnIndex("products")));
            order.setDelAddress(res.getString(res.getColumnIndex("del_address")));
            orders.add(order);
            res.moveToNext();
        }
        return orders;
    }

    public ArrayList<Order> getOrders(String userPhone) {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ORDER + " where user_phone='" + userPhone + "'", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            Order order = new Order();
            order.setId(res.getInt(res.getColumnIndex("id")));
            order.setOrder_number(res.getString(res.getColumnIndex("order_number")));
            order.setUser_phone(res.getString(res.getColumnIndex("user_phone")));
            order.setAmount(res.getString(res.getColumnIndex("amount")));
            order.setProducts(res.getString(res.getColumnIndex("products")));
            order.setDelAddress(res.getString(res.getColumnIndex("del_address")));
            orders.add(order);
            res.moveToNext();
        }
        return orders;
    }

    public boolean insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_number", order.getOrder_number());
        contentValues.put("user_phone", order.getUser_phone());
        contentValues.put("amount", order.getAmount());
        contentValues.put("products", order.getProducts());
        contentValues.put("del_address", order.getDelAddress());
        db.insert(ORDER, null, contentValues);
        return true;
    }

    public boolean insertCart(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_number", order.getOrder_number());
        contentValues.put("user_phone", order.getUser_phone());
        contentValues.put("amount", order.getAmount());
        contentValues.put("products", order.getProducts());
        contentValues.put("del_address", order.getDelAddress());
        db.insert(CART, null, contentValues);
        return true;
    }

    public boolean updateCart(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("order_number", order.getOrder_number());
        contentValues.put("user_phone", order.getUser_phone());
        contentValues.put("amount", order.getAmount());
        contentValues.put("products", order.getProducts());
        contentValues.put("del_address", order.getDelAddress());
        db.update(CART, contentValues, "id = ? ", new String[]{Integer.toString(order.getId())});
        return true;
    }

    public boolean deleteCart(Order cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART, "id = ? ", new String[]{Integer.toString(cart.getId())});
        return true;
    }

    public boolean deleteAllCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CART, "", new String[]{});
        return true;
    }

    public ArrayList<Order> getCart(String userPhone) {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CART + " where user_phone='" + userPhone + "'", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            Order order = new Order();
            order.setId(res.getInt(res.getColumnIndex("id")));
            order.setOrder_number(res.getString(res.getColumnIndex("order_number")));
            order.setUser_phone(res.getString(res.getColumnIndex("user_phone")));
            order.setAmount(res.getString(res.getColumnIndex("amount")));
            order.setProducts(res.getString(res.getColumnIndex("products")));
            order.setDelAddress(res.getString(res.getColumnIndex("del_address")));
            orders.add(order);
            res.moveToNext();
        }
        return orders;
    }
}
