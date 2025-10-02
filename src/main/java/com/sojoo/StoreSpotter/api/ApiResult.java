package com.sojoo.StoreSpotter.api;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.jwt.exception.JwtErrorCode;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiResult {
	// success
	String successCode() default "200";
	String successDescription() default "성공";
	Class<?> successSchema() default Void.class;
	String successMediaType() default "application/json";

	// errors
	ErrorCode[] errors() default {};
	JwtErrorCode[] jwtErrors() default {};
}
