package com.github.dracute.okhttpwizard.lib;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by DrAcute on 2015/12/23.
 */
public abstract class WizardCall<T> implements Callback, ProgressCallback {

    protected Call call;
    protected Request request;
    protected WeakReference<WizardCallback<T>> callback;
    protected Handler mainLooperHandler = new Handler(Looper.getMainLooper());

    public WizardCall(WizardCallBuilder builder, String type) {
        this.call = builder.build(this, type);
        request = builder.request;
    }

    public void enqueue(WizardCallback<T> callback) {
        this.callback = new WeakReference<>(callback);
        this.call.enqueue(this);
    }

    public void enqueue() {
        this.call.enqueue(this);
    }

    protected void onPostResponse(int code, T t) {
        if (callback != null && callback.get() != null) {
            callback.get().onSuccess(code, t);
        }
    }

    protected void onPostFailure(Request request, Exception e) {
        if (callback != null && callback.get() != null) {
            callback.get().onFailure(request, e);
        }
    }

    protected void onPostProgress(long bytesRead, long contentLength, boolean done) {
        if (callback != null && callback.get() != null) {
            callback.get().onProgress(bytesRead, contentLength, done);
        }
    }

    public static class WizardCallBuilder {

        protected UrlBuilder urlBuilder;
        protected RequestBodyBuilder requestBodyBuilder;
        protected OkHttpClient okHttpClient;

        private Request request;
        private String method;

        public WizardCallBuilder(WizardConfig config, String method) {
            this.method = method;
            this.urlBuilder = new UrlBuilder(config.urlBuilder);
            this.requestBodyBuilder = new RequestBodyBuilder(config.requestBodyBuilder);
        }

        public final WizardCallBuilder setOkHttpClient(OkHttpClient client) {
            this.okHttpClient = client;
            return this;
        }

        public final UrlBuilder getUrlBuilder() {
            return this.urlBuilder;
        }

        public final RequestBodyBuilder getRequestBodyBuilder() {
            return this.requestBodyBuilder;
        }

        public Call build(WizardCall wizardCall, String type) {
            switch (type) {
                case "TEXT":
                default:
                    return buildText(wizardCall);
                case "UPLOAD":
                    return buildUpload(wizardCall);
                case "DOWNLOAD":
                    return buildDownload(wizardCall);
            }
        }

        private Call buildText(WizardCall wizardCall) {
            if (method.equals("Get")) {
                request = new Request.Builder().url(urlBuilder.build()).build();
            } else if (method.equals("Post")) {
                request = new Request.Builder().url(urlBuilder.build()).post(new ProgressRequestBody(requestBodyBuilder.build(), wizardCall)).build();
            }
            return okHttpClient.newCall(request);
        }

        private Call buildUpload(WizardCall wizardCall) {
            OkHttpClient clone = okHttpClient.clone();
            request = new Request.Builder().url(urlBuilder.build()).post(new ProgressRequestBody(requestBodyBuilder.build(), wizardCall)).build();
            return clone.newCall(request);
        }

        private Call buildDownload(final WizardCall wizardCall) {
            OkHttpClient clone = okHttpClient.clone();
            if (method.equals("Get")) {
                request = new Request.Builder().url(urlBuilder.build()).build();
            } else if (method.equals("Post")) {
                request = new Request.Builder().url(urlBuilder.build()).post(requestBodyBuilder.build()).build();
            }
            clone.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), wizardCall))
                            .build();
                }
            });
            return clone.newCall(request);
        }

    }


}
