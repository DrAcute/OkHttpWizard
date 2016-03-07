package com.github.dracute.okhttpwizard.lib.param;

import java.io.File;

/**
 * Created by DrAcute on 2015/12/21.
 */
public abstract class ParamHelper {

    public ParamHelper addParam(String name, String value) {
        putParam(name, value);
        return this;
    }

    public ParamHelper addParam(String name, char[] value) {
        putParam(name, String.valueOf(value));
        return this;
    }

    public ParamHelper addParam(String name, char value) {
        putParam(name, String.valueOf(value));
        return this;
    }

    public ParamHelper addParam(String name, double value) {
        putParam(name, String.valueOf(value));
        return this;
    }

    public ParamHelper addParam(String name, float value) {
        putParam(name, String.valueOf(value));
        return this;
    }

    public ParamHelper addParam(String name, int value) {
        putParam(name, String.valueOf(value));
        return this;
    }

    public ParamHelper addParam(String name, long value) {
        putParam(name, String.valueOf(value));
        return this;
    }

    public ParamHelper addParam(String name, Object value) {
        putParam(name, String.valueOf(value));
        return this;
    }

    public ParamHelper addParam(String name, boolean value) {
        putParam(name, String.valueOf(value));
        return this;
    }

    public ParamHelper addParamIfNullable(String name, String value) {
        if (value != null) {
            putParam(name, value);
        }
        return this;
    }

    public ParamHelper addParamIfNullable(String name, String value, String defaultValue) {
        if (value != null) {
            putParam(name, value);
        } else {
            putParam(name, defaultValue);
        }
        return this;
    }

    public ParamHelper addParamIfNullable(String name, Object value) {
        if (value != null) {
            putParam(name, String.valueOf(value));
        }
        return this;
    }

    public ParamHelper addParam(String name, File file) {
        return this;
    }

    public ParamHelper addParam(String name, File file, String fileName, String fileType) {
        return this;
    }

    protected abstract void putParam(String name, String value);
}
