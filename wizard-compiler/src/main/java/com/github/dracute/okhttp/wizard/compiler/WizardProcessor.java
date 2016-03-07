package com.github.dracute.okhttp.wizard.compiler;

import com.github.dracute.okhttp.wizard.annotations.Download;
import com.github.dracute.okhttp.wizard.annotations.Field;
import com.github.dracute.okhttp.wizard.annotations.Get;
import com.github.dracute.okhttp.wizard.annotations.Headers;
import com.github.dracute.okhttp.wizard.annotations.Host;
import com.github.dracute.okhttp.wizard.annotations.Path;
import com.github.dracute.okhttp.wizard.annotations.Post;
import com.github.dracute.okhttp.wizard.annotations.UseQuerySymbolInUrl;
import com.github.dracute.okhttp.wizard.compiler.builder.ClassBuilder;
import com.github.dracute.okhttp.wizard.compiler.builder.DownloadBuilder;
import com.github.dracute.okhttp.wizard.compiler.param.RequestParam;
import com.github.dracute.okhttp.wizard.compiler.utils.TextUtils;
import com.github.dracute.okhttp.wizard.compiler.builder.ArgBuilder;
import com.github.dracute.okhttp.wizard.compiler.builder.MethodBuilder;
import com.github.dracute.okhttp.wizard.compiler.param.ArgParam;
import com.github.dracute.okhttp.wizard.compiler.utils.TypeUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleTypeVisitor7;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class WizardProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Types typeUtils;
    private Filer filer;

    @Override
    public Set<String> getSupportedAnnotationTypes() {

        Set<String> types = new LinkedHashSet<>();
        types.add(Get.class.getCanonicalName());
        types.add(Post.class.getCanonicalName());
        types.add(Field.class.getCanonicalName());
        types.add(Path.class.getCanonicalName());
        types.add(Host.class.getCanonicalName());
        types.add(UseQuerySymbolInUrl.class.getCanonicalName());
        types.add(Download.class.getCanonicalName());
        types.add(Headers.class.getCanonicalName());
        return types;
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        elementUtils = env.getElementUtils();
        typeUtils = env.getTypeUtils();
        filer = env.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<TypeElement, ClassBuilder> classBuilderMap = findRequestTarget(roundEnv);


        for (ClassBuilder classBuilder : classBuilderMap.values()) {
            try {
                classBuilder.build().writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Map<TypeElement, ClassBuilder> findRequestTarget(RoundEnvironment roundEnv) {
        Map<TypeElement, ClassBuilder> classBuilderMap = new LinkedHashMap<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(Get.class)) {
            Get get = element.getAnnotation(Get.class);
            findRequestTarget(new RequestParam("Get", get.value()), classBuilderMap, element);
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Post.class)) {
            Post post = element.getAnnotation(Post.class);
            findRequestTarget(new RequestParam("Post", post.value()), classBuilderMap, element);
        }
        return classBuilderMap;
    }

    private void findRequestTarget(RequestParam param, Map<TypeElement, ClassBuilder> classBuilderMap, Element element) {
        if (verifyMethodElement(element)) {
            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            ClassBuilder classBuilder = classBuilderMap.get(enclosingElement);
            if (classBuilder == null) {
                TypeName targetType = TypeName.get(enclosingElement.asType());
                String classPackage = elementUtils.getPackageOf(enclosingElement).getQualifiedName().toString();
                String className = enclosingElement.getQualifiedName().toString().substring(classPackage.length() + 1).replace('.', '$') + "$$Impl";

                classBuilder = new ClassBuilder(classPackage, className, targetType);
                classBuilderMap.put(enclosingElement, classBuilder);
            }

            if (!classBuilder.addMethod(findRequestMethod(param, element))) {
                error(element, "%s has a bug, or use same method name", element.getSimpleName());
            }
        }
    }

    private MethodBuilder findRequestMethod(RequestParam param, Element element) {

        param.host = getHost(element);
        param.useQuerySymbol = userQuerySymbol(element);

        ExecutableElement executableElement = (ExecutableElement) element;
        Pa pa = getTypeArgumentEx(executableElement.getReturnType(), new Pa());
        MethodBuilder methodBuilder = new MethodBuilder();
        methodBuilder.setRequestParam(param)
                .setMethodName(executableElement.getSimpleName().toString())
                .setReturnType(TypeName.get(executableElement.getReturnType()))
                .setCallType(pa.type)
                .setReturnParam(pa.returnType)
                .setTokenParam(pa.tokenParam);

        for (VariableElement variableElement : executableElement.getParameters()) {
            methodBuilder.addArgs(findArgs(variableElement, methodBuilder));
        }
        return methodBuilder;
    }

    private ArgBuilder findArgs(VariableElement element, MethodBuilder methodBuilder) {
        ArgBuilder argBuilder = new ArgBuilder();
        argBuilder.setArgName(element.getSimpleName().toString())
                .setArgType(TypeName.get(element.asType()));
        if (element.getAnnotation(Path.class) != null) {
            Path path = element.getAnnotation(Path.class);
            argBuilder.setArgParam(new ArgParam("Path", TextUtils.useDefaultIfEmpty(path.value(), element.getSimpleName().toString()), path.nullable()));
        } else if (element.getAnnotation(Field.class) != null) {
            Field field = element.getAnnotation(Field.class);
            argBuilder.setArgParam(new ArgParam("Field", TextUtils.useDefaultIfEmpty(field.value(), element.getSimpleName().toString()), field.nullable()));
        } else if (element.getAnnotation(Download.class) != null) {
            if (verifyDownload(element.asType())) {
                Download download = element.getAnnotation(Download.class);
                argBuilder.setArgParam(null);
                String savePath = "null";
                String saveName = "null";
                String autoResume = "false";
                if (TypeName.get(element.asType()).equals(TypeName.get(String.class))) {
                    if (!TextUtils.isEmpty(download.savePath())) {
                        savePath = "\"" + download.savePath() + "\"";
                    }
                    saveName = element.getSimpleName().toString();
                    if (download.autoResume()) {
                        autoResume = "true";
                    } else {
                        autoResume = "false";
                    }
                } else if (TypeName.get(element.asType()).equals(TypeUtils.Name_DownloadParam)) {
                    savePath = element.getSimpleName().toString() + ".getSavePath()";
                    saveName = element.getSimpleName().toString() + ".getFileName()";
                    autoResume = element.getSimpleName().toString() + ".isAutoResume()";
                }
                methodBuilder.setDownload(new DownloadBuilder(savePath, saveName, autoResume));
            }
        } else {
            argBuilder.setArgParam(null);
        }
        return argBuilder;
    }

    private boolean verifyMethodElement(Element e) {
        if (!e.getKind().equals(ElementKind.METHOD)) {
            error(e, "%s isn't a method", e.getSimpleName());
            return false;
        }
        if (!e.getEnclosingElement().getKind().equals(ElementKind.INTERFACE)) {
            error(e, "%s isn't belong an interface", e.getSimpleName());
            return false;
        }
        if (!verifyReturn(((ExecutableElement)e).getReturnType())) {
            error(e, "%s has wrong return type", e.getSimpleName());
            return false;
        }
        return true;
    }

    private boolean verifyReturn(TypeMirror mirror) {
        return mirror.accept(new SimpleTypeVisitor7<Boolean, Void>(false) {
            @Override
            public Boolean visitNoType(NoType t, Void p) {
                return t.getKind() == TypeKind.VOID;
            }

            @Override
            public Boolean visitDeclared(DeclaredType t, Void aVoid) {
                if (ClassName.get((TypeElement) t.asElement()).equals(TypeUtils.Name_WizardCall)) return true;
                List<? extends TypeMirror> superTypes = typeUtils.directSupertypes(t);
                return !superTypes.isEmpty() && verifyReturn(superTypes.get(0));
            }
        }, null);
    }

    private boolean verifyDownload(TypeMirror mirror) {
        return mirror.accept(new SimpleTypeVisitor7<Boolean, Void>(false) {
            @Override
            public Boolean visitDeclared(DeclaredType t, Void aVoid) {
                if (ClassName.get((TypeElement) t.asElement()).equals(TypeName.get(String.class))) return true;
                if (ClassName.get((TypeElement) t.asElement()).equals(TypeUtils.Name_DownloadParam)) return true;
                error(t.asElement(), "%s has wrong download type", t.asElement().getSimpleName());
                return false;
            }
        }, null);
    }

    private Pa getTypeArgumentEx(TypeMirror mirror, Pa pa) {
        return mirror.accept(new SimpleTypeVisitor7<Pa, Void>(null) {

            @Override
            public Pa visitNoType(NoType t, Void p) {
                pa.type = "VOID";
                return pa;
            }

            @Override public Pa visitDeclared(DeclaredType t, Void p) {
                if (pa.returnType == null) {
                    if (!t.getTypeArguments().isEmpty()) {
                        pa.returnType = TypeName.get(t.getTypeArguments().get(0));
                    }
                }
                List<? extends TypeMirror> superTypes = typeUtils.directSupertypes(t);
                if (superTypes.isEmpty()) return pa;
                if (ClassName.get((TypeElement) t.asElement()).equals(TypeUtils.Name_WizardCall)) {
                    if (!pa.finalType) {
                        pa.finalType = true;
                        pa.type = "CALL";
                    }
                    if (!t.getTypeArguments().isEmpty()) {
                        pa.tokenParam = TypeName.get(t.getTypeArguments().get(0));
                    }
                    return pa;
                }
                return getTypeArgumentEx(superTypes.get(0), pa);
            }
        }, null);
    }

    private int userQuerySymbol(Element e) {
        UseQuerySymbolInUrl useQuerySymbolInUrl = e.getAnnotation(UseQuerySymbolInUrl.class);
        if (useQuerySymbolInUrl == null) {
            return 0;
        } else {
            return useQuerySymbolInUrl.value() ? 2 : 1;
        }
    }

    private String getHost(Element e) {
        Host host = e.getAnnotation(Host.class);
        if (host == null) {
            return null;
        } else {
            return host.value();
        }
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    private void note(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message, element);
    }

    private void note(String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }

    public class Pa {
        TypeName tokenParam;
        TypeName returnType;
        String type;
        boolean finalType = false;
    }

}
