package com.fz5.tradezmobile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fz5.tradezmobile.adapters.SpinnerAdapterView;
import com.fz5.tradezmobile.model.Category;
import com.fz5.tradezmobile.model.SharedPreferencesHelper.*;
import com.fz5.tradezmobile.model.SubCategory;
import com.fz5.tradezmobile.util.MultipartUtility;
import com.fz5.tradezmobile.util.URLContract.*;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CreateAdActivity extends AppCompatActivity {

    private String url;
    private Uri imageUri;
    private Spinner categorySpinner;
    private Spinner subCategorySpinner;
    private ArrayList<Category> categories;
    private ArrayList<SubCategory> subCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad);
        //url = "http://" + getResources().getString(R.string.ip) + "/tradez/index.php/TradezAPI/addImage/1/5";
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        subCategorySpinner = (Spinner) findViewById(R.id.subCategorySpinner);
        categories = Configs.getCategories(this);
        subCategories = Configs.getSubCategories(this);
        categorySpinner.setAdapter(new SpinnerAdapterView(this, getCategoriesList(), "Category"));
        subCategorySpinner.setAdapter(new SpinnerAdapterView(this, getSubCategoriesList(), "Sub Category"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), 52);
            }
        }
    }

    private ArrayList<String> getSubCategoriesList() {
        ArrayList<String> subCategories = new ArrayList<>();
        for (SubCategory subCategory: this.subCategories) {
            subCategories.add(subCategory.getName());
        }
        return subCategories;
    }

    private ArrayList<String> getCategoriesList() {
        ArrayList<String> categories = new ArrayList<>();
        for (Category category: this.categories) {
            categories.add(category.getName());
        }
        return categories;
    }

    public void createAdButton_Click(View view) {
        String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
        String location = ((EditText) findViewById(R.id.locationEditText)).getText().toString();
        int category = categories.get(categorySpinner.getSelectedItemPosition()).getId();
        int subCategory = subCategories.get(subCategorySpinner.getSelectedItemPosition()).getId();
        if (!title.equals("") && !location.equals("")) {
            JsonObjectRequest request = new JsonObjectRequest(URLSkeleton.getCreateAdUrl(
                    Configs.getUserId(this), title, location, category, subCategory), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(getApplicationContext(), "Ad Created Successfully, Start adding Media.", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error Creating Ad", Toast.LENGTH_SHORT).show();
                }
            });
            Volley.newRequestQueue(this).add(request);
        } else {
            Toast.makeText(getApplicationContext(), "Missing Fields.", Toast.LENGTH_LONG).show();
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
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 52);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 20);
        }
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
