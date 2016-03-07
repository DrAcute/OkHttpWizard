package com.github.dracute.okhttp.wizard.lib.controller;

import android.util.Log;

import com.github.dracute.okhttp.wizard.lib.body.ProgressResponseBody;
import com.github.dracute.okhttp.wizard.lib.handler.WizardHandler;
import com.github.dracute.okhttp.wizard.lib.parser.FileParser;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

/**
 * Created by DrAcute on 2016/1/29.
 */
public class DownloadController<T> extends Controller<T>{

    private WizardHandler<T> handler;

    protected DownloadController(Builder<T> builder) {
        super(builder);
        handler = new WizardHandler<>(callback, parser);
    }

    @Override
    public OkHttpClient getClient() {
        OkHttpClient clone = client.clone();
        clone.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), handler))
                        .build();
            }
        });
        return clone;
    }

    @Override
    public WizardHandler<T> getHandler() {
        return handler;
    }

    @Override
    protected Request buildRequest() {
        if (parser != null && parser instanceof FileParser && ((FileParser) parser).isAutoResumeAndFileExist()) {
            Log.d("Tag", ((FileParser) parser).getHasDownload() + "");
            requestBuilder.addHeader("RANGE", "bytes=" + ((FileParser) parser).getHasDownload() + "-");
        } else {
            requestBuilder.addHeader("RANGE", "bytes=0-");
        }
        return requestBuilder.build().build();
    }
}
