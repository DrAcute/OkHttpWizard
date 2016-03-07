package com.github.dracute.okhttp.wizard.lib;

import android.util.Log;

import com.github.dracute.okhttp.wizard.lib.builder.RequestBuilder;
import com.github.dracute.okhttp.wizard.lib.callback.SimpleOkHttpCallback;
import com.github.dracute.okhttp.wizard.lib.callback.WizardCallback;
import com.github.dracute.okhttp.wizard.lib.controller.Controller;
import com.github.dracute.okhttp.wizard.lib.parser.IParser;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * Created by DrAcute on 2016/1/23.
 */
public class WizardCall<T> {

    Controller.Builder<T> controllerBuilder;

    public WizardCall() {
        controllerBuilder = new Controller.Builder<>();
    }

    public final WizardCall<T> setClient(OkHttpClient client) {
        controllerBuilder.setClient(client);
        return this;
    }

    public final WizardCall<T> setRequest(RequestBuilder builder) {
        controllerBuilder.setRequestBuilder(builder);
        return this;
    }

    public final WizardCall<T> setParser(IParser<T> parser) {
        controllerBuilder.setParser(parser);
        return this;
    }

    public final WizardCall<T> setTag(Object tag) {
        controllerBuilder.setTag(tag);
        return this;
    }

    public final Call enqueue(WizardCallback<T> callback) {
        controllerBuilder.setCallback(callback);
        Controller<T> controller = controllerBuilder.build();
        Request request = controller.getRequest();
        Log.d("Tag", request.urlString());
        Call call = controller.getClient().newCall(request);
        call.enqueue(controller.getHandler());
        return call;
    }

    public final Call enqueue() {
        Controller<T> controller = controllerBuilder.build();
        Request request = controller.getRequest();
        Log.d("Tag", request.headers().toString());
        Call call = controller.getClient().newCall(request);
        call.enqueue(new SimpleOkHttpCallback());
        return call;
    }

    public void setCustomParser() {

    }
}
