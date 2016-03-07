package com.github.dracute.okhttp.wizard.lib.builder;

import android.util.Log;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Request;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by DrAcute on 2016/1/22.
 */
public class HeaderWrapper {
    Map<String, String> headers;

    public HeaderWrapper() {
        headers = new LinkedHashMap<>();
    }

    void addHeaders(Request.Builder builder) {
        Set<String> keys = headers.keySet();
        for (String key : keys) {
            Log.d("TAG", key + " = " + headers.get(key));
            builder.addHeader(key, headers.get(key));
        }
    }
}
