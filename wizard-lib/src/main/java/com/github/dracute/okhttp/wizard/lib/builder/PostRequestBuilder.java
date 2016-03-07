package com.github.dracute.okhttp.wizard.lib.builder;

import com.github.dracute.okhttp.wizard.lib.WizardConfig;
import com.github.dracute.okhttp.wizard.lib.wenum.RequestBodyType;
import com.squareup.okhttp.Request;

/**
 * Created by DrAcute on 2016/1/23.
 */
public class PostRequestBuilder extends RequestBuilder {

    RequestBodyWrapper requestBodyWrapper;

    public PostRequestBuilder() {
        headerWrapper = new HeaderWrapper();
        urlWrapper = new UrlWrapper();
        requestBodyWrapper = new RequestBodyWrapper();
    }

    public PostRequestBuilder(WizardConfig config) {
        headerWrapper = new HeaderWrapper();
        urlWrapper = new UrlWrapper();
        urlWrapper.host = config.getHost();
        urlWrapper.useQuerySymbol = config.isUseQuerySymbol();
        requestBodyWrapper = new RequestBodyWrapper();
    }

    public PostRequestBuilder setRequestBodyType(RequestBodyType requestBodyType) {
        requestBodyWrapper.bodyType = requestBodyType;
        return this;
    }

    public PostRequestBuilder addRequestParam(String name, String value) {
        requestBodyWrapper.requestParams.put(name, value);
        return this;
    }

    @Override
    public Request.Builder build() {
        Request.Builder builder = new Request.Builder().url(urlWrapper.toUrl());

        headerWrapper.addHeaders(builder);

        if (tag != null && tag.get() != null) {
            builder.tag(tag.get());
        }
        builder.post(addProgressRequestBody(requestBodyWrapper.buildRequestBody()));
        return builder;
    }

}
