package com.sojoo.StoreSpotter.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SuccessApiResponse {
	String responseCode() default "200";
	String description() default "성공";
	Class<?> schema() default Void.class;
	String mediaType() default "application/json";
} 