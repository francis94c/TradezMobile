package com.fz5.tradezmobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fz5.tradezmobile.util.MultipartUtility;

import java.io.File;
import java.io.IOException;

public class CreateAdActivity extends AppCompatActivity {

    private String url;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad);
        url = "http://" + getResources().getString(R.string.ip) + "/tradez/index.php/TradezAPI/addImage/1/5";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 52);
            }
        }
    }

    public void uploadImage(View view) {
        boolean permission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 52);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 20);
        }

        /*try{
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-length", null);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 52) {
            if (data != null) {
                imageUri = data.getData();
                new Thread(new UploadRunnable()).start();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private class UploadRunnable implements Runnable {

        @Override
        public void run() {
            String charset = "UTF-8";
            String requestURL = url;
            String file_path = "";
            file_path = getPath(imageUri);
            try {
                MultipartUtility multipart = new MultipartUtility(requestURL, charset);
                multipart.addFilePart("ad_image", new File(file_path));
                String response = multipart.finish(); // response from server.
                Log.e("TRADEZ", response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
