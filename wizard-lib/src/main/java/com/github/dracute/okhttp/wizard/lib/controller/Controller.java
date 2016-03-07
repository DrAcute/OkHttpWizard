package com.github.dracute.okhttp.wizard.lib.controller;

import com.github.dracute.okhttp.wizard.lib.builder.RequestBuilder;
import com.github.dracute.okhttp.wizard.lib.callback.WizardCallback;
import com.github.dracute.okhttp.wizard.lib.handler.WizardHandler;
import com.github.dracute.okhttp.wizard.lib.parser.FileParser;
import com.github.dracute.okhttp.wizard.lib.parser.IParser;
import com.github.dracute.okhttp.wizard.lib.parser.JsonParser;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.lang.ref.WeakReference;

/**
 * Created by DrAcute on 2016/1/23.
 */
public abstract class Controller<T> {

    protected final OkHttpClient client;
    protected final IParser<T> parser;
    protected final RequestBuilder requestBuilder;
    protected final WeakReference<WizardCallback<T>> callback;

    protected Controller(Builder<T> builder) {
        this.client = builder.client;
        this.parser = builder.parser;
        this.requestBuilder = builder.requestBuilder;
        this.callback = builder.callback;
    }

    public abstract OkHttpClient getClient();
    public abstract WizardHandler<T> getHandler();
    protected abstract Request buildRequest();

    public final Request getRequest() {
        if (requestBuilder == null) {
            throw new IllegalArgumentException("Set RequestBuilder before calling build()");
        }
        return buildRequest();
    }

    public static class Builder<T> {

        private OkHttpClient client;
        private IParser<T> parser;
        private RequestBuilder requestBuilder;
        private WeakReference<WizardCallback<T>> callback;

        public Builder<T> setClient(OkHttpClient client) {
            this.client = client;
            return this;
        }

        public Builder<T> setTag(Object tag) {
            if (requestBuilder != null) {
                requestBuilder.tag(tag);
            }
            return this;
        }

        public Builder<T> setParser(IParser<T> parser) {
            this.parser = parser;
            return this;
        }

        public Builder<T> setRequestBuilder(RequestBuilder requestBuilder) {
            this.requestBuilder = requestBuilder;
            return this;
        }

        public Builder<T> setCallback(WizardCallback<T> callback) {
            if (callback != null) {
                this.callback = new WeakReference<>(callback);
            }
            return this;
        }

        public Controller<T> build() {
            if (parser != null) {
                if (parser instanceof JsonParser) {
                    return new JsonController<>(this);
                } else if (parser instanceof FileParser){
                    return new DownloadController<>(this);
                }
            }
            return null;
        }
    }


}
