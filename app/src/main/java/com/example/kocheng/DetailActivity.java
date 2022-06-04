package com.example.kocheng;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    // GANTI VARIABEL SESUAI NAMA DESAIN
    ImageView ivGambarDetail;
    TextView tvKucingDetail, tvJenisDetail, tvTitipDetail, tvKembaliDetail, tvNamaDetail, tvNoDetail, tvBeratDetail, tvPesanDetail;

    ImageButton btnDetail;

    protected Cursor cursor;
    DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        btnDetail = (ImageButton) findViewById(R.id.btn_back_from_detail);
        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this,CatsActivity2.class);
                startActivity(intent);
            }
        });


        // unntuk init
        inisialisasi();

        // untuk atur nilai detail kucing
        setValueKucing();
    }

    private void inisialisasi(){

        // GANTI INISIALISASI UNTUK MENGHUBUNGKAN KOMPONEN KE LAYOUT XML

        ivGambarDetail = (ImageView) findViewById(R.id.gambar_detail);
        tvKucingDetail = (TextView) findViewById(R.id.kucing_detail);
        tvJenisDetail = (TextView) findViewById(R.id.jenis_detail);
        tvTitipDetail = (TextView) findViewById(R.id.titip_detail);
        tvKembaliDetail = (TextView) findViewById(R.id.kembali_detail);
        tvNamaDetail = (TextView) findViewById(R.id.nama_detail);
        tvNoDetail = (TextView) findViewById(R.id.no_detail);
        tvBeratDetail = (TextView) findViewById(R.id.berat_detail);
        tvPesanDetail = (TextView) findViewById(R.id.pesan_detail);


        databaseHelper = new DatabaseHelper(this);

        // toolbar

    }

    private void setValueKucing(){
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM cing WHERE namakucing = '" +
                getIntent().getStringExtra("namakucing") + "'",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            cursor.moveToPosition(0);

            // ini yang diubah untuk mengatur

            tvNamaDetail.setText( cursor.getString(1));
            tvNoDetail.setText(cursor.getString(2));
            tvKucingDetail.setText(cursor.getString(3));
            tvBeratDetail.setText(cursor.getString(4));
            tvJenisDetail.setText(cursor.getString(5));
            tvTitipDetail.setText(cursor.getString(6));
            tvKembaliDetail.setText(cursor.getString(7));
            ivGambarDetail.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(8)));
            tvPesanDetail.setText(cursor.getString(9));

        }
    }
}