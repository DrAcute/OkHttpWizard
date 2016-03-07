package com.github.dracute.okhttpwizard.lib.call;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.github.dracute.okhttpwizard.lib.WizardCall;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by DrAcute on 2015/12/23.
 */
public class JsonWizardCall<T> extends WizardCall<T> {

    private TypeReference<T> typeReference;

    public JsonWizardCall(WizardCallBuilder builder) {
        super(builder, "TEXT");
    }

    public final void setTypeReference(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public void onResponse(final Response response) throws IOException {
        try {
            final String body = response.body().string();
            final T t = onParse(body);
            mainLooperHandler.post(new Runnable() {
                @Override
                public void run() {
                    onPostResponse(response.code(), t);
                }
            });
        } catch (final JSONException e) {
            mainLooperHandler.post(new Runnable() {
                @Override
                public void run() {
                    onPostFailure(response.request(), e);
                }
            });
        }

    }

    @Override
    public void onFailure(final Request request, final IOException e) {
        mainLooperHandler.post(new Runnable() {
            @Override
            public void run() {
                onPostFailure(request, e);
            }
        });
    }

    @Override
    public void onProgress(final long bytesRead, final long contentLength, final boolean done) {
        mainLooperHandler.post(new Runnable() {
            @Override
            public void run() {
                onPostProgress(bytesRead, contentLength, done);
            }
        });
    }

    protected TypeReference<T> getTypeReference() {
        return typeReference;
    }

    protected T onParse(String s) {
        return JSON.parseObject(s, getTypeReference());
    }

}
