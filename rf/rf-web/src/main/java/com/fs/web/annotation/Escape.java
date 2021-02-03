package com.fs.web.annotation;

import com.fs.web.enums.EscapeType;

import java.lang.annotation.*;

/**
 * 格式脱离，防止被注入恶意脚本
 * @author fk-7075
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Escape {

	EscapeType value() default EscapeType.HTML4;

}
