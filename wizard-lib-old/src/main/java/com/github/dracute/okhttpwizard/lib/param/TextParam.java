package com.github.dracute.okhttpwizard.lib.param;

import android.util.Pair;

import com.github.dracute.okhttpwizard.lib.utils.Utils;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.RequestBody;

/**
 * Created by DrAcute on 2015/12/22.
 */
public class TextParam implements IParam {

    String name;
    String value;

    public TextParam(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Pair<Headers, RequestBody> getMultiPart() {
        if (name == null) {
            throw new NullPointerException("name == null");
        }
        StringBuilder disposition = new StringBuilder("form-data; name=");
        Utils.appendQuotedString(disposition, name);

        return new Pair<>(Headers.of("Content-Disposition", disposition.toString()), RequestBody.create(null, value));
    }

    @Override
    public Pair<String, String> getFormEncoding() {
        return new Pair<>(name, value);
    }
}
