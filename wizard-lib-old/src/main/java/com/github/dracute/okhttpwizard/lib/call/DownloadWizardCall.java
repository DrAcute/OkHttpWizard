package com.github.dracute.okhttpwizard.lib.call;

import android.text.TextUtils;

import com.github.dracute.okhttpwizard.lib.WizardCall;
import com.github.dracute.okhttpwizard.lib.param.DownloadParam;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by DrAcute on 2015/12/24.
 */
public class DownloadWizardCall<T> extends WizardCall<T> {

    private long hasDownloadByte = 0l;

    protected DownloadParam param;

    protected String url;

    public DownloadWizardCall(WizardCallBuilder builder) {
        super(builder, "DOWNLOAD");
        url = request.urlString();
    }

    public final void setDownloadParam(DownloadParam param) {
        this.param = param;
    }

    @Override
    public void onFailure(final Request request, final IOException e) {
        mainLooperHandler.post(new Runnable() {
            @Override
            public void run() {
                onPostFailure(request, e);
            }
        });
    }

    @Override
    public void onResponse(final Response response) throws IOException {
        if (response.code() == 416) {
            mainLooperHandler.post(new Runnable() {
                @Override
                public void run() {
                    onPostProgress(hasDownloadByte, hasDownloadByte, true);
                    onPostResponse(416, null);
                }
            });
            return;
        }
        InputStream is;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos;
        try {
            is = response.body().byteStream();
            File file = new File(param.getSavePath(), getFileName(url, param.getFileName()));
            if (file.exists() && param.isAutoResume()) {
                fos = new FileOutputStream(file, true);
            } else {
                fos = new FileOutputStream(file);
            }
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            mainLooperHandler.post(new Runnable() {
                @Override
                public void run() {
                    onPostResponse(response.code(), null);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgress(final long bytesRead, final long contentLength, final boolean done) {
        mainLooperHandler.post(new Runnable() {
            @Override
            public void run() {
                onPostProgress(bytesRead, contentLength, done);
            }
        });
    }

    private static String getFileName(String path, String saveName) {
        if (!TextUtils.isEmpty(saveName)) {
            return saveName;
        }
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }
}
