package com.github.dracute.okhttp.wizard.compiler.builder;

/**
 * Created by DrAcute on 2016/1/14.
 */
public class DownloadBuilder {
    String savePath;
    String fileName;
    String autoResume;

    public DownloadBuilder(String savePath, String fileName, String autoResume) {
        this.savePath = savePath;
        this.fileName = fileName;
        this.autoResume = autoResume;
    }
}
