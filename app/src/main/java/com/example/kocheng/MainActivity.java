package com.example.kocheng;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarItemView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationItemView btn_cats;
    NavigationBarItemView btn_info;

    FloatingActionButton button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inisialisasi();
        mainButton();

    }

    private void inisialisasi(){

        btn_cats = (BottomNavigationItemView) findViewById(R.id.btn_cats);
        btn_info = (BottomNavigationItemView) findViewById(R.id.info1);

        button = (FloatingActionButton) findViewById(R.id.buttonadd);

    }

    private void mainButton(){

        // ke cats activity
        btn_cats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CatsActivity2.class);
                startActivity(intent);
            }
        });

        // ke form add
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Form.class);
                startActivity(intent);
            }
        });

        // ke about activity
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Info.class);
                startActivity(intent);
            }
        });

    }
}