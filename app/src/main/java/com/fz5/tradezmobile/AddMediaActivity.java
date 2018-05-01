package com.fz5.tradezmobile;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.fz5.cropimage.CropImage;
import com.fz5.tradezmobile.OnActivityResultContract.*;
import com.fz5.tradezmobile.abstractions.PassportManager;
import com.fz5.tradezmobile.adapters.MediaAdapter;
import com.fz5.tradezmobile.model.Image;
import com.fz5.tradezmobile.model.LOCATION;
import com.fz5.tradezmobile.model.MEDIA_TYPE;
import com.fz5.tradezmobile.model.Video;
import com.fz5.tradezmobile.util.MultipartUtility;
import com.fz5.tradezmobile.util.URLContract.*;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class AddMediaActivity extends AppCompatActivity {

    private int adId;
    private Uri videoUri;
    private Handler handler;
    private MediaAdapter adapter;
    private Uri passportUri;
    private String passportPath;
    private Bitmap passport;
    private int EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 27;
    private Bitmap bufferBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_media);
        adId = getIntent().getIntExtra("ad_id", 0);
        handler = new Handler();
        adapter = new MediaAdapter();
        ((GridView) findViewById(R.id.mediaGridView)).setAdapter(adapter);
        if (!getIntent().getBooleanExtra("new", false)) {
            //TODO: create media objects and use picasso to load images.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_media_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addImage:
                try {
                    Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Object[] object = new PassportManager(this).createImageFile();
                        File photoFile = (File) object[0];
                        if (photoFile != null) {
                            passportUri = FileProvider.getUriForFile(this, "com.fz5.tradezmobile", photoFile);
                            passportPath = (String) object[1];
                            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, passportUri);
                        } else {
                            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Error Readying Capture Intent.", Snackbar.LENGTH_LONG);
                            snackBar.setAction("Ok", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackBar.dismiss();
                                }
                            });
                        }
                    }
                    startActivityForResult(captureIntent, RequestCodes.CAMERA_CAPTURE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Your Mobile Device Doesn't Support Image Capture.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.CAMERA_CAPTURE && resultCode == RESULT_OK) {
            boolean permission = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            }
            if (permission) {
                attemptCrop();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
            }
        } else if (requestCode == RequestCodes.PICTURE_CROP && resultCode == RESULT_OK) {
            String path = data.getStringExtra(CropImage.IMAGE_PATH);
            if (path == null) {
                return;
            }
            try {
                passport = MediaStore.Images.Media.getBitmap(this.getContentResolver(), passportUri);
                bufferBitmap = passport;
                new Thread(new UploadRunnable()).start();
                // Image image = new Image();
                //image.setBitmap(passport);
                 //image.setLocation(LOCATION.OFFLINE);
                //adapter.addImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void attemptCrop() {
        final ContentResolver cr = getContentResolver();
        final String[] p1 = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATE_TAKEN
        };
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Cursor c1 = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, p1, null, null, p1[1] + " DESC");
            if (c1.moveToFirst()) {
                String uriString = "content://media/external/images/media/" + c1.getInt(0);
                passportUri = Uri.parse(uriString);
                c1.close();
                performCrop();
            }
        } else {
            performCrop();
        }
    }

    private void performCrop() {
        Intent intent = new Intent(this, CropImage.class);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            passportPath = getPath(passportUri);
        }
        intent.putExtra(CropImage.IMAGE_PATH, passportPath);
        intent.putExtra(CropImage.SCALE, false);
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);
        intent.putExtra(CropImage.OUTPUT_Y, 200);
        intent.putExtra(CropImage.OUTPUT_X, 200);
        intent.putExtra(CropImage.SCALE_UP_IF_NEEDED, false);
        startActivityForResult(intent, RequestCodes.PICTURE_CROP);
    }

    private class UploadRunnable implements Runnable {

        @Override
        public void run() {
            String charset = "UTF-8";
            final String file_path = passportPath;
            try {
                MultipartUtility multipart = new MultipartUtility(URLSkeleton.getUploadImageUrl(adId), charset);
                multipart.addFilePart("ad_image", new File(file_path));
                String response = multipart.finish(); // response from server.
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getInt("success") == 1) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                Image image = new Image();
                                image.setBitmap(bufferBitmap);
                                image.setLocation(LOCATION.OFFLINE);
                                adapter.addImage(image);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getApplicationContext(), "Error Uploading Image", Toast.LENGTH_SHORT).show();
                                Image image = new Image();
                                image.setBitmap(bufferBitmap);
                                image.setLocation(LOCATION.OFFLINE);
                                adapter.addImage(image);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
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

}
