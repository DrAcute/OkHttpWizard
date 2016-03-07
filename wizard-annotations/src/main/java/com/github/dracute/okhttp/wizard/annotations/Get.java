package com.github.dracute.okhttp.wizard.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by DrAcute on 2016/1/4.
 */
@Target(ElementType.METHOD) @Retention(RetentionPolicy.CLASS)
public @interface Get {
    String value() default "";
}
