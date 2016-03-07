package com.github.dracute.okhttpwizard.lib;

import android.text.TextUtils;

import com.github.dracute.okhttpwizard.lib.param.ParamHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by DrAcute on 2015/12/21.
 */
public class UrlBuilder {

    boolean useQuerySymbol;
    String host;
    String url;
    String font;
    HashMap<String, String> params;

    public UrlBuilder() {
        params = new HashMap<>();
    }

    public UrlBuilder(UrlBuilder input) {
        this.useQuerySymbol = input.useQuerySymbol;
        this.host = input.host;
        this.url = input.url;
        this.params = new HashMap<>();
        this.params.putAll(input.params);
        this.font = input.font;
    }

    public UrlBuilder setUseQuerySymbol(boolean useQuerySymbol) {
        this.useQuerySymbol = useQuerySymbol;
        return this;
    }

    public UrlBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public UrlBuilder fullUrlInput() {
        this.host = null;
        return this;
    }

    public UrlBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public UrlBuilder setFont(String font) {
        this.font = font;
        return this;
    }

    public UrlBuilder add(String name, String value) {
        params.put(name, value);
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        try {
            if (!TextUtils.isEmpty(host)) {
                sb.append(host);
            }
            if (!TextUtils.isEmpty(url)) {
                sb.append(url);
            }
            Set<String> keySet = params.keySet();
            if (useQuerySymbol) {
                if (host.contains("?") || url.contains("?")) {
                    sb.append("&");
                } else {
                    sb.append("?");
                }
                for (String key : keySet) {
                    sb.append(URLEncoder.encode(key, font)).append("=").append(URLEncoder.encode(params.get(key), font)).append("&");
                }
            } else {
                sb.append("/");
                for (String key : keySet) {
                    sb.append(URLEncoder.encode(key, font)).append("/").append(URLEncoder.encode(params.get(key), font)).append("/");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
