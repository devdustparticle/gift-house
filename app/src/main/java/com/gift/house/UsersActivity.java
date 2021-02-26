package com.gift.house;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gift.house.adapters.UsersAdapter;
import com.gift.house.models.User;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    Context context;
    RecyclerView rvUsers;
    UsersAdapter usersAdapter;
    DBHelper database;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        context = this;
        database = new DBHelper(context);
        users = new ArrayList<>();
        users.addAll(database.getAllUsers());
        rvUsers = findViewById(R.id.rv_users);
        usersAdapter = new UsersAdapter(context, users);
        rvUsers.setLayoutManager(new LinearLayoutManager(context));
        rvUsers.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        rvUsers.setAdapter(usersAdapter);
    }
}