package com.fz5.tradezmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fz5.tradezmobile.model.SharedPreferencesHelper.*;
import com.fz5.tradezmobile.OnActivityResultContract.*;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Configs.getUserId(this) != -1) {
            startActivityForResult(new Intent(this, MainActivity.class), RequestCodes.MAIN_ACTIVITY);
        } else {
            startActivityForResult(new Intent(this, LoginActivity.class), RequestCodes.LOGIN_ACTIVITY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.MAIN_ACTIVITY || requestCode == RequestCodes.LOGIN_ACTIVITY) {
            finish();
        }
    }
}
