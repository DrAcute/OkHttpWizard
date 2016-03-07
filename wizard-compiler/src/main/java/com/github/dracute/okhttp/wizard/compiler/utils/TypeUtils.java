package com.github.dracute.okhttp.wizard.compiler.utils;

import com.squareup.javapoet.ClassName;

/**
 * Created by DrAcute on 2016/1/7.
 */
public class TypeUtils {
    public static final ClassName Name_WizardCall = ClassName.get("com.github.dracute.okhttp.wizard.lib", "WizardCall");

    public static final ClassName Name_OkHttpClient = ClassName.get("com.squareup.okhttp", "OkHttpClient");

    public static final ClassName Name_WizardConfig = ClassName.get("com.github.dracute.okhttp.wizard.lib", "WizardConfig");

    public static final ClassName Name_GetRequestBuilder = ClassName.get("com.github.dracute.okhttp.wizard.lib.builder", "GetRequestBuilder");
    public static final ClassName Name_PostRequestBuilder = ClassName.get("com.github.dracute.okhttp.wizard.lib.builder", "PostRequestBuilder");

    public static final ClassName Name_JsonParser = ClassName.get("com.github.dracute.okhttp.wizard.lib.parser", "JsonParser");
    public static final ClassName Name_FileParser = ClassName.get("com.github.dracute.okhttp.wizard.lib.parser", "FileParser");

    public static final ClassName Name_DownloadParam = ClassName.get("com.github.dracute.okhttp.wizard.lib.param", "DownloadParam");

    public static final ClassName Name_Json = ClassName.get("com.alibaba.fastjson", "TypeReference");
}
