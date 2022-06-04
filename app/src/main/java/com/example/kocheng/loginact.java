package com.example.kocheng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginact extends AppCompatActivity {
    DatabaseHelper db;
    Button login, register;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginact);

        db = new DatabaseHelper(this);

        username = (EditText)findViewById(R.id.edtText_username);
        password = (EditText)findViewById(R.id.edtText_password);
        login = (Button)findViewById(R.id.btn_login);
        register = (Button)findViewById(R.id.btn_register);

        //register
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(loginact.this, regisact.class);
                startActivity(registerIntent);
                finish();
                }
            });

                //login
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String strUsername = username.getText().toString();
                        String strPassword = password.getText().toString();
                        Boolean masuk = db.checkLogin(strUsername, strPassword);
                        if (masuk == true) {
                            Boolean updateSession = db.upgradeSession("ada", 1);
                            if (updateSession == true) {
                                Toast.makeText(getApplicationContext(), "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(loginact.this, MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal masuk!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}