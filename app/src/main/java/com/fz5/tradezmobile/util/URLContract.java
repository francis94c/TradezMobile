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

    }
}
