package com.fz5.tradezmobile;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fz5.tradezmobile.util.URLContract.*;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class AdActivity extends AppCompatActivity {

    Intent intent;
    String phone = "";
    private ImageView.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), ImageViewActivity.class);
            intent.putExtra("url", view.getTag().toString());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        intent = getIntent();
        final LinearLayout slide = (LinearLayout) findViewById(R.id.imageSlide);
        JsonArrayRequest request = new JsonArrayRequest(URLSkeleton.getImagesUrl(intent.getIntExtra("ad_id", 0)), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int y = 0; y < response.length(); y++) {
                        ImageView imageView = new ImageView(getApplicationContext());
                        imageView.setTag(URLSkeleton.getImageUrl(response.getJSONObject(y).getString("name")));
                        Picasso.with(getApplicationContext()).load(URLSkeleton.getImageUrl(response.getJSONObject(y).getString("name"))).into(imageView);
                        imageView.setOnClickListener(listener);
                        slide.addView(imageView);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Pulling Ad Image", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getApplicationContext()).add(request);
        ((TextView) findViewById(R.id.titleTextView)).setText(String.format("Title: %s", intent.getStringExtra("title")));
        ((TextView) findViewById(R.id.locationTextView)).setText(String.format("Location: %s", intent.getStringExtra("location")));
        JsonObjectRequest userRequest = new JsonObjectRequest(URLSkeleton.getUserDetailsUrl(intent.getIntExtra("uid", 0)), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ((TextView) findViewById(R.id.fullNameTextView)).setText(String.format("Full Name: %s", response.getString("full_name")));
                    ((TextView) findViewById(R.id.emailTextView)).setText(String.format("Email: %s", response.getString("email")));
                    ((TextView) findViewById(R.id.phoneTextView)).setText(String.format("Phone: %s", response.getString("phone")));
                    phone = response.getString("phone");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(userRequest);
    }

    public void contact_Click(View view) {
        String message = "Good Day, Please i want to contact you regarding the ad you posted on Tradez title " + intent.getStringExtra("title") + ".";
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("vnd.android-dir/mms-sms");
        sendIntent.putExtra("address", phone);
        sendIntent.putExtra("sms_body", message);
        startActivity(sendIntent);
    }

}
