package com.github.dracute.okhttp.wizard.lib.controller;

import com.github.dracute.okhttp.wizard.lib.handler.WizardHandler;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * Created by DrAcute on 2016/1/25.
 */
public class JsonController<T> extends Controller<T> {

    protected JsonController(Builder<T> builder) {
        super(builder);
    }

    @Override
    public OkHttpClient getClient() {
        return client;
    }

    @Override
    public WizardHandler<T> getHandler() {
        return new WizardHandler<>(callback, parser);
    }

    @Override
    public Request buildRequest() {
        return requestBuilder.build().build();
    }
}
