package com.github.dracute.okhttp.wizard.lib.builder;

import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by DrAcute on 2016/1/22.
 */
public class UrlWrapper {
    boolean useQuerySymbol;
    boolean urlEncoding;
    String host;
    String path;
    Map<String, String> urlParams;

    public UrlWrapper() {
        urlParams = new LinkedHashMap<>();
    }

    String toUrl() {
        StringBuilder path_origin = new StringBuilder();
        StringBuilder path_add = new StringBuilder();
        try {
            if (!TextUtils.isEmpty(host)) {
                path_origin.append(host);
            }
            if (!TextUtils.isEmpty(path)) {
                path_origin.append(path);
            }
            Set<String> keySet = urlParams.keySet();
            if (urlParams.size() > 0) {
                if (useQuerySymbol) {
                    if (path_origin.indexOf("?") > 0) {
                        path_add.append("&");
                    } else {
                        path_add.append("?");
                    }
                    for (String key : keySet) {
                        if (key.startsWith("{") && key.endsWith("}")) {
                            replaceSymbol(path_origin, key, URLEncoder.encode(urlParams.get(key), "utf-8"));
                        } else {
                            path_add.append(URLEncoder.encode(key, "utf-8")).append("=").append(URLEncoder.encode(urlParams.get(key), "utf-8")).append("&");
                        }
                    }
                } else {
                    path_add.append("/");
                    for (String key : keySet) {
                        if (key.startsWith("{") && key.endsWith("}")) {
                            replaceSymbol(path_origin, key, URLEncoder.encode(urlParams.get(key), "utf-8"));
                        } else {
                            path_add.append(URLEncoder.encode(key, "utf-8")).append("/").append(URLEncoder.encode(urlParams.get(key), "utf-8")).append("/");
                        }
                    }
                }
                path_add.deleteCharAt(path_add.length() - 1);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return path_origin.append(path_add).toString();
    }

    private void replaceSymbol(StringBuilder path_origin, String symbol, String value) {
        int index;
        while ((index = path_origin.indexOf(symbol)) >= 0) {
            path_origin.replace(index, index + symbol.length(), value);
        }
    }
}
