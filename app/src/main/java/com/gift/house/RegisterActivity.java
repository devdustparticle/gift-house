package com.gift.house;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gift.house.models.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, phone, email, password;
    private Button signUp;
    private TextView login;

    private DBHelper database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        database = new DBHelper(this);
        name = findViewById(R.id.etName);
        phone = findViewById(R.id.etPhone);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPass);
        signUp = findViewById(R.id.btnSignUP);
        login = findViewById(R.id.btn_Login);

        signUp.setOnClickListener(this);
        login.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSignUP:
                register();
                break;
            case R.id.btn_Login:
                finish();
                break;
        }

    }

    private void register() {
        User user = new User();
        user.setName(name.getText().toString().trim());
        user.setPhone(phone.getText().toString().trim());
        user.setEmail(email.getText().toString().trim());
        user.setPassword(password.getText().toString().trim());
        user.setAddress("");

        if (!valid(user)) return;

        database.insertUser(user);
        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_LONG).show();
        finish();


    }

    private boolean valid(User user) {

        if (user.getName().isEmpty()) {
            Toast.makeText(this, "Enter name", Toast.LENGTH_LONG).show();
            name.setError("Enter valid name");
            return false;
        }
        if (user.getPhone().isEmpty()) {
            Toast.makeText(this, "Enter valid phone", Toast.LENGTH_LONG).show();
            phone.setError("Enter valid phone");
            return false;
        }
        if (user.getEmail().isEmpty()) {
            Toast.makeText(this, "Enter valid email id", Toast.LENGTH_LONG).show();
            email.setError("Enter valid email");
            return false;
        }
        if (user.getPassword().isEmpty()) {
            Toast.makeText(this, "Enter valid Password", Toast.LENGTH_LONG).show();
            password.setError("Enter valid Password");
            return false;
        }


        return true;
    }
}