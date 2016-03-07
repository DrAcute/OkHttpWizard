package com.github.dracute.okhttpwizard.lib;

import com.squareup.okhttp.Request;

/**
 * Created by DrAcute on 2015/12/23.
 */
public interface WizardCallback<T> extends ProgressCallback{
    void onSuccess(int code, T t);
    void onFailure(Request request, Exception e);
}
