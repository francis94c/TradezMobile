package com.fz5.tradezmobile.model;

import android.content.Context;

import com.fz5.tradezmobile.R;
import com.fz5.tradezmobile.model.SharedPreferencesContract.*;

/**
 * Created by Francis Ilechukwu 01/04/2018.
 */

public class SharedPreferencesHelper {
    public static class Configs {
        public static int getUserId(Context context) {
            return context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).getInt(Keys.USER_ID, -1);
        }
    }
}
