package com.github.dracute.okhttp.wizard.lib.builder;

import android.util.Log;

import com.github.dracute.okhttp.wizard.lib.WizardConfig;
import com.squareup.okhttp.Request;

/**
 * Created by DrAcute on 2016/1/23.
 */
public class GetRequestBuilder extends RequestBuilder {

    public GetRequestBuilder() {
        urlWrapper = new UrlWrapper();
        headerWrapper = new HeaderWrapper();
    }

    public GetRequestBuilder(WizardConfig config) {
        urlWrapper = new UrlWrapper();
        urlWrapper.host = config.getHost();
        urlWrapper.useQuerySymbol = config.isUseQuerySymbol();
        headerWrapper = new HeaderWrapper();
    }

    @Override
    public Request.Builder build() {
        Request.Builder builder = new Request.Builder().url(urlWrapper.toUrl());

        headerWrapper.addHeaders(builder);

        if (tag != null && tag.get() != null) {
            builder.tag(tag.get());
        }
        return builder;
    }

}
