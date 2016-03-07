package com.github.dracute.okhttp.wizard.compiler.utils;

/**
 * Created by DrAcute on 2016/1/5.
 */
public class TextUtils {
    public static String useDefaultIfEmpty(String value, String defaultValue) {
        if (value == null || value.length() == 0) {
            return defaultValue;
        }
        return value;
    }

    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
}
