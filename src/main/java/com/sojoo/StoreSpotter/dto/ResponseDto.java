//package com.sojoo.StoreSpotter.dto;
//
//import com.sojoo.StoreSpotter.common.error.ErrorCode;
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.Getter;
//import org.hibernate.procedure.spi.ParameterRegistrationImplementor;
//
//@Schema(name = "StandardResponse", description = "API 표준응답")
//@Getter
//public class ResponseDto<T> {
//
//    @Schema(description = "HTTP 상태코드", example = "200")
//    private int status;
//    @Schema(description = "성공: OK, 실패: ErrorCode", example = "OK")
//    private String code;
//    @Schema(description = "상세 메시지")
//    private String message;
//
//    private ResponseDto(int status, String code, String message) {
//        this.status = status;
//        this.code = code;
//        this.message = message;
//    }
//
//    // 성공 팩토리
//    public static <T> ResponseDto<T> ok() {
//        return new ResponseDto<>(200, "OK", "성공");
//    }
//
//    //실패 팩토리
//    public static <T> ResponseDto<T> error(ErrorCode e) {
//        return new ResponseDto<>(e.getStatus(), e.getErrorCode(), e.getMessage());
//    }
//}
