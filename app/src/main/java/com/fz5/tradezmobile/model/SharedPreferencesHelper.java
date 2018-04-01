package com.fz5.tradezmobile.model;

import android.content.Context;
import android.content.SharedPreferences;

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

        public static void setUserId(Context context, int userId) {
            SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).edit();
            editor.putInt(Keys.USER_ID, userId);
            editor.apply();
        }

    }
}
