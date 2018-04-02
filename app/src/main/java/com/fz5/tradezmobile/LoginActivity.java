package com.fz5.tradezmobile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fz5.tradezmobile.OnActivityResultContract.*;
import com.android.volley.toolbox.Volley;
import com.fz5.tradezmobile.model.SharedPreferencesHelper.*;
import com.fz5.tradezmobile.util.CustomRequest;
import com.fz5.tradezmobile.util.URLContract.*;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ((EditText) findViewById(R.id.passwordEditText)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    loginButton_Click(null);
                    return true;
                }
                return false;
            }
        });
    }

    public void loginButton_Click(View view) {
        final String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        final String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, URLSkeleton.getValidateUserUrl(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("user_id") != -1) {
                        Configs.setUserId(getApplicationContext(), response.getInt("user_id"));
                        Configs.setFullName(getApplicationContext(), response.getJSONObject("user").getString("full_name"));
                        Configs.setPhone(getApplicationContext(), response.getJSONObject("user").getString("phone"));
                        Configs.setEmail(getApplicationContext(), response.getJSONObject("user").getString("email"));
                        startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), RequestCodes.MAIN_ACTIVITY);
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Email and or Password.", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error response from server.", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(customRequest);
    }

    public void signUpButton_Click(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

}
