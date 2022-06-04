package com.example.kocheng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class hansap extends AppCompatActivity {
    TextView kelompok10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hansap);

        kelompok10 = findViewById(R.id.kelompok10);
        kelompok10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comeback();
            }
        });
    }

    public void comeback(){
        Intent intent = new Intent(this, Info.class);
        startActivity(intent);
    }
}
