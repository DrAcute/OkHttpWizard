package com.github.dracute.okhttpwizard.lib;

/**
 * Created by DrAcute on 2015/12/24.
 */
public interface ProgressCallback {
    void onProgress(long bytesRead, long contentLength, boolean done);
}
