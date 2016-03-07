package com.github.dracute.okhttp.wizard.compiler.builder;

import com.github.dracute.okhttp.wizard.compiler.utils.TypeUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.element.Modifier;

/**
 * Created by DrAcute on 2016/1/4.
 */
public class ClassBuilder {

    private String classPackage;
    private String className;
    private TypeName targetType;

    private Map<String, MethodBuilder> methodBuilderMap;

    public ClassBuilder(String classPackage, String className, TypeName targetType) {
        this.classPackage = classPackage;
        this.className = className;
        this.targetType = targetType;
        this.methodBuilderMap = new LinkedHashMap<>();
    }

    public boolean addMethod(MethodBuilder methodBuilder) {
        if (methodBuilderMap.containsKey(methodBuilder.methodName)) {
            return false;
        } else {
            methodBuilderMap.put(methodBuilder.methodName, methodBuilder);
            return true;
        }
    }

    public JavaFile build() {
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(targetType);

        typeBuilder.addField(TypeUtils.Name_OkHttpClient, "okhttp", Modifier.PRIVATE);
        typeBuilder.addField(TypeUtils.Name_WizardConfig, "config", Modifier.PRIVATE);

        typeBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeUtils.Name_OkHttpClient, "okhttp")
                .addParameter(TypeUtils.Name_WizardConfig, "config")
                .addStatement("this.okhttp = okhttp")
                .addStatement("this.config = config")
                .build());

        for (MethodBuilder methodBuilder : methodBuilderMap.values()) {
            typeBuilder.addMethod(methodBuilder.build());
        }
        TypeSpec typeSpec = typeBuilder.build();
        return JavaFile.builder(classPackage, typeSpec).build();
    }
}
