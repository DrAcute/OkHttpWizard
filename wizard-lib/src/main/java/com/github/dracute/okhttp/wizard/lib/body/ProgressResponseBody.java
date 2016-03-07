package com.github.dracute.okhttp.wizard.lib.body;

import com.github.dracute.okhttp.wizard.lib.callback.ProgressCallback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by DrAcute on 2015/12/23.
 */
public class ProgressResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private final ProgressCallback progressListener;
    private BufferedSource bufferedSource;
    private long hasDownload = 0l;

    public ProgressResponseBody(ResponseBody responseBody, ProgressCallback progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    public ProgressResponseBody(ResponseBody responseBody, ProgressCallback progressListener, long hasDownload) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
        this.hasDownload = hasDownload;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() throws IOException {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {

            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                progressListener.onProgress(totalBytesRead + hasDownload, responseBody.contentLength() + hasDownload, bytesRead == -1);
                return bytesRead;
            }
        };
    }
}
