package com.example.kocheng;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.List;

public class EditActivity2 extends AppCompatActivity {

    DatabaseHelper helper;
    EditText inputNoTelp, inputpesan, inputberat, inputNamaP, inputNamaK, inputTglAmbil, inputTglTitip;
    Spinner SpJK;
    long id;

    String filePathLama;

    ImageView imagecats, imageAwalCats;

    Button save;

    ImageButton toolbar;

    DatePickerDialog.OnDateSetListener setListener;

    Spinner spinJenis;
    public String sJenis;
    String namaJenis;

    public static final int REQUEST_PICK_PHOTO = 1;
    int REQ_CAMERA = 101;
    byte[] imageBytes;
    File fileDirectory, imageFilename;

    String strTimeStamp, strImageName, strFilePath, strEncodedImage;

    String strNamaP, strTelp, strNamaK, strBerat, strTglTitip, strTglAmbil, strPesan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit2);

        inisialisasi();

        mainButton();

        ambilInputanUser();

        getData();

    }

    private void inisialisasi() {
        helper = new DatabaseHelper(this);
        inputNamaP = findViewById(R.id.inputNamaP);
        inputNamaK = findViewById(R.id.inputNamaK);
        inputberat = findViewById(R.id.inputberat);
        inputNoTelp = findViewById(R.id.inputNoTelp);
        inputpesan = findViewById(R.id.inputpesan);
        inputTglTitip = findViewById(R.id.inputTglTitip);
        inputTglAmbil = findViewById(R.id.inputTglAmbil);
        spinJenis = findViewById(R.id.inputjenis);
        imagecats = findViewById(R.id.imagekocheng);
        imageAwalCats = (ImageView) findViewById(R.id.image_awal);

        id = getIntent().getLongExtra(com.example.kocheng.DatabaseHelper.row_id, 0);


        // button
        save = findViewById(R.id.addsave);

        toolbar = findViewById(R.id.toolbar);

    }

    private void mainButton() {

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditActivity2.this, CatsActivity2.class);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strNamaP = inputNamaP.getText().toString().trim();
                strTelp = inputNoTelp.getText().toString().trim();
                strNamaK = inputNamaK.getText().toString().trim();
                strBerat = inputberat.getText().toString().trim();
                strTglTitip = inputTglTitip.getText().toString().trim();
                strTglAmbil = inputTglAmbil.getText().toString().trim();
                strPesan = inputpesan.getText().toString().trim();

                ContentValues values = new ContentValues();

                // ketika user tidak masukin gambar, maka yang akan dimasukkan adalah nilai dari filepath gambar yang lama
                if (strFilePath == null) {

                    values.put(com.example.kocheng.DatabaseHelper.namapemilik, strNamaP);
                    values.put(com.example.kocheng.DatabaseHelper.nohp, strTelp);
                    values.put(com.example.kocheng.DatabaseHelper.namakucing, strNamaK);
                    values.put(com.example.kocheng.DatabaseHelper.beratkucing, strBerat);
                    values.put(com.example.kocheng.DatabaseHelper.jeniskucing, sJenis);
                    values.put(com.example.kocheng.DatabaseHelper.tanggaltitip, strTglTitip);
                    values.put(com.example.kocheng.DatabaseHelper.tanggalngambil, strTglAmbil);
                    values.put(com.example.kocheng.DatabaseHelper.foto, filePathLama);
                    values.put(com.example.kocheng.DatabaseHelper.pesan, strPesan);

                } else {
                    // ketika user menambahkan gambar baru, maka yang akan kita masukin filepath gambar baru
                    values.put(com.example.kocheng.DatabaseHelper.namapemilik, strNamaP);
                    values.put(com.example.kocheng.DatabaseHelper.nohp, strTelp);
                    values.put(com.example.kocheng.DatabaseHelper.namakucing, strNamaK);
                    values.put(com.example.kocheng.DatabaseHelper.beratkucing, strBerat);
                    values.put(com.example.kocheng.DatabaseHelper.jeniskucing, sJenis);
                    values.put(com.example.kocheng.DatabaseHelper.tanggaltitip, strTglTitip);
                    values.put(com.example.kocheng.DatabaseHelper.tanggalngambil, strTglAmbil);
                    values.put(com.example.kocheng.DatabaseHelper.foto, strFilePath);
                    values.put(com.example.kocheng.DatabaseHelper.pesan, strPesan);
                }

                if (strNamaP.trim().isEmpty() || strTelp.trim().isEmpty() || strNamaK.trim().isEmpty() ||
                        strBerat.trim().isEmpty() || strTglTitip.trim().isEmpty() || strTglAmbil.trim().isEmpty() ||
                        sJenis.equalsIgnoreCase("Pilih Jenis")) {
                    Toast.makeText(EditActivity2.this, "Mohon lengkapi form penitipan!", Toast.LENGTH_LONG).show();
                } else {
                    helper.updateData(values, id);
                    Toast.makeText(EditActivity2.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void ambilInputanUser() {

        // disable keyboard when editText loose focus
        inputNamaP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        inputNamaK.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        inputberat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        inputNoTelp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
        inputpesan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });


        // spinner jenis kucing
        final String[] jenis = {"Pilih Jenis", "Anggora", "Persia", "Siam", "Sphynx", "Lainnya"};

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
                        EditActivity2.this, new DatePickerDialog.OnDateSetListener() {
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
                        EditActivity2.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        inputTglAmbil.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }

            ;
        });

        //image
        imagecats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(EditActivity2.this);
                pictureDialog.setTitle("Unggah Foto");
                String[] pictureDialogItems = {"Pilih foto dari galeri"};

                pictureDialog.setItems(pictureDialogItems,
                        (dialog, which) -> {
                            switch (which) {
                                case 0:
                                    Dexter.withContext(EditActivity2.this)
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

    }

    private void getData() {
        Cursor cursor = helper.oneData(id);
        if (cursor.moveToFirst()) {
            String namaP = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.namapemilik));
            String noTelp = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.nohp));
            String namaK = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.namakucing));
            String berat = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.beratkucing));
            String tanggalTitip = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.tanggaltitip));
            String tanggalAmbil = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.tanggalngambil));
            String imageAwalPath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.foto));
            String pesan = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.pesan));


            inputNamaP.setText(namaP);
            inputNoTelp.setText(noTelp);
            inputNamaK.setText(namaK);
            inputberat.setText(berat);
            inputTglTitip.setText(tanggalTitip);
            inputTglAmbil.setText(tanggalAmbil);
            imageAwalCats.setImageBitmap(BitmapFactory.decodeFile(imageAwalPath));
            inputpesan.setText(pesan);

            // atur nilai filePathLama
            filePathLama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.foto));
        }
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

}