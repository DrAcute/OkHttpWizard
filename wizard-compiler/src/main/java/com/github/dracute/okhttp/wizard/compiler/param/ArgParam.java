package com.github.dracute.okhttp.wizard.compiler.param;

/**
 * Created by DrAcute on 2016/1/5.
 */
public class ArgParam {
    public String argType;
    public String fieldName;
    public boolean nullable;

    public ArgParam(String argType, String fieldName, boolean nullable) {
        this.argType = argType;
        this.fieldName = fieldName;
        this.nullable = nullable;
    }
}
