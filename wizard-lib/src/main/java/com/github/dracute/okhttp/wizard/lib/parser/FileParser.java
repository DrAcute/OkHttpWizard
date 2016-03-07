package com.github.dracute.okhttp.wizard.lib.parser;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by DrAcute on 2016/1/27.
 */
public class FileParser implements IParser<File> {

    private String savePath;
    private String fileName;
    private long hasDownload = 0l;
    private boolean autoResume;

    public FileParser(String savePath, String fileName, boolean autoResume) {
        this.savePath = savePath;
        this.fileName = fileName;
        this.autoResume = autoResume;
        if (autoResume) {
            File file = new File(savePath, fileName);
            if (file.exists()) {
                hasDownload = file.length();
            }
        }
    }

    public String getSavePath() {
        return savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isAutoResume() {
        return autoResume;
    }

    public long getHasDownload() {
        return hasDownload;
    }

    public boolean isAutoResumeAndFileExist() {
        return autoResume && hasDownload != 0l;
    }

    @Override
    public Pair<Integer, File> parser(Response response) throws IOException {
        if (response.code() == 416) {
            return new Pair<>(416, new File(savePath, fileName));
        }

        if (autoResume && hasDownload > 0) {
            if (!TextUtils.isEmpty(response.header("Content-Range"))) {
                String content_range = response.header("Content-Range");
                String[] s = content_range.trim().split("/");
                if (!TextUtils.isEmpty(s[1])) {
                    long total = Long.valueOf(s[1]);
                    if (total <= hasDownload) {
                        return new Pair<>(416, new File(savePath, fileName));
                    }
                }
            } else if (!TextUtils.isEmpty(response.header("Content-Length"))) {
                String content_length = response.header("Content-Length");
                long total = Long.valueOf(content_length.trim());
                if (total <= hasDownload) {
                    return new Pair<>(416, new File(savePath, fileName));
                }
            }
        }

        InputStream is;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos;
        is = response.body().byteStream();
        File file = getFile(response);
        if (file.exists() && autoResume) {
            fos = new FileOutputStream(file, true);
        } else {
            fos = new FileOutputStream(file);
        }
        while ((len = is.read(buf)) != -1) {
            fos.write(buf, 0, len);
        }
        fos.flush();
        return new Pair<>(response.code(), file);
    }

    @Override
    public ProgressEntity progress(long bytesRead, long contentLength, boolean done) {
        return new ProgressEntity(bytesRead + hasDownload, contentLength + hasDownload, done);
    }

    private File getFile(Response response) {
        if (TextUtils.isEmpty(savePath)) {
            int index = fileName.lastIndexOf("/");
            if (index > 0 && index < fileName.length() - 1) {
                savePath = fileName.substring(0, index + 1);
                fileName = fileName.substring(index + 1);
            } else {
                throw new IllegalArgumentException("");
            }
        }
        File savePathFile = new File(savePath);
        if (!savePathFile.exists()) {
            savePathFile.mkdirs();
        }
        return new File(savePathFile, getFileName(response.request().urlString(), fileName));
    }

    private static String getFileName(String path, String saveName) {
        if (!TextUtils.isEmpty(saveName)) {
            return saveName;
        }
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }
}
