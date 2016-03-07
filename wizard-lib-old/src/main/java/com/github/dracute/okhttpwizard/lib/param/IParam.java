package com.github.dracute.okhttpwizard.lib.param;

import android.util.Pair;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.RequestBody;

/**
 * Created by DrAcute on 2015/12/22.
 */
public interface IParam {

    Pair<Headers, RequestBody> getMultiPart();
    Pair<String, String> getFormEncoding();
}
