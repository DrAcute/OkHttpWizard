package com.github.dracute.okhttp.wizard.lib.parser;

import android.util.Pair;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by DrAcute on 2016/1/23.
 */
public interface IParser<T> {
    Pair<Integer, T> parser(Response response) throws IOException;
    ProgressEntity progress(long bytesRead, long contentLength, boolean done);

    class ProgressEntity {
        public ProgressEntity(long bytesRead, long contentLength, boolean done) {
            this.bytesRead = bytesRead;
            this.contentLength = contentLength;
            this.done = done;
        }

        public long bytesRead;
        public long contentLength;
        public boolean done;
    }
}
