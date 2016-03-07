package com.github.dracute.okhttpwizard.lib.call;

import com.github.dracute.okhttpwizard.lib.WizardCall;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by DrAcute on 2016/1/7.
 */
public class NoResponseWizardCall extends WizardCall {

    public NoResponseWizardCall(WizardCallBuilder builder) {
        super(builder, "TEXT");
    }

    @Override
    public void onFailure(Request request, IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(Response response) throws IOException {

    }

    @Override
    public void onProgress(long bytesRead, long contentLength, boolean done) {

    }
}
