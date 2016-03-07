package com.github.dracute.okhttp.wizard.compiler.builder;

import com.github.dracute.okhttp.wizard.compiler.param.RequestParam;
import com.github.dracute.okhttp.wizard.compiler.utils.TextUtils;
import com.github.dracute.okhttp.wizard.compiler.utils.TypeUtils;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

/**
 * Created by DrAcute on 2016/1/4.
 */
public class MethodBuilder {

    RequestParam requestParam;
    String methodName;
    TypeName returnType;
    TypeName returnParam;
    TypeName tokenParam;
    List<ArgBuilder> argBuilderList;
    String callType;

    DownloadBuilder download;

    public MethodBuilder setMethodName(String name) {
        this.methodName = name;
        return this;
    }

    public MethodBuilder setRequestParam(RequestParam param) {
        this.requestParam = param;
        return this;
    }

    public MethodBuilder setTokenParam(TypeName tokenParam) {
        this.tokenParam = tokenParam;
        return this;
    }

    public MethodBuilder setReturnType(TypeName returnType) {
        this.returnType = returnType;
        return this;
    }

    public MethodBuilder setReturnParam(TypeName returnParam) {
        this.returnParam = returnParam;
        return this;
    }

    public MethodBuilder addArgs(ArgBuilder arg) {
        if (argBuilderList == null) {
            argBuilderList = new ArrayList<>();
        }
        argBuilderList.add(arg);
        return this;
    }

    public MethodBuilder setCallType(String callType) {
        this.callType = callType;
        return this;
    }

    public MethodBuilder setDownload(DownloadBuilder download) {
        this.download = download;
        return this;
    }

    public MethodSpec build() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(returnType);

        if (argBuilderList != null) {
            for (ArgBuilder args : argBuilderList) {
                methodBuilder.addParameter(args.argType, args.argName);
            }
        }

        builderRequest(methodBuilder);

        CodeBlock.Builder callBuilder = CodeBlock.builder();

        if ("CALL".equals(callType)) {
            callBuilder.add("$T wizard = new $T();\n", returnType, returnType);
            callBuilder.add("wizard.setClient(okhttp)");
            callBuilder.add(".setRequest(requestBuilder)");
            if (download != null) {
                callBuilder.add(".setParser(new $T(" + download.savePath + ", " + download.fileName + ", " + download.autoResume +"))", TypeUtils.Name_FileParser);
            } else {
                callBuilder.add(".setParser(new $T(new $T<" + tokenParam.toString() + ">(){}))", TypeUtils.Name_JsonParser, TypeUtils.Name_Json);
            }
            callBuilder.add(";\n");
            callBuilder.addStatement("return wizard");
        } else if ("VOID".equals(callType)) {
            callBuilder.add("$T wizard = new $T();\n", TypeUtils.Name_WizardCall, TypeUtils.Name_WizardCall);
            callBuilder.add("wizard.setClient(okhttp)");
            callBuilder.add(".setRequest(requestBuilder)");
            callBuilder.add(".enqueue();\n");
        }

        methodBuilder.addCode(callBuilder.build());
        return methodBuilder.build();
    }

    private void builderRequest(MethodSpec.Builder methodBuilder) {
        CodeBlock.Builder requestBuilder = CodeBlock.builder();
        if ("Get".equals(requestParam.requestMethod)) {
            requestBuilder.add("$T requestBuilder = new $T(config);\n", TypeUtils.Name_GetRequestBuilder, TypeUtils.Name_GetRequestBuilder);
        } else if ("Post".equals(requestParam.requestMethod)) {
            requestBuilder.add("$T requestBuilder = new $T(config);\n", TypeUtils.Name_PostRequestBuilder, TypeUtils.Name_PostRequestBuilder);
        }
        requestBuilder.add("requestBuilder");
        if (requestParam.host != null) {
            requestBuilder.add(".setHost(\""+ requestParam.host +"\")");
        }
        requestBuilder.add(".setPath(\"" + requestParam.path + "\")");
        if (requestParam.useQuerySymbol == 1) {
            requestBuilder.add(".useQuerySymbol(false)");
        } else {
            requestBuilder.add(".useQuerySymbol(true)");
        }

        if (argBuilderList != null) {
            for (ArgBuilder args : argBuilderList) {
                if (args.argParam == null) {
                    continue;
                }
                if ("Path".equals(args.argParam.argType)) {
                    if (TextUtils.isEmpty(args.argParam.fieldName)) {
                        args.argParam.fieldName = args.argName;
                    }
                    if (args.argType.equals(TypeName.get(String.class))) {
                        requestBuilder.add(".addUrlParam(\""+ args.argParam.fieldName +"\", " + args.argName + ")");
                    } else {
                        requestBuilder.add(".addUrlParam(\""+ args.argParam.fieldName +"\", String.valueOf(" + args.argName + "))");
                    }
                } else if ("Field".equals(args.argParam.argType)) {
                    if (TextUtils.isEmpty(args.argParam.fieldName)) {
                        args.argParam.fieldName = args.argName;
                    }
                    if (args.argType.equals(TypeName.get(String.class))) {
                        requestBuilder.add(".addRequestParam(\""+ args.argParam.fieldName +"\", " + args.argName + ")");
                    } else {
                        requestBuilder.add(".addRequestParam(\""+ args.argParam.fieldName +"\", String.valueOf(" + args.argName + "))");
                    }
                }
            }
        }
        requestBuilder.add(";\n");
        methodBuilder.addCode(requestBuilder.build());
    }

}
