package com.github.dracute.okhttpwizard.lib.param;

import android.text.TextUtils;
import android.util.Pair;

import com.github.dracute.okhttpwizard.lib.utils.Utils;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;

/**
 * Created by DrAcute on 2015/12/22.
 */
public class FileParam implements IParam {

    String name;
    File file;
    String fileName;
    String fileType;

    public FileParam(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public FileParam(String name, File file, String fileName, String fileType) {
        this.name = name;
        this.file = file;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    @Override
    public Pair<Headers, RequestBody> getMultiPart() {
        String _fileName = fileName;
        if (TextUtils.isEmpty(fileName)) {
            _fileName = file.getName();
        }
        RequestBody fileBody = RequestBody.create(MediaType.parse(TextUtils.isEmpty(fileType) ? Utils.guessMimeType(_fileName) : fileType), file);
        return new Pair<>(Headers.of("Content-Disposition",
                        "form-data; name=\"" + name + "\"; filename=\"" + _fileName + "\""),
                fileBody);
    }

    @Override
    public Pair<String, String> getFormEncoding() {
        throw new UnsupportedOperationException();
    }


}
