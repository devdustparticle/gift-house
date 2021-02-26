package com.gift.house;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ADMIN_PHONE = "1111111111";
    public static final String ADMIN_PASSWORD = "admin";

    private EditText username;
    private EditText password;
    private Button signIn;
    private TextView signUp;
    private DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = new DBHelper(this);
        username = findViewById(R.id.etMob);
        password = findViewById(R.id.etPass);
        signIn = findViewById(R.id.btn_Login);
        signUp = findViewById(R.id.txt_Signup);

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Login:
                userLogin();
                break;
            case R.id.txt_Signup:
                startActivity(new Intent(this, RegisterActivity.class));

                break;
        }

    }

    private void userLogin() {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if (!valid(user, pass)) return;

        if (user.equals(ADMIN_PHONE) && pass.equals(ADMIN_PASSWORD)) {
            SharedPreferences sharedPreferences = getSharedPreferences("GiftHouse", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("admin", "admin");
            editor.apply();
            startActivity(new Intent(this, AdminActivity.class));
            finish();
        } else {
            if (database.userLogin(user, pass)) {
                SharedPreferences sharedPreferences = getSharedPreferences("GiftHouse", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try {
                    editor.putString("user_name", database.getUser(user).getName());
                } catch (Exception e) {
                    editor.putString("user_name", user);
                }
                editor.putString("user_phone", user);
                editor.apply();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Incorrect Username or Password", Toast.LENGTH_LONG).show();
            }
        }

    }

    private boolean valid(String user, String pass) {

        if (user.isEmpty()) {
            Toast.makeText(this, "Enter valid username", Toast.LENGTH_LONG).show();
            username.setError("Enter valid Username");
            return false;
        }
        if (pass.isEmpty()) {
            Toast.makeText(this, "Enter valid Password", Toast.LENGTH_LONG).show();
            password.setError("Enter valid Password");
            return false;
        }

        return true;
    }
}