package com.github.dracute.okhttpwizard.lib;

import android.util.Pair;

import com.github.dracute.okhttpwizard.lib.param.FileParam;
import com.github.dracute.okhttpwizard.lib.param.IParam;
import com.github.dracute.okhttpwizard.lib.param.ParamHelper;
import com.github.dracute.okhttpwizard.lib.param.TextParam;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by DrAcute on 2015/12/22.
 */
public class RequestBodyBuilder {

    private List<IParam> params;
    private String bodyType = "X_WWW_FORM_URLENCODED";

    public RequestBodyBuilder() {
        params = new ArrayList<>();
    }

    public RequestBodyBuilder(RequestBodyBuilder input) {
        this.params = new ArrayList<>();
        this.params.addAll(input.params);
        this.bodyType = input.bodyType;
    }

    public void setRequestBodyBuilder(RequestBodyBuilder input) {
        this.params.addAll(input.params);
        this.bodyType = input.bodyType;
    }

    public RequestBodyBuilder setBodyType(String bodyType) {
        this.bodyType = bodyType;
        return this;
    }

    public RequestBodyBuilder add(String name, String value) {
        this.params.add(new TextParam(name, value));
        return this;
    }

    public RequestBodyBuilder add(String name, File file, String fileName, String fileType) {
        this.bodyType = "FORM_DATA";
        this.params.add(new FileParam(name, file, fileName, fileType));
        return this;
    }

    public RequestBodyBuilder add(String name, File file) {
        this.bodyType = "FORM_DATA";
        this.params.add(new FileParam(name, file));
        return this;
    }

    public RequestBody build() {
        RequestBody result;
        switch (bodyType) {
            case "FORM_DATA":
            default:
                result = buildMultipart();
                break;
            case "X_WWW_FORM_URLENCODED":
                result = buildFormEncoding();
        }
        return result;
    }

    private RequestBody buildMultipart() {
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MediaType.parse("multipart/form-data"));
        for (IParam param: params) {
            Pair<Headers, RequestBody> pair = param.getMultiPart();
            multipartBuilder.addPart(pair.first, pair.second);
        }
        return multipartBuilder.build();
    }

    private RequestBody buildFormEncoding() {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (IParam param: params) {
            Pair<String, String> pair = param.getFormEncoding();
            builder.add(pair.first, pair.second);
        }
        return builder.build();
    }
}
