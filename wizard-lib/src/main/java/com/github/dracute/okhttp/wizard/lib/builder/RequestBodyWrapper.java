package com.github.dracute.okhttp.wizard.lib.builder;

import com.github.dracute.okhttp.wizard.lib.utils.Utils;
import com.github.dracute.okhttp.wizard.lib.wenum.RequestBodyType;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by DrAcute on 2016/1/23.
 */
public class RequestBodyWrapper {

    Map<String, String> requestParams;
    RequestBodyType bodyType = RequestBodyType.X_WWW_FORM_URLENCODED;

    public RequestBodyWrapper() {
        requestParams = new LinkedHashMap<>();
    }

    public RequestBody buildRequestBody() {
        RequestBody result;
        switch (bodyType) {
            case FORM_DATA:
            default:
                result = buildMultipart();
                break;
            case X_WWW_FORM_URLENCODED:
                result = buildFormEncoding();
        }
        return result;
    }

    private RequestBody buildMultipart() {
        MultipartBuilder multipartBuilder = new MultipartBuilder();
        multipartBuilder.type(MediaType.parse("multipart/form-data"));
        Set<String> keySet = requestParams.keySet();
        for (String key: keySet) {
            StringBuilder disposition = new StringBuilder("form-data; name=");
            Utils.appendQuotedString(disposition, key);
            multipartBuilder.addPart(Headers.of("Content-Disposition", disposition.toString()), RequestBody.create(null, requestParams.get(key)));
        }
        return multipartBuilder.build();
    }

    private RequestBody buildFormEncoding() {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Set<String> keySet = requestParams.keySet();
        for (String key: keySet) {
            builder.add(key, requestParams.get(key));
        }
        return builder.build();
    }
}
