package com.example.kocheng;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Info extends AppCompatActivity {

    CardView cardViewNaura;
    CardView cardViewHansap;
    CardView cardViewRazmi;
    CardView cardViewYoga;
    CardView cardViewLuey;
    ImageView imageview13;

    ImageButton btnBackFromInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);

        // method back button to mainActivity
        imageview13 = (ImageView) findViewById(R.id.imageView13);
        imageview13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                balik();
            }
        });

        // panggil fungsi
        inisialisasiVariable();

        //panggil lagi
        mainButton();
    }
    // buat fungsinya disini, biar di onCreate, kita tinggal manggil" odang, jadinya codenyya rapi
    // dan best practice
    private void inisialisasiVariable(){

        //sesuaikan namaa id dengan di cardview
        cardViewNaura = (CardView) findViewById(R.id.cardview_naura);
        cardViewHansap = (CardView) findViewById(R.id.cardview_hansap);
        cardViewRazmi = (CardView) findViewById(R.id.cardview_razmi);
        cardViewLuey = (CardView) findViewById(R.id.cardview_luey);
        cardViewYoga = (CardView) findViewById(R.id.cardview_yoga);

        // btn back
        btnBackFromInfo = (ImageButton) findViewById(R.id.btn_back_from_info);
    }
    // bikin function button, biar tinggal manggil dan jadi lebih rapi
    private void mainButton(){

        // back
        btnBackFromInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Info.this, MainActivity.class);
                startActivity(intent);
            }
        });

        cardViewRazmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // intent untuk pindah aktivity
                Intent intent = new Intent(Info.this, mrazmi.class);
                startActivity(intent);
            }
        });
        cardViewNaura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // intent untuk pindah aktivity
                Intent intent = new Intent(Info.this, naura.class);
                startActivity(intent);
            }
        });
        cardViewHansap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // intent untuk pindah aktivity
                Intent intent = new Intent(Info.this, hansap.class);
                startActivity(intent);
            }
        });
        cardViewLuey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // intent untuk pindah aktivity
                Intent intent = new Intent(Info.this, aluey.class);
                startActivity(intent);
            }
        });
        cardViewYoga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // intent untuk pindah aktivity
                Intent intent = new Intent(Info.this, yogas.class);
                startActivity(intent);
            }
        });
    }
    // method kembali ke menu utama
    public void balik(){
        Intent intent = new Intent(Info.this, MainActivity.class);
        startActivity(intent);
    }
}
