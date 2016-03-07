package com.github.dracute.okhttp.wizard.lib.builder;

import com.github.dracute.okhttp.wizard.lib.callback.ProgressCallback;
import com.github.dracute.okhttp.wizard.lib.body.ProgressRequestBody;
import com.github.dracute.okhttp.wizard.lib.wenum.RequestBodyType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.lang.ref.WeakReference;

/**
 * Created by DrAcute on 2016/1/22.
 */
public abstract class RequestBuilder {

    protected HeaderWrapper headerWrapper;
    protected UrlWrapper urlWrapper;
    protected WeakReference<Object> tag;
    protected WeakReference<ProgressCallback> callback;

    protected String url;

    public RequestBuilder setHost(String host) {
        urlWrapper.host = host;
        return this;
    }

    public RequestBuilder setPath(String path) {
        urlWrapper.path = path;
        return this;
    }

    public RequestBuilder useQuerySymbol(boolean flag) {
        urlWrapper.useQuerySymbol = flag;
        return this;
    }

    public RequestBuilder useUrlEncoding(boolean flag) {
        urlWrapper.useQuerySymbol = flag;
        return this;
    }

    public RequestBuilder tag(Object tag) {
        this.tag = new WeakReference<>(tag);
        return this;
    }

    public RequestBuilder addHeader(String name, String value) {
        headerWrapper.headers.put(name, value);
        return this;
    }

    public RequestBuilder addUrlParam(String name, String value) {
        urlWrapper.urlParams.put(name, value);
        return this;
    }

    public RequestBuilder setRequestProgressCallback(ProgressCallback callback) {
        this.callback = new WeakReference<>(callback);
        return this;
    }

    public RequestBuilder setRequestBodyType(RequestBodyType requestBodyType) {
        throw new IllegalAccessError();
    }

    public RequestBuilder addRequestParam(String name, String value) {
        throw new IllegalAccessError();
    }

    public final RequestBody addProgressRequestBody(RequestBody requestBody) {
        if (callback != null && callback.get() != null) {
            return new ProgressRequestBody(requestBody, callback.get());
        }
        return requestBody;
    }

    public abstract Request.Builder build();

}
