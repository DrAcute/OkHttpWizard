package com.github.dracute.okhttp.wizard.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by DrAcute on 2016/1/14.
 */
@Target(ElementType.PARAMETER) @Retention(RetentionPolicy.CLASS)
public @interface Download {
    String savePath() default "";
    boolean autoResume() default false;
}
