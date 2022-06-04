package com.example.kocheng;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.List;

public class Form extends AppCompatActivity {

    // default id untuk id kucing
    long id;

    EditText inputNoTelp;
    EditText inputpesan;
    EditText inputberat;
    EditText inputNamaP;
    EditText inputNamaK;
    EditText inputTglAmbil;
    EditText inputTglTitip;
    ImageButton toolbar;
    DatePickerDialog.OnDateSetListener setListener;
    Spinner spinJenis;
    DatabaseHelper DatabaseHelper;
    public String sJenis;

    public static final int REQUEST_PICK_PHOTO = 1;
    int REQ_CAMERA = 101;
    byte[] imageBytes;
    File fileDirectory, imageFilename;
    ImageView imagecats;
    String strTimeStamp, strImageName, strFilePath, strEncodedImage;
    String strNamaP, strTelp, strNamaK, strBerat, strTglTitip, strTglAmbil, strPesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        // back button use return() method
        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comeback();
            }
        });

        DatabaseHelper = new DatabaseHelper(this);
        inputNamaP = findViewById(R.id.inputNamaP);
        inputNamaK = findViewById(R.id.inputNamaK);
        inputberat = findViewById(R.id.inputberat);
        inputNoTelp = findViewById(R.id.inputNoTelp);
        inputpesan = findViewById(R.id.inputpesan);
        inputTglTitip = findViewById(R.id.inputTglTitip);
        inputTglAmbil = findViewById(R.id.inputTglAmbil);
        spinJenis = findViewById(R.id.inputjenis);
        imagecats = findViewById(R.id.imagekocheng);

        id = getIntent().getLongExtra(com.example.kocheng.DatabaseHelper.row_id, 0);


        // disable keyboard when editText loose focus
        inputNamaP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        inputNamaK.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        inputberat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        inputNoTelp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        inputpesan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        // spinner jenis kucing
        final String[] jenis = {"Pilih Jenis","Anggora","Persia","Siam","Sphynx","Lainnya"};

        ArrayAdapter<CharSequence> adapterAsal = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, jenis);
        adapterAsal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinJenis.setAdapter(adapterAsal);

        spinJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sJenis = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // calendar declaration
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        // disable keyboard when editText clicked
        inputTglTitip.setShowSoftInputOnFocus(false);
        inputTglAmbil.setShowSoftInputOnFocus(false);

        // method to pop up the calender
        inputTglTitip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Form.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        inputTglTitip.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        inputTglAmbil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Form.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        inputTglAmbil.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            };
        });

        //image
        imagecats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(Form.this);
                pictureDialog.setTitle("Unggah Foto");
                String[] pictureDialogItems = {"Pilih foto dari galeri"};

                pictureDialog.setItems(pictureDialogItems,
                        (dialog, which) -> {
                            switch (which) {
                                case 0:
                                    Dexter.withContext(Form.this)
                                            .withPermissions(Manifest.permission.CAMERA,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                            .withListener(new MultiplePermissionsListener() {
                                                @Override
                                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                                    if (report.areAllPermissionsGranted()) {
                                                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                        startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
                                                    }
                                                }

                                                @Override
                                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                                    token.continuePermissionRequest();
                                                }
                                            }).check();
                                    break;
                            }
                        });
                pictureDialog.show();
            }
        });

        // saving
        Button save = findViewById(R.id.addsave);
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                strNamaP = inputNamaP.getText().toString();
                strTelp = inputNoTelp.getText().toString();
                strNamaK = inputNamaK.getText().toString();
                strBerat = inputberat.getText().toString();
                strTglTitip = inputTglTitip.getText().toString();
                strTglAmbil = inputTglAmbil.getText().toString();
                strPesan = inputpesan.getText().toString();

                ContentValues values = new ContentValues();

                values.put(com.example.kocheng.DatabaseHelper.namapemilik, strNamaP);
                values.put(com.example.kocheng.DatabaseHelper.nohp, strTelp);
                values.put(com.example.kocheng.DatabaseHelper.namakucing, strNamaK);
                values.put(com.example.kocheng.DatabaseHelper.beratkucing, strBerat);
                values.put(com.example.kocheng.DatabaseHelper.jeniskucing, sJenis);
                values.put(com.example.kocheng.DatabaseHelper.tanggaltitip, strTglTitip);
                values.put(com.example.kocheng.DatabaseHelper.tanggalngambil, strTglAmbil);
                values.put(com.example.kocheng.DatabaseHelper.foto, strFilePath);
                values.put(com.example.kocheng.DatabaseHelper.pesan, strPesan);


                if (strNamaP.trim().isEmpty() || strTelp.trim().isEmpty() || strNamaK.trim().isEmpty() ||
                        strBerat.trim().isEmpty() || strTglTitip.trim().isEmpty() || strTglAmbil.trim().isEmpty() ||
                        strFilePath == null || sJenis.equalsIgnoreCase("Pilih Jenis")) {
                    Toast.makeText(Form.this, "Mohon lengkapi form penitipan!", Toast.LENGTH_LONG).show();
                } else {
                    DatabaseHelper.insertData(values);

                    Toast.makeText(Form.this, "Penitipan berhasil!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Form.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    // gallery and camera
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CAMERA && resultCode == RESULT_OK) {
            convertImage(strFilePath);
        } else if (requestCode == REQUEST_PICK_PHOTO && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            assert selectedImage != null;
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String mediaPath = cursor.getString(columnIndex);
            cursor.close();
            strFilePath = mediaPath;
            convertImage(mediaPath);
        }
    }


    private void convertImage(String imageFilePath) {
        File imageFile = new File(imageFilePath);
        if (imageFile.exists()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            final Bitmap bitmap = BitmapFactory.decodeFile(strFilePath, options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            Glide.with(this)
                    .load(bitmap)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_imageup)
                    .into(imagecats);
            imageBytes = baos.toByteArray();
            strEncodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        }
    }

    public void comeback(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}