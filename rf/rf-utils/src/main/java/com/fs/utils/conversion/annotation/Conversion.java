package com.fs.utils.conversion.annotation;

import com.fs.utils.conversion.LuckyConversion;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conversion {

    String id() default "";

    Class<? extends LuckyConversion>[] value() default LuckyConversion.class;
}
