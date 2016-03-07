package com.github.dracute.okhttpwizard.lib.param;

import android.text.TextUtils;

/**
 * Created by DrAcute on 2016/1/4.
 */
public class DownloadParam {

    String savePath;
    String fileName;
    Boolean autoResume = false;

    public DownloadParam() {
    }

    public String getSavePath() {
        return savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public Boolean isAutoResume() {
        return autoResume;
    }

    public DownloadParam setAutoResume(boolean autoResume) {
        this.autoResume = autoResume;
        return this;
    }

    public DownloadParam setSavePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

    public DownloadParam setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public DownloadParam setAnnotationValue(String key, String value) {
        switch (key) {
            case "savePath":
                if (TextUtils.isEmpty(savePath)) {
                    savePath = value;
                }
                break;
            case "fileName":
                if (TextUtils.isEmpty(fileName)) {
                    fileName = value;
                }
                break;
            case "autoResume":
                if (autoResume == null) {
                    autoResume = Boolean.valueOf(value);
                }
        }
        return this;
    }

}
