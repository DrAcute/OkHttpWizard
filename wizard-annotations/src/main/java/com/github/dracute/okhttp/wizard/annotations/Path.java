package com.github.dracute.okhttp.wizard.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by DrAcute on 2015/9/18.
 */
@Target(ElementType.PARAMETER) @Retention(RetentionPolicy.CLASS)
public @interface Path {
    String value() default "";
    boolean nullable() default false;
}
