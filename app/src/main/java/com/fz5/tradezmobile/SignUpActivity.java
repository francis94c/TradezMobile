package com.fz5.tradezmobile;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.toolbox.Volley;
import com.fz5.tradezmobile.util.CustomRequest;
import com.fz5.tradezmobile.util.URLContract.URLSkeleton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ((EditText) findViewById(R.id.reTypePasswordEditText)).setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_GO) {
                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    signUpButton_Click(null);
                    return true;
                }
                return false;
            }
        });
    }

    public void signUpButton_Click(View view) {
        final String fullName = ((EditText) findViewById(R.id.fullNameEditText)).getText().toString();
        final String userName = ((EditText) findViewById(R.id.userNameEditText)).getText().toString();
        final String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        final String phone = ((EditText) findViewById(R.id.phoneEditText)).getText().toString();
        final String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
        final String reTypePassword = ((EditText) findViewById(R.id.reTypePasswordEditText)).getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("full_name", fullName);
        params.put("user_name", userName);
        params.put("email", email);
        params.put("phone", phone);
        params.put("password", password);
        if (!userName.equals("") && !phone.equals("") && !email.equals("") && !fullName.equals("")) {
            if (!password.equals("") && password.equals(reTypePassword)) {
                CustomRequest customRequest = new CustomRequest(Request.Method.POST, URLSkeleton.getSignUpUrl(), params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("response") == 1) {
                                Toast.makeText(getApplicationContext(), "Sign up was successful", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error Signing up", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "User Already Exists.", Toast.LENGTH_LONG).show();
                    }
                });
                Volley.newRequestQueue(this).add(customRequest);
            } else {
                Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Empty Fields", Toast.LENGTH_SHORT).show();
        }

    }

}
