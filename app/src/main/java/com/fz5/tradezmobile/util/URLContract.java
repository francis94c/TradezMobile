package com.fz5.tradezmobile.util;

import android.content.Context;

/**
 * Created by Francis Ilechukwu 01/04/2018.
 */

public class URLContract {

    public static class URLSkeleton {

        static String server = "nfcsfutoalumni.org";

        public static String getSignUpUrl() {
            return "http://" + server + "/tradez/index.php/TradezAPI/signUp/";
        }

        public static String getValidateUserUrl(String email, String password) {
            return "http://" + server + "/tradez/index.php/TradezAPI/validateUser/" + email + "/" + password + "/";
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

        public static String getUploadImageUrl(int adId) {
            return "http://" + server + "/tradez/index.php/TradezAPI/addImage/" + adId;
        }

        public static String getUploadVideoUrl(int adId) {
            return "http://" + server + "/tradez/index.php/TradezAPI/addVideo/" + adId;
        }

    }
}
