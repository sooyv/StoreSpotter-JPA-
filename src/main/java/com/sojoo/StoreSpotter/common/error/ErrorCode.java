package com.sojoo.StoreSpotter.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
	// 400
	@ExplainError("이미 등록된 이메일입니다.")
	EMAIL_DUPLICATION(400,"USER-DUPLICATE-400","EMAIL DUPLICATED"),
	@ExplainError("입력한 메일 인증 코드가 일치하지 않습니다.")
	MAIL_CODE_NOT_EQUAL_400(400, "MAIL-CODE-NOT-EQUAL-400", "NOT EQUAL MAILCODE"),
	@ExplainError("메일 인증 코드의 유효 시간이 만료되었습니다.")
	MAIL_CODE_EXPIRED_400(400, "MAIL-CODE-EXPIRED-400", "EXPIRATION MAILCODE"),


	// 500
	@ExplainError("해당 사용자를 찾을 수 없습니다.")
	USER_NOT_FOUND(500, "USER-NOT-FOUND", "USER NOT FOUND"),
	@ExplainError("추천할 업종 데이터를 찾을 수 없습니다.")
	DATA_RECOMMEND_NOT_FOUND(500, "DATA-RECOMMEND-NOT-FOUND", "DATA(INDUSTRY) RECOMMEND NOT FOUND"),
	@ExplainError("외부 API로부터 데이터를 가져오지 못했습니다.")
	API_DATA_NOT_FOUND(500, "API-DATA-NOT-FOUND", "API DATA NOT FOUND"),
	@ExplainError("데이터 페어 생성 중 오류가 발생했습니다.")
	DATA_PAIR_CREATE_FAILED(500, "DATA-PAIR-CREATE-FAILED", "DATA PAIR CREATE FAILED"),


	//421
	@ExplainError("메일 서버(SMTP) 전송에 실패했습니다.")
	SMTP_SEND_FAILED(421, "SMTP-SEND-FAILED-421", "SMTP SEND FAILED");

	private final int status;
	private final String errorCode;
	private final String message;
}