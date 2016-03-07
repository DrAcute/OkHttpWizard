package com.github.dracute.okhttp.wizard.compiler.builder;

import com.github.dracute.okhttp.wizard.compiler.param.ArgParam;
import com.squareup.javapoet.TypeName;

/**
 * Created by DrAcute on 2016/1/5.
 */
public class ArgBuilder {

    ArgParam argParam;
    public String argName;
    public TypeName argType;

    public ArgBuilder setArgName(String argName) {
        this.argName = argName;
        return this;
    }

    public ArgBuilder setArgType(TypeName argType) {
        this.argType = argType;
        return this;
    }

    public ArgBuilder setArgParam(ArgParam argParam) {
        this.argParam = argParam;
        return this;
    }
}
