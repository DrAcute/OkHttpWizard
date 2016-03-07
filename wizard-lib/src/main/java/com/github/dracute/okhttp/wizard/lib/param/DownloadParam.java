package com.github.dracute.okhttp.wizard.lib.param;

/**
 * Created by DrAcute on 2016/1/29.
 */
public class DownloadParam {
    String savePath;
    String fileName;
    boolean autoResume = false;

    public DownloadParam() {
    }

    public DownloadParam(String savePath, String fileName, boolean autoResume) {
        this.savePath = savePath;
        this.fileName = fileName;
        this.autoResume = autoResume;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isAutoResume() {
        return autoResume;
    }

    public void setAutoResume(boolean autoResume) {
        this.autoResume = autoResume;
    }
}
