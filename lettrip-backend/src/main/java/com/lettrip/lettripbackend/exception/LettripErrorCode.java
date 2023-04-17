package com.lettrip.lettripbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LettripErrorCode {
    DUPLICATED_EMAIL("이미 해당 Email로 가입된 계정이 존재합니다."),
    USER_NOT_FOUND("해당 Email로 가입된 계정이 존재하지 않습니다."),
    EMAIL_CODE_NOT_MATCH("이메일 인증 코드가 일치하지 않습니다."),
    WRONG_PASSWORD("비밀번호가 일치하지 않습니다."),

    INTERNAL_SERVER_ERROR("서버에 오류가 발생했습니다."),
    INVALID_REQUEST("잘못된 요청입니다.");

    private final String message;
}
