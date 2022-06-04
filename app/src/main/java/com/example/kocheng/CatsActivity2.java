package com.example.kocheng;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CatsActivity2 extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    DatabaseHelper helper;
    LayoutInflater inflater;
    View dialogView;

    ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cats2);

        inisialisasi();

        mainButton();

    }

    private void inisialisasi(){
        helper = new DatabaseHelper(this);
        listView = (ListView)findViewById(R.id.cartListview);
        listView.setOnItemClickListener(this);

        btnBack = (ImageButton) findViewById(R.id.btn_back);
    }

    private void mainButton(){

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatsActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setListView(){
        Cursor cursor = helper.allData();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, cursor, 1);
        listView.setAdapter(customCursorAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long x) {

        TextView getId = (TextView)view.findViewById(R.id.listID);
        final long id = Long.parseLong(getId.getText().toString());
        final Cursor cur = helper.oneData(id);
        cur.moveToFirst();

        final AlertDialog.Builder builder = new AlertDialog.Builder(CatsActivity2.this);
        builder.setTitle("Pilih Opsi");

        //Add a list
        String[] options = {"View Detail", "Edit Data", "Hapus Data"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        String selection = cur.getString(cur.getColumnIndexOrThrow(DatabaseHelper.namakucing));

                        Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                        i.putExtra("namakucing", selection);
                        startActivity(i);
                        break;
                }
                switch (which){
                    case 1:
                        Intent iddata = new Intent(CatsActivity2.this, EditActivity2.class);
                        iddata.putExtra(DatabaseHelper.row_id, id);
                        startActivity(iddata);
                }
                switch (which) {
                    case 2:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CatsActivity2.this);
                        builder1.setMessage("Data ini akan dihapus.");
                        builder1.setCancelable(true);
                        builder1.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                helper.deleteData(id);
                                Toast.makeText(CatsActivity2.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                                setListView();
                            }
                        });
                        builder1.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder1.create();
                        alertDialog.show();

                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListView();
    }

}