package com.example.examnote;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONObject;
import android.location.Location;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import android.os.Handler;
import android.os.Looper;

public class NoteEditActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;
    private NotesViewModel notesViewModel;
    private EditText titleEditText;
    private EditText contentEditText;
    private Button addLocationButton;
    private Button viewLocationButton;

    private ImageView imageView;

    private int noteId;
    private Note note;
    private FusedLocationProviderClient fusedLocationClient;
    private String imageUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        noteId = getIntent().getIntExtra("noteId", -1);
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        addLocationButton = findViewById(R.id.addLocationButton);
        viewLocationButton = findViewById(R.id.viewLocationButton);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        notesViewModel.getNoteById(noteId).observe(this, note -> {
            if (note != null) {
                this.note = note;
                titleEditText.setText(note.getTitle());
                contentEditText.setText(note.getContent());
                addLocationButton.setText(note.getLocation() != null ? "更新位置" : "保存位置");
                viewLocationButton.setVisibility(note.getLocation() != null ? View.VISIBLE : View.GONE);
                displayImage(note);
            }
        });

        addLocationButton.setOnClickListener(v -> addLocation());
        viewLocationButton.setOnClickListener(v -> viewLocation());

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveNote());

         imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(v -> pickImageFromGallery());


        requestPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveAndUpdate();
    }

    private void saveAndUpdate() {
        note.setTitle(titleEditText.getText().toString());
        note.setContent(contentEditText.getText().toString());
        note.setImageUri(this.imageUri);
        note.setUpdatedDate(System.currentTimeMillis());
        notesViewModel.updateNote(note);
        notesViewModel.getNoteById(noteId).observe(this, note -> {
            if (note != null) {
                this.note = note;
                viewLocationButton.setVisibility(note.getLocation() != null ? View.VISIBLE : View.GONE);
                displayImage(note);
            }
        });
    }


    private void addLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            try {
                                JSONObject json = new JSONObject();
                                json.put("latitude", location.getLatitude());
                                json.put("longitude", location.getLongitude());
                                String jsonString = json.toString();
                                note.setLocation(jsonString);
                                viewLocationButton.setVisibility(View.VISIBLE);
                                Toast.makeText(this, "笔记位置已保存", Toast.LENGTH_SHORT).show();
                                saveAndUpdate(); // 位置保存后立即触发保存
                            } catch (Exception e) {
                                e.printStackTrace();
//                                String[] a = {};
//                                String b = a[1];
                                Toast.makeText(this, "无法笔记保存位置，保存过程出错", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "无法笔记保存位置，定位失败了", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(this, e -> {
                        Toast.makeText(this, "无法笔记保存位置，定位失败了" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void viewLocation() {

        try {
            String jsonString = note.getLocation();
            JSONObject json = new JSONObject(jsonString);

            // 获取经纬度
            double latitude = json.getDouble("latitude");
            double longitude = json.getDouble("longitude");

            String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + "Label which you want" + ")";
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));

            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");

            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "查看笔记定位失败，因为无法获取笔记当前的位置信息" + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }
    private static final int PICK_IMAGE_REQUEST = 1;

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }



    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            Uri internalUri = copyImageToInternalStorage(imageUri);
            if (internalUri != null) {
                saveImageUriToDatabase(internalUri);
                saveAndUpdate();
            }
        }
    }
    private Uri copyImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            String fileName = getFileName(imageUri);
            if (fileName == null) {
                fileName = "image_" + UUID.randomUUID().toString() + ".jpg"; // Default name if extraction fails
            } else {
                fileName = UUID.randomUUID().toString() + "_" + fileName;
            }

            File directory = new File(getFilesDir(), "images");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, fileName);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
            return FileProvider.getUriForFile(NoteEditActivity.this, "com.example.examnote.fileprovider", file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    private void saveImageUriToDatabase(Uri imageUri) {
        this.imageUri = imageUri.toString();
    }

    private boolean isUriValid(Uri uri) {
        return true;
    }

    private void displayImage(Note note) {
        String imageUriString = note.getImageUri();
        if (imageUriString != null && !imageUriString.isEmpty()) {
            Uri imageUri = Uri.parse(imageUriString);
            if (isUriValid(imageUri)) {
                ImageView imageView = findViewById(R.id.imageView); // 假设你有一个ImageView来显示图片
                System.out.println("设置图片URL为"+imageUri);
                loadRoundedImage(imageUri);
            } else {
                // 显示默认图片或提示用户图片已被删除
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageResource(R.drawable.image_deleted); // 假设你有一张默认图片
                Toast.makeText(this, "图片已被删除", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadRoundedImage(Uri uri) {

        int radius = 16;
        RequestOptions requestOptions = new RequestOptions()
                .transform(new FitCenter(), new RoundedCorners(radius));

        Glide.with(this)
                .load(uri)
                .apply(requestOptions)
                .into(imageView);
    }

    private void saveNote() {
        saveAndUpdate();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Read External Storage permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            addLocation();
        }
    }

}