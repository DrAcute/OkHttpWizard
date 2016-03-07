package com.github.dracute.okhttp.wizard.lib.callback;

/**
 * Created by DrAcute on 2015/12/24.
 */
public interface ProgressCallback {
    void onProgress(long bytesRead, long contentLength, boolean done);
}
