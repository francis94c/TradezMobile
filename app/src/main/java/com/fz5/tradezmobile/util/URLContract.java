package com.fz5.tradezmobile.util;

import android.content.Context;

/**
 * Created by Francis Ilechukwu 01/04/2018.
 */

public class URLContract {

    public static class URLSkeleton {

        static String server = "192.168.0.3";

        public static String getSignUpUrl() {
            return "http://" + server + "/tradez/index.php/TradezAPI/signUp/";
        }

        public static String getValidateUserUrl() {
            return "http://" + server + "/tradez/index.php/TradezAPI/validateUser/";
        }

        public static String getCreateAdUrl(int userId, String title, String location, int category, int subCategory) {
            return "http://" + server + "/tradez/index.php/TradezAPI/createAd/" + userId + "/" + title + "/" + location + "/" + category + "/" + subCategory + "/";
        }

        public static String getCategoriesUrl() {
            return "http://" + server + "/tradez/index.php/TradezAPI/getCategories/";
        }

        public static String getSubCategoriesUrl() {
            return "http://" + server + "/tradez/index.php/TradezAPI/getSubCategories/";
        }

    }
}
