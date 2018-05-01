package com.fz5.tradezmobile.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.fz5.tradezmobile.R;
import com.fz5.tradezmobile.model.SharedPreferencesContract.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Francis Ilechukwu 01/04/2018.
 */

public class SharedPreferencesHelper {
    public static class Configs {

        public static int getUserId(Context context) {
            return context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).getInt(Keys.USER_ID, -1);
        }

        public static String getFullName(Context context) {
            return context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).getString(Keys.FULL_NAME, "");
        }

        public static String getPhone(Context context) {
            return context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).getString(Keys.PHONE, "");
        }

        public static void setUserId(Context context, int userId) {
            SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).edit();
            editor.putInt(Keys.USER_ID, userId);
            editor.apply();
        }

        public static void setEmail(Context context, String email) {
            SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).edit();
            editor.putString(Keys.EMAIL, email);
            editor.apply();
        }

        public static void setFullName(Context context, String fullName) {
            SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).edit();
            editor.putString(Keys.FULL_NAME, fullName);
            editor.apply();
        }

        public static void setPhone(Context context, String phone) {
            SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).edit();
            editor.putString(Keys.PHONE, phone);
            editor.apply();
        }

        public static void setCategories(Context context, String json) {
            SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).edit();
            editor.putString(Keys.CATEGORIES, json);
            editor.apply();
        }

        public static void setSubCategories(Context context, String json) {
            SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).edit();
            editor.putString(Keys.SUB_CATEGORIES, json);
            editor.apply();
        }

        public static ArrayList<Category> getCategories(Context context) {
            try {
                JSONArray array = new JSONArray(context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).getString(Keys.CATEGORIES, ""));
                ArrayList<Category> categories = new ArrayList<>();
                JSONObject object;
                for (int x = 0; x < array.length(); x ++) {
                    object = array.getJSONObject(x);
                    categories.add(new Category(object.getInt("id"), object.getString("name")));
                }
                return categories;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static ArrayList<SubCategory> getSubCategories(Context context) {
            try {
                JSONArray array = new JSONArray(context.getSharedPreferences(context.getResources().getString(R.string.app_name), 0).getString(Keys.SUB_CATEGORIES, ""));
                ArrayList<SubCategory> subCategories = new ArrayList<>();
                JSONObject object;
                for (int x = 0; x < array.length(); x ++) {
                    object = array.getJSONObject(x);
                    subCategories.add(new SubCategory(object.getInt("id"), object.getInt("category_id"), object.getString("name")));
                }
                return subCategories;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
