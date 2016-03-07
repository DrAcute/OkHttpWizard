package com.github.dracute.okhttp.wizard.lib.parser;

import android.util.Pair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by DrAcute on 2016/1/23.
 */
public class JsonParser<T> implements IParser<T> {

    private TypeReference<T> typeReference;

    public JsonParser(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
    }

    @Override
    public Pair<Integer, T> parser(Response response) throws IOException {
        int code = response.code();
        T result = null;
        if (response.isSuccessful()) {
            result = JSON.parseObject(response.body().string(), typeReference);
        }
        return new Pair<>(code, result);
    }

    @Override
    public ProgressEntity progress(long bytesRead, long contentLength, boolean done) {
        return null;
    }
}
