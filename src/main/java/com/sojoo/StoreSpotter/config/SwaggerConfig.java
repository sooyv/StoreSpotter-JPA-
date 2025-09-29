package com.sojoo.StoreSpotter.config;

import com.sojoo.StoreSpotter.api.ErrorApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import com.sojoo.StoreSpotter.api.ApiResult;
import com.sojoo.StoreSpotter.api.SuccessApiResponse;
import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.common.error.ErrorResponse;
import com.sojoo.StoreSpotter.common.error.ExplainError;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class SwaggerConfig {


	@Bean
	public OpenAPI openAPI() {
		// API 기본 정보 설정
		Info info = new Info()
				.title("StoreSpotter API Document")
				.version("1.0")
				.description(
						"이 API 문서는 StoreSpotter의 API를 사용하는 방법을 설명합니다.\n");

		Components components = new Components();
		// Ensure ErrorResponse schema is registered for $ref
		try {
			io.swagger.v3.core.converter.ResolvedSchema rs = io.swagger.v3.core.converter.ModelConverters.getInstance()
					.resolveAsResolvedSchema(new io.swagger.v3.core.converter.AnnotatedType(com.sojoo.StoreSpotter.common.error.ErrorResponse.class));
			if (rs != null && rs.schema != null) {
				components.addSchemas("ErrorResponse", rs.schema);
				if (rs.referencedSchemas != null) {
					rs.referencedSchemas.forEach(components::addSchemas);
				}
			}
		} catch (Throwable ignored) {}

		return new OpenAPI().info(info).components(components)
				.addServersItem(new Server().url("http://localhost:8080").description("로컬 서버"));
	}

	@Bean
	public OperationCustomizer errorResponsesCustomizer() {
		return (operation, handlerMethod) -> {
			ErrorApiResponses methodErrorAnno = handlerMethod.getMethodAnnotation(ErrorApiResponses.class);
			ErrorApiResponses classErrorAnno = handlerMethod.getBeanType().getAnnotation(ErrorApiResponses.class);
			ApiResult methodApiResult = handlerMethod.getMethodAnnotation(ApiResult.class);
			ApiResult classApiResult = handlerMethod.getBeanType().getAnnotation(ApiResult.class);
			SuccessApiResponse methodSuccessAnno = handlerMethod.getMethodAnnotation(SuccessApiResponse.class);
			SuccessApiResponse classSuccessAnno = handlerMethod.getBeanType().getAnnotation(SuccessApiResponse.class);

			// 우선순위: ApiResult > SuccessApiResponse (Deprecated path)
			String successCode = null;
			String successDesc = null;
			Class<?> successSchema = null;
			String successMediaType = null;
			if (methodApiResult != null || classApiResult != null) {
				ApiResult a = methodApiResult != null ? methodApiResult : classApiResult;
				successCode = a.successCode();
				successDesc = a.successDescription();
				successSchema = a.successSchema();
				successMediaType = a.successMediaType();
			} else if (methodSuccessAnno != null || classSuccessAnno != null) {
				SuccessApiResponse a = methodSuccessAnno != null ? methodSuccessAnno : classSuccessAnno;
				successCode = a.responseCode();
				successDesc = a.description();
				successSchema = a.schema();
				successMediaType = a.mediaType();
			}

			if (successCode != null) {
				ApiResponses responses = operation.getResponses();
				if (responses == null) {
					responses = new ApiResponses();
					operation.setResponses(responses);
				}
				ApiResponse ok = new ApiResponse();
				ok.setDescription(successDesc);
				Content content = new Content();
				MediaType mediaType = new MediaType();
				if (successSchema != null && !Void.class.equals(successSchema)) {
					mediaType.setSchema(new Schema<>().$ref("#/components/schemas/" + successSchema.getSimpleName()));
				}
				content.addMediaType(successMediaType != null ? successMediaType : "application/json", mediaType);
				ok.setContent(content);
				responses.addApiResponse(successCode, ok);
			}

			ErrorCode[] codes = null;
			if (methodApiResult != null || classApiResult != null) {
				ApiResult a = methodApiResult != null ? methodApiResult : classApiResult;
				if (a.errors() != null && a.errors().length > 0) {
					codes = a.errors();
				}
			}
			if (codes == null) {
				if (methodErrorAnno == null && classErrorAnno == null) {
					return operation;
				}
				codes = methodErrorAnno != null ? methodErrorAnno.value() : classErrorAnno.value();
			}

			// group by status to aggregate multiple examples per HTTP status
			Map<Integer, ApiResponse> statusToResponse = new LinkedHashMap<>();

			for (ErrorCode code : codes) {
				int status = code.getStatus();
				ApiResponse apiResp = statusToResponse.computeIfAbsent(status, s -> {
					ApiResponse r = new ApiResponse();
					r.setDescription("에러 응답");
					Content content = new Content();
					MediaType mediaType = new MediaType();
					mediaType.setSchema(new Schema<ErrorResponse>().$ref("#/components/schemas/ErrorResponse"));
					content.addMediaType("application/json", mediaType);
					r.setContent(content);
					return r;
				});

				MediaType mt = apiResp.getContent().get("application/json");
				if (mt.getExamples() == null) {
					mt.setExamples(new LinkedHashMap<>());
				}
				String exampleName = code.name();
				mt.getExamples().put(exampleName, new io.swagger.v3.oas.models.examples.Example()
						.summary(code.getErrorCode())
						.description(getExplain(code))
						.value(buildExampleJson(code)));
			}

			ApiResponses targetResponses = operation.getResponses();
			if (targetResponses == null) {
				targetResponses = new ApiResponses();
				operation.setResponses(targetResponses);
			}
			for (java.util.Map.Entry<Integer, ApiResponse> entry : statusToResponse.entrySet()) {
				targetResponses.addApiResponse(String.valueOf(entry.getKey()), entry.getValue());
			}

			return operation;
		};
	}

	private static String buildExampleJson(ErrorCode errorCode) {
		return "{" +
				"\n  \"status\": " + errorCode.getStatus() + "," +
				"\n  \"code\": \"" + errorCode.getErrorCode() + "\"," +
				"\n  \"message\": \"" + escape(errorCode.getMessage()) + "\"\n" +
				"}";
	}

	private static String getExplain(ErrorCode errorCode) {
		try {
			Field field = ErrorCode.class.getField(errorCode.name());
			ExplainError annotation = field.getAnnotation(ExplainError.class);
			if (Objects.nonNull(annotation) && annotation.value() != null && !annotation.value().isEmpty()) {
				return annotation.value();
			}
		} catch (NoSuchFieldException ignored) {}
		return errorCode.getMessage();
	}

	private static String escape(String s) {
		return s.replace("\"", "\\\"");
	}
}
